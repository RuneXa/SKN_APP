package rpro.com.surveykantinnestle;

/**
 * Created by Richie on 25/02/2016.
 */

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVWriter;

public class DBHelper extends SQLiteOpenHelper {

    //TODO : Ganti Bulan,tanggal,tahun jadi variable yang bisa dipilih

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "surveyKantin";

    // table name
    private static final String TABLE_SURVEY = "survey";
    private static final String TABLE_HASIL_SURVEY = "hasil_survey";
    private static final String TABLE_HASIL_SURVEY_ALASAN_TIDAK = "hasil_alasan_tidak";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIMESTAMP = "time";
    private static final String KEY_RESPOND = "respon";
    private static final String KEY_RESPOND_ALASAN = "alasan";
//    private static final String KEY_RESPOND_ALASAN1 = "alasan1";
//    private static final String KEY_RESPOND_ALASAN2 = "alasan2";
    private static final String KEY_RESPOND_COUNT_YA = "count_ya";
    private static final String KEY_RESPOND_COUNT_TIDAK = "count_tidak";
    private static final String KEY_RESPOND_COUNT_YA_PERS = "count_ya_persen";
    private static final String KEY_RESPOND_COUNT_TIDAK_PERS = "count_tidak_persen";
    private static final String KEY_RESPOND_COUNT_ALASAN = "count_alasan";
    private static final String KEY_RESPOND_COUNT_ALASAN_PERS = "count_alasan_persen";
    private static final String KEY_TIME_JAM = "jam";
    private static final String KEY_TIME_TANGGAL = "tanggal";
    private static final String KEY_TIME_BULAN = "bulan";
    private static final String KEY_TIME_TAHUN = "tahun";

    private static final String CREATE_SURVEY_TABLE = "CREATE TABLE " + TABLE_SURVEY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIME_JAM + " TEXT,"
            + KEY_TIME_TANGGAL + " TEXT,"
            + KEY_TIME_BULAN + " TEXT,"
            + KEY_TIME_TAHUN + " TEXT,"
            + KEY_RESPOND + " TEXT,"
            + KEY_RESPOND_ALASAN + " TEXT"
            + ")";

