package rpro.com.surveykantinnestle;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class HalamanPertanyaan extends Fragment {

    public HalamanPertanyaan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_halaman_pertanyaan, container, false);

        final MainActivity act = (MainActivity)getActivity();

        final CountDownTimer cd = new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanAwalV2(),"FRAG_AWAL").commit();
                Toast.makeText(getContext(),"Waktu Habis",Toast.LENGTH_SHORT).show();
            }
        };

        view.findViewById(R.id.imgV_puas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                act.dbHelper.insertRespond("ya","-"); //,null);
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main,new HalamanTerimakasih(),"FRAG_TERIMAKASIH").addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.imgV_cukup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                act.dbHelper.insertRespond("cukup","-"); //,null);
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main,new HalamanTerimakasih(),"FRAG_TERIMAKASIH").addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.imgV_kurang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main,new HalamanAlasanTidak()).commit();
            }
        });

        view.findViewById(R.id.button_back_halamanPertanyaan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main,new HalamanAwalV2(),"FRAG_AWAL").addToBackStack(null).commit();
            }
        });


        cd.start();
        return view;
    }

}
