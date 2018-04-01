package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivityTabVerschluesselungen extends Fragment implements View.OnClickListener{
    View contentView2;
    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    Vibrator vibrator;

    Boolean vibrieren;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView2 = inflater.inflate(R.layout.fragment_main_activity_tab_verschluesselungen, null);
        return contentView2;



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initKomponenten();
    }

    private void initKomponenten() {
        // Karten 1. Seite
        card1 = (CardView) contentView2.findViewById(R.id.cardview1);
        card1.setOnClickListener(this);
        card2 = (CardView) contentView2.findViewById(R.id.cardview2);
        card2.setOnClickListener(this);
        card3 = (CardView) contentView2.findViewById(R.id.cardview3);
        card3.setOnClickListener(this);
        card4 = (CardView) contentView2.findViewById(R.id.cardview4);
        card4.setOnClickListener(this);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        mainActivity = (MainActivity)getActivity();
        vibrieren = mainActivity.vibrieren;

    }


    @Override
    public void onClick(View v) {
        if(vibrieren)
            vibrator.vibrate(50);
        if (v.getId() == R.id.cardview1) {
            Intent intent = new Intent(getActivity(), Rot13Activity.class);
            intent.putExtra("vibrieren", mainActivity.vibrieren);
            startActivity(intent);
        } else if(v.getId() == R.id.cardview2) {
            Intent intent = new Intent(getActivity(), CaesarActivity.class);
            intent.putExtra("vibrieren", mainActivity.vibrieren);
            startActivity(intent);
        } else if (v.getId() == R.id.cardview3) {
            Intent intent = new Intent(getActivity(), VigenereActivity.class);
            intent.putExtra("vibrieren", mainActivity.vibrieren);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.mainActivity_dialog_funktionen_title);
            builder.setMessage(R.string.mainActivity_dialog_funktionen_text);
            builder.show();
        }




    }
}