    /*
    private static final String CREATE_HASIL_SURVEY_TABLE = "CREATE TABLE " + TABLE_HASIL_SURVEY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIME_BULAN + " TEXT,"
            + KEY_TIME_TAHUN + " TEXT,"
            + KEY_RESPOND_COUNT_YA + " INTEGER,"
            + KEY_RESPOND_COUNT_TIDAK + " INTEGER,"
            + KEY_RESPOND_COUNT_YA_PERS + " FLOAT,"
            + KEY_RESPOND_COUNT_TIDAK_PERS + " FLOAT"
            + ")";

    private static final String CREATE_HASIL_ALASAN_TIDAK_TABLE = "CREATE TABLE " + TABLE_HASIL_SURVEY_ALASAN_TIDAK + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIME_BULAN + " TEXT,"
            + KEY_TIME_TAHUN + " TEXT,"
            + KEY_RESPOND_ALASAN + " TEXT,"
            + KEY_RESPOND_COUNT_ALASAN + " INTEGER,"
            + KEY_RESPOND_COUNT_ALASAN_PERS + " FLOAT"
            + ")";
    */

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SURVEY_TABLE);
        //db.execSQL(CREATE_HASIL_SURVEY_TABLE);
        //db.execSQL(CREATE_HASIL_ALASAN_TIDAK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_HASIL_SURVEY);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_HASIL_SURVEY_ALASAN_TIDAK);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    void insertRespond(String respond, String alasan){
        SQLiteDatabase db = this.getWritableDatabase();

        String INSERT_SURVEY_TO_TABLE = "INSERT INTO " + TABLE_SURVEY + "("
               + KEY_TIME_JAM + "," + KEY_TIME_TANGGAL + "," + KEY_TIME_BULAN + "," + KEY_TIME_TAHUN + ","
                + KEY_RESPOND + "," + KEY_RESPOND_ALASAN
                + ") VALUES " +
                "(\""+getDateTimeJam()+
                "\",\""+getDateTimeTanggal()+
                "\",\""+getDateTimeBulan() +
                "\",\""+getDateTimeTahun()+
                "\",\""+respond+
                "\",\""+alasan+
                "\")" ;

        // Inserting Row
        try {
            db.execSQL(INSERT_SURVEY_TO_TABLE);
        } catch(SQLiteException ex){
            Log.e("SqliteErr :", ex.getMessage());
        }
        db.close(); // Closing database connection
    }

    void truncateRespond(Context c){
        SQLiteDatabase db = this.getWritableDatabase();

        String TRUNCATE_SURVEY_TABLE = "DELETE FROM "+ TABLE_SURVEY;

        // Inserting Row
        try {
            db.execSQL(TRUNCATE_SURVEY_TABLE);
        } catch(SQLiteException ex){
            Log.e("SqliteErr :", ex.getMessage());
            Toast.makeText(c,"Error",Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }
        Toast.makeText(c,"Sukses",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }

    /*
    void upsertAlasan(String alasan){
        SQLiteDatabase db = this.getWritableDatabase();

        String INSERT_ALASAN_TO_TABLE = "INSERT INTO " + TABLE_HASIL_SURVEY_ALASAN_TIDAK + "("
                + KEY_TIME_BULAN + "," + KEY_TIME_TAHUN + ","
                + KEY_RESPOND_ALASAN + "," + KEY_RESPOND_COUNT_ALASAN + "," + KEY_RESPOND_COUNT_ALASAN_PERS
                + ") VALUES " +
                "(\"" + getDateTimeBulan() +
                "\",\"" + getDateTimeTahun() +
                "\",\"" + alasan +
                "\"," + "1" +
                "," + "0" +
                ")";

        String SELECT_ALASAN_IF_EXIST = "SELECT * FROM " +TABLE_HASIL_SURVEY_ALASAN_TIDAK
                +" WHERE "+KEY_RESPOND_ALASAN+"=\""+alasan+"\" AND "+KEY_TIME_BULAN+" = \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";


        Cursor selectC = db.rawQuery(SELECT_ALASAN_IF_EXIST, null);
        Log.d("UPSERT Status: ", "Upserting..");
        if(selectC.getCount() != 0){
            Log.d("UPSERT Status: ","C != null");

            String rawQuerySelect_countAlasan = "SELECT * FROM "+TABLE_HASIL_SURVEY_ALASAN_TIDAK
                    +" WHERE "+KEY_RESPOND_ALASAN+"=\""+alasan+"\" AND "+KEY_TIME_BULAN+" = \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";

            Cursor selectCountAlasan = db.rawQuery(rawQuerySelect_countAlasan,null);
            selectCountAlasan.moveToFirst();
            int countAlasanIndex = selectCountAlasan.getColumnIndex(KEY_RESPOND_COUNT_ALASAN);
            int countAlasan = selectCountAlasan.getInt(countAlasanIndex);
            countAlasan++; //increase by 1
            String countAlasanStr = Integer.toString(countAlasan);

            String UPDATE_ALASAN_TO_TABLE = "UPDATE " + TABLE_HASIL_SURVEY_ALASAN_TIDAK + " SET "
                    + KEY_RESPOND_COUNT_ALASAN + "=" + countAlasanStr
                    +" WHERE "+KEY_RESPOND_ALASAN+"=\""+alasan+"\" AND "+KEY_TIME_BULAN+" = \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\""
                    ;


            // Inserting Row
            try {
                db.execSQL(UPDATE_ALASAN_TO_TABLE);
            } catch(SQLiteException ex){
                Log.e("SqliteErr :", ex.getMessage());
            }
            selectC.close();

        } else {
            Log.d("UPSERT Status: ","C == null");
            // Inserting Row
            try {
                db.execSQL(INSERT_ALASAN_TO_TABLE);
            } catch(SQLiteException ex){
                Log.e("SqliteErr :", ex.getMessage());
            }
        }

        db.close(); // Closing database connection
    }


    void createCountTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectCountTotal = "SELECT * FROM "+TABLE_SURVEY+" WHERE "+KEY_TIME_BULAN+"= \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";
        String selectCountYa = "SELECT * FROM " + TABLE_SURVEY +" WHERE "+KEY_RESPOND+" = \"ya\"" + " AND " +KEY_TIME_BULAN+"= \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";
        String selectCountTidak = "SELECT * FROM " + TABLE_SURVEY +" WHERE "+KEY_RESPOND+" = \"tidak\"" + " AND " +KEY_TIME_BULAN+"= \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";

        int countTotalRespond = db.rawQuery(selectCountTotal,null).getCount();
        int countTotalYa = db.rawQuery(selectCountYa,null).getCount();
        int countTotalTidak = db.rawQuery(selectCountTidak,null).getCount();
        float pers_ya = (countTotalYa/countTotalRespond) * 100;
        float pers_tidak = (countTotalTidak/countTotalRespond) * 100;

        String INSERT_HASIL_SURVEY_TO_TABLE = "INSERT INTO " + TABLE_HASIL_SURVEY + "("
                + KEY_TIME_BULAN + "," + KEY_TIME_TAHUN + "," + KEY_RESPOND_COUNT_YA + "," + KEY_RESPOND_COUNT_TIDAK + "," + KEY_RESPOND_COUNT_YA_PERS + "," + KEY_RESPOND_COUNT_TIDAK_PERS
                + ") VALUES (" +
                "\"" + getDateTimeBulan() + "\"," +
                "\"" + getDateTimeTahun() + "\"," +
                Integer.toString(countTotalYa) + "," +
                Integer.toString(countTotalTidak) + "," +
                Float.toString(pers_ya) + "," +
                Float.toString(pers_tidak) +
                ")";

        String UPDATE_HITUNG_HASIL_SURVEY = "UPDATE " + TABLE_HASIL_SURVEY + " SET "
                + KEY_RESPOND_COUNT_YA + " =" + Integer.toString(countTotalYa) +", "
                + KEY_RESPOND_COUNT_TIDAK + " =" + Integer.toString(countTotalTidak) +", "
                + KEY_RESPOND_COUNT_YA_PERS + " =" + Float.toString(pers_ya) +", "
                + KEY_RESPOND_COUNT_TIDAK_PERS + " =" + Float.toString(pers_tidak) +" "
                + "WHERE " + KEY_TIME_BULAN +" = \"" + getDateTimeBulan() +"\" AND " + KEY_TIME_TAHUN +" = \"" + getDateTimeTahun() +"\""  ;


        String SELECT_ROW_IF_EXIST = "SELECT * FROM " +TABLE_HASIL_SURVEY
                +" WHERE "+KEY_TIME_BULAN+" = \""+getDateTimeBulan()+"\"" + " AND " +KEY_TIME_TAHUN+"= \""+getDateTimeTahun()+"\"";


        Cursor selectC = db.rawQuery(SELECT_ROW_IF_EXIST, null);
        Log.d("UPSERT Status: ", "Upserting..");
        if(selectC.getCount() != 0){
            Log.d("UPSERT Status: ","C != null");
            // Inserting Row
            try {
                db.execSQL(UPDATE_HITUNG_HASIL_SURVEY);
            } catch(SQLiteException ex){
                Log.e("SqliteErr :", ex.getMessage());
            }
            selectC.close();
        } else {
            Log.d("UPSERT Status: ","C == null");
            // Inserting Row
            try {
                db.execSQL(INSERT_HASIL_SURVEY_TO_TABLE);
            } catch(SQLiteException ex){
                Log.e("SqliteErr :", ex.getMessage());
            }
        }

        db.close();
    }
    */

    String filename = "Survey_kantin.xls";
    void exportTableSurveyToCSV(){
        SQLiteDatabase sqldb = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = sqldb.rawQuery("select * from " + TABLE_SURVEY, null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            Log.d("SDCard Path", sdCardDir.toString());

            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile,false);
            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + "\t");
                    } else {
                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + "\t");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Log.d("Status :", "Exported Successfully.");
            }
        } catch (IOException ex) {
            if (sqldb.isOpen()) {
                sqldb.close();
                Log.e("Failed :", ex.getMessage());
            }
        } finally {
        }
    }

    /*
    void exportTableHasilSurveyToCSV(){
        SQLiteDatabase sqldb = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = sqldb.rawQuery("select * from " + TABLE_HASIL_SURVEY , null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "Survey_Hasil_" + getDateTimeBulan() +".csv";
            // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile,false);
            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + ",");
                    } else {
                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Log.d("Status :","Exported Successfully.");
            }
        } catch (IOException ex) {
            if (sqldb.isOpen()) {
                sqldb.close();
                Log.e("Failed :", ex.getMessage());
            }
        } finally {
        }
    }

    void exportTableSurveyAlasanTidakToCSV(){
        SQLiteDatabase sqldb = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = sqldb.rawQuery("select * from " + TABLE_HASIL_SURVEY_ALASAN_TIDAK , null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "Survey_Alasan_Tidak_" + getDateTimeBulan() +".csv";
            // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile,false);
            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + ",");
                    } else {
                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Log.d("Status :","Exported Successfully.");
            }
        } catch (IOException ex) {
            if (sqldb.isOpen()) {
                sqldb.close();
                Log.e("Failed :", ex.getMessage());
            }
        } finally {
        }
    }
    */

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTimeJam() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTimeTanggal() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTimeBulan() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTimeTahun() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void startSendEmailIntent(Context c){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {
                        "Dessy.Lindasari@id.nestle.com",
                        "Bahrul.Ulum@id.nestle.com",
                        "Angelia-Iskandar.Putri@id.nestle.com",
                        "Mimin.Widiarti@id.nestle.com",
                        "AlinSuryahendraKusuma.Dewi@id.nestle.com"
                });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Kantin Cikupa");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile;
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Berikut adalah data feedback kantin pabrik Cikupa sejauh ini. \n" +
                    "Diambil tanggal : " + getDateTime());
        pathToMyAttachedFile = filename;
        Log.d("Path :",pathToMyAttachedFile);

        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists()) {
            Toast.makeText(c,"File tidak ada",Toast.LENGTH_SHORT).show();
            Log.d("Error :", "File Not Exist");
            return;
        }
        if (!file.canRead()) {
            Toast.makeText(c,"File tidak terbaca",Toast.LENGTH_SHORT).show();
            Log.d("Error :", "File Can't be Read");
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        c.startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

}
