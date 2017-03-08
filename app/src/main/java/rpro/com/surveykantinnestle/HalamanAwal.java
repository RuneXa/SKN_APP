package rpro.com.surveykantinnestle;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class HalamanAwal extends Fragment {
/*
    final String KEY_PIN_SEND_REPORT = "1234";

    public HalamanAwal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_halaman_awal, container, false);

        final CountDownTimer cd = new CountDownTimer(20000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanSlideShow()).commit();
            }
        };


        view.findViewById(R.id.button_mulai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanPertanyaan()).addToBackStack(null).commit();
            }
        });


        view.findViewById(R.id.button_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Send/Save Report");
                builder.setMessage("Masukan PIN :");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals(KEY_PIN_SEND_REPORT)) {
                            MainActivity act = (MainActivity) getActivity();
                            try {
                                act.dbHelper.createCountTable();
                                act.dbHelper.exportTableSurveyToCSV();
                                act.dbHelper.exportTableHasilSurveyToCSV();
                                act.dbHelper.exportTableSurveyAlasanTidakToCSV();
                            } catch(Exception ex){
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getContext(),"Selesai",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"PIN Salah",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        cd.start();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        cd.start();
                    }
                });

                builder.show();

            }
        });

        cd.start();
        return view;
    }
*/
}
