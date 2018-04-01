package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivityTabWerkzeuge extends Fragment  implements  View.OnClickListener {
    View contentView;
    private CardView card1;
    private CardView card2;
    Vibrator vibrator;

    MainActivity mainActivity;
    Boolean vibrieren;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_main_activity_tab_werkzeuge, null);
        return contentView;


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initKomponenten();
    }

    private void initKomponenten() {

        card1 = (CardView) contentView.findViewById(R.id.cardview1_2);
        card1.setOnClickListener(this);
        card2 = (CardView) contentView.findViewById(R.id.cardview2_2);
        card2.setOnClickListener(this);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        mainActivity = (MainActivity)getActivity();
        vibrieren = mainActivity.vibrieren;
    }

    @Override
    public void onClick(View v) {
       if(vibrieren)
        vibrator.vibrate(50);
        if (v.getId() == R.id.cardview1_2) {
            Intent intent = new Intent(getActivity(), TextInAsciActivity.class);
            intent.putExtra("vibrieren", mainActivity.vibrieren);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), Base64Activity.class);
            intent.putExtra("vibrieren", mainActivity.vibrieren);
            startActivity(intent);

        }


    }
}
