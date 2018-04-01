package com.ludevstudio.android.lutextcrypt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import lutextcrypt.java.luca.com.lutextcrypt.R;

import static android.R.id.home;

public class EinstellungenActivity extends AppCompatActivity implements View.OnClickListener{
        Toolbar toolbar;
        Switch switch_vibrieren,switch_drehen;
        MainActivity mainActivity;
        SharedPreferences prefs;
        SharedPreferences.Editor prefsEditor;
        Button btn_ok_dialog_permissions;
    TextView dauer_short, dauer_veryshort, dauer_normal, dauer_long, dauer_verylong, dauer_extremelong;
    Dialog dialogVibrationsdauer, dialogPermissions, dialogUeberApp;

     Boolean vibrieren;

        TextView tw_vibrationsdauer;
        TextView tw_ueberdieseapp;

        Vibrator vibrator;
        int vibrationsdauer;
    TextView tw_permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);
           mainActivity = new MainActivity();

            initKomponenten();
    }

    private void initKomponenten() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_einstellungen);
        setSupportActionBar(toolbar);

        switch_vibrieren = (Switch) findViewById(R.id.switch_vibration);
        switch_vibrieren.setOnClickListener(this);

        prefs = this.getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        prefsEditor = prefs.edit();
        switch_vibrieren.setChecked(prefs.getBoolean("VIBRIEREN_ONOFF", true));
        vibrieren = true;


        tw_vibrationsdauer = (TextView) findViewById(R.id.button_vibrationsdauer_einstellungen);
        tw_vibrationsdauer.setOnClickListener(this);


        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    public void onClick(View v) {
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);
        switch (v.getId()) {
            case R.id.switch_vibration: {
               if(switch_vibrieren.isChecked()) {
                    prefsEditor.putBoolean("VIBRIEREN_ONOFF", true);
                    prefsEditor.commit();
               }    else {
                   prefsEditor.putBoolean("VIBRIEREN_ONOFF", false);
                   prefsEditor.commit();

               }
                break;


            }case R.id.button_vibrationsdauer_einstellungen: {

                 dialogVibrationsdauer = new Dialog(EinstellungenActivity.this);
               dialogVibrationsdauer.setContentView(R.layout.dialog_einstellungen_vibrationsdauer);
                    dauer_veryshort = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_veryshort_einstellungen);
                    dauer_short = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_short_einstellungen);
                    dauer_normal = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_normal_einstellungen);
                    dauer_long = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_long_einstellungen);
                    dauer_verylong = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_verylong_einstellungen);
                    dauer_extremelong = (TextView) dialogVibrationsdauer.findViewById(R.id.textView_duration_extremelong_einstellungen);
                    dauer_veryshort.setOnClickListener(this);
                    dauer_short.setOnClickListener(this);
                    dauer_normal.setOnClickListener(this);
                    dauer_long.setOnClickListener(this);
                    dauer_verylong.setOnClickListener(this);
                    dauer_extremelong.setOnClickListener(this);



                        dialogVibrationsdauer.show();
                    }





                break;




        }



        int id=v.getId();
        if(id==R.id.textView_duration_veryshort_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 30);
            prefsEditor.commit();
            vibrator.vibrate(30);
            dialogVibrationsdauer.cancel();
        } else  if(id==R.id.textView_duration_short_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 60);
            prefsEditor.commit();
            vibrator.vibrate(60);
            dialogVibrationsdauer.cancel();
        }  else  if(id==R.id.textView_duration_normal_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 90);
            prefsEditor.commit();
            vibrator.vibrate(90);
            dialogVibrationsdauer.cancel();
        } else  if(id==R.id.textView_duration_long_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 130);
            prefsEditor.commit();
            vibrator.vibrate(130);
            dialogVibrationsdauer.cancel();
        } else  if(id==R.id.textView_duration_verylong_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 200);
            prefsEditor.commit();
            vibrator.vibrate(200);
            dialogVibrationsdauer.cancel();
        } else  if(id==R.id.textView_duration_extremelong_einstellungen) {
            prefsEditor.putInt("VIBRIEREN_DAUER", 1000);
            prefsEditor.commit();
            vibrator.vibrate(1000);
            dialogVibrationsdauer.cancel();
        } else if(id==R.id.button_dialo_permissions_einstellungen) {
            dialogPermissions.cancel();
        }



    }
}
