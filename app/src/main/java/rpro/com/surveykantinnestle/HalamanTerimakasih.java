package rpro.com.surveykantinnestle;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rpro.com.surveykantinnestle.R;

public class HalamanTerimakasih extends Fragment {



    public HalamanTerimakasih() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_terimakasih, container, false);

       final CountDownTimer cd = new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanAwalV2(),"FRAG_AWAL").commit();
            }
        }.start();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanAwalV2(),"FRAG_AWAL").commit();
            }
        });

        return view;

    }

}
