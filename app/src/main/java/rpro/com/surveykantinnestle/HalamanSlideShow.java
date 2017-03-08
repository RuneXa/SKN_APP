package rpro.com.surveykantinnestle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;


public class HalamanSlideShow extends Fragment {


    public HalamanSlideShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_halaman_slide_show, container, false);
        ViewFlipper vp = (ViewFlipper) view.findViewById(R.id.viewFlipper_halamanSlideShow);
        vp.setAutoStart(true);
        vp.setFlipInterval(3000);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameContainer_Main, new HalamanAwal()).commit();
            }
        });
        return view;
    }

}
