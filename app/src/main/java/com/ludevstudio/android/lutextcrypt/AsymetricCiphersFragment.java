package com.ludevstudio.android.lutextcrypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class AsymetricCiphersFragment extends android.app.Fragment implements View.OnClickListener{
    View contentView2;
    private CardView card1;

    Vibrator vibrator;

    int vibrationsdauer;
    Boolean vibrieren;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView2 = inflater.inflate(R.layout.fragment_asymetric_ciphers, null);
        return contentView2;



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initKomponenten();
    }

    private void initKomponenten() {
        // Karten 1. Seite
        card1 = (CardView) contentView2.findViewById(R.id.cardview6);
        card1.setOnClickListener(this);


        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        mainActivity = (MainActivity)getActivity();
        vibrieren = mainActivity.vibrieren;
        vibrationsdauer = mainActivity.vibrationsdauer;

    }


    @Override
    public void onClick(View v) {
        Bundle extras = new Bundle();
        extras.putBoolean("vibrieren", mainActivity.vibrieren);
        extras.putInt("vibrationsdauer", mainActivity.vibrationsdauer);
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);
       if(v.getId()==R.id.cardview6) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), RsaActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        }




    }
}
