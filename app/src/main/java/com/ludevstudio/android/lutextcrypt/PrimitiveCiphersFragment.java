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

public class PrimitiveCiphersFragment extends android.app.Fragment implements View.OnClickListener{
    View contentView2;
    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    private CardView card5;
    private CardView card6;
    Vibrator vibrator;

    int vibrationsdauer;
    Boolean vibrieren;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView2 = inflater.inflate(R.layout.fragment_primitive_ciphers, null);
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
        card5 = (CardView) contentView2.findViewById(R.id.cardview5);
        card5.setOnClickListener(this);


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
        if (v.getId() == R.id.cardview1) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), Rot13Activity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        } else if(v.getId() == R.id.cardview2) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), CaesarActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        } else if (v.getId() == R.id.cardview3) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), VigenereActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        } else if(v.getId()==R.id.cardview4) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), AffineActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        } else if(v.getId()==R.id.cardview5) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), AtbashActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        } else if(v.getId()==R.id.cardview6) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), RsaActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        }




    }
}
