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

public class SymetricCiphersFragment extends android.app.Fragment implements View.OnClickListener{
    View contentView2;
    private CardView cardAes;

    Vibrator vibrator;

    int vibrationsdauer;
    Boolean vibrieren;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView2 = inflater.inflate(R.layout.fragment_symetric_ciphers, null);
        return contentView2;



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initKomponenten();
    }

    private void initKomponenten() {
        cardAes = (CardView) contentView2.findViewById(R.id.card_aes);
        cardAes.setOnClickListener(this);

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
        if (v.getId() == R.id.card_aes) {
            mainActivity.mInterstitialAd.show();
            mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Intent intent = new Intent(getActivity(), AesActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        }




    }
}
