package rpro.com.surveykantinnestle;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HalamanAwalV2 extends Fragment {

    final String KEY_PIN_SEND_REPORT = "1234";

    public HalamanAwalV2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_halaman_awal_v2, container, false);
        final MainActivity act = (MainActivity) getActivity();

        ViewFlipper vp = (ViewFlipper) view.findViewById(R.id.viewflipper_halamanAwalV2);
        vp.setAutoStart(true);
        vp.setFlipInterval(3000);

        view.findViewById(R.id.button_advanced_halamanAwalV2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Advanced Menu");
                builder.setMessage("Masukan PIN :");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals(KEY_PIN_SEND_REPORT)) {
                            try {
                                /*
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                builder2.setTitle("Send/Save Report");
                                builder2.setMessage("Pilih Bulan :");

                                final Spinner spinner = new Spinner(getContext());
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                                        R.array.Bulan, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                builder2.setView(spinner);

                                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        exportOperation exportOp = new exportOperation();
                                        exportOp.execute(act.dbHelper,spinner.getSelectedItemPosition());
                                    }
                                });

                                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder2.show();
                                */

                                AlertDialog.Builder popupMenu = new AlertDialog.Builder(getContext());
                                popupMenu.setTitle("Advanced Menu :");
                                popupMenu.setMessage("Silahkan Pilih Perintah");

                                LinearLayout linLayout = new LinearLayout(getContext());
                                linLayout.setOrientation(LinearLayout.VERTICAL);
                                ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                Button send = new Button(getContext());
                                send.setText("Kirim Report");
                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        exportOperation exportOp = new exportOperation();
                                        exportOp.execute(act.dbHelper);
                                    }
                                });

                                Button truncate = new Button(getContext());
                                truncate.setText("Hapus Semua Record");
                                truncate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder truncateMenu = new AlertDialog.Builder(getContext());
                                        truncateMenu.setTitle("TRUNCATE DATA");
                                        truncateMenu.setMessage("PERHATIAN : DATA YANG SUDAH DIHAPUS TIDAK DAPAT DIKEMBALIKAN");
                                        truncateMenu.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                act.dbHelper.truncateRespond(getContext());
                                            }
                                        });

                                        truncateMenu.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        truncateMenu.show();
                                    }
                                });

                                linLayout.setLayoutParams(linLayoutParam);
                                linLayout.addView(send);
                                linLayout.addView(truncate);
                                popupMenu.setView(linLayout);

                                popupMenu.setNegativeButton("Selesai", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                popupMenu.show();

                            } catch(Exception ex){
                                ex.printStackTrace();
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(),"PIN Salah",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanPertanyaan()).commit();
            }
        });

        return view;
    }

    private class exportOperation extends AsyncTask<Object,Void,Void> {

        ProgressDialog loading;
        DBHelper helper;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Please Wait...",null,true,true);
        }

        @Override
        protected Void doInBackground(Object... o) {
            helper = (DBHelper) o[0];
            try {
                helper.exportTableSurveyToCSV();
                helper.startSendEmailIntent(getContext());
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            loading.dismiss();
        }

    }

}
