package com.ludevstudio.android.lutextcrypt;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.util.Output;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import android.os.Vibrator;
import lutextcrypt.java.luca.com.lutextcrypt.R;


public class RsakeygeneratorActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    private RadioButton radio_512bits;
    private RadioButton radio_1024bits;
    private RadioButton radio_2048bits;
    private RadioButton radio_4096bits;
    private RadioButton radio_defaultfile;
    private RadioButton radio_costumfile;
    private EditText edt_pubkeyname;
    private EditText edt_prikeyname;
    private Button btn_generieren;

    private ProgressDialog progBuilder;

    Boolean vibrieren;
    int vibrationsdauer;

    // Variablen für generation
    
    int keylenght = 1024;
    String algo = "RSA";
    
    KeyPair key = null;

    String fileName_pubKey = "public.key";
    String fileName_priKey = "private.key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsakeygenerator);
            toolbar = (Toolbar) findViewById(R.id.toolbar_rsakeygenerator);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initKomponenten();
    }
            Vibrator vibrator;

    private void initKomponenten() {
        edt_pubkeyname = (EditText) findViewById(R.id.editText_eingabe_pubkey_rsakeygenerator);
        edt_prikeyname = (EditText) findViewById(R.id.editText_eingabe_prikey_rsakeygenerator);
        edt_pubkeyname.setEnabled(false);
        edt_prikeyname.setEnabled(false);


        radio_512bits = (RadioButton) findViewById(R.id.checkBox_512bits_rsakeygenerator);
        radio_1024bits = (RadioButton) findViewById(R.id.checkBox_1024bits_rsakeygenerator);
        radio_2048bits = (RadioButton) findViewById(R.id.checkbox_2048bits_rsakeygenerator);
        radio_4096bits = (RadioButton) findViewById(R.id.checkbox_4096bits_rsakeygenerator);

        radio_512bits.setOnClickListener(this);
        radio_1024bits.setOnClickListener(this);
        radio_1024bits.setChecked(true);
        radio_2048bits.setOnClickListener(this);
        radio_4096bits.setOnClickListener(this);

        radio_defaultfile = (RadioButton) findViewById(R.id.checkbox_defaultfilename_rsakeygenerator);
        radio_costumfile = (RadioButton) findViewById(R.id.checkbox_costumfilename_rsakeygenerator);
        radio_defaultfile.setChecked(true);
        radio_defaultfile.setOnClickListener(this);
        radio_costumfile.setOnClickListener(this);

        btn_generieren = (Button) findViewById(R.id.button_generieren_rsakeygenerator);
        btn_generieren.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

        progBuilder = new ProgressDialog(this);
        progBuilder.setCancelable(false);
        progBuilder.setTitle("generating keys");
        progBuilder.setMessage("please wait...");

    }


    @Override
    public void onClick(View v) {
       if(vibrieren)
           vibrator.vibrate(vibrationsdauer);

        int id = v.getId();

        if(id==R.id.checkBox_512bits_rsakeygenerator) {
            radio_1024bits.setChecked(false);
            radio_2048bits.setChecked(false);
            radio_2048bits.setChecked(false);
            keylenght = 512;
        } else if(id==R.id.checkBox_1024bits_rsakeygenerator) {
            radio_512bits.setChecked(false);
            radio_2048bits.setChecked(false);
            radio_2048bits.setChecked(false);
            keylenght = 1024;
        } else if(id==R.id.checkbox_2048bits_rsakeygenerator) {
            radio_1024bits.setChecked(false);
            radio_512bits.setChecked(false);
            radio_4096bits.setChecked(false);
            keylenght = 2048;
        } else if(id==R.id.checkbox_4096bits_rsakeygenerator) {
            radio_1024bits.setChecked(false);
            radio_2048bits.setChecked(false);
            radio_512bits.setChecked(false);
            keylenght = 4096;
        } else if(id==R.id.checkbox_defaultfilename_rsakeygenerator){
           radio_costumfile.setChecked(false);
            edt_pubkeyname.setEnabled(false);
            edt_prikeyname.setEnabled(false);
            fileName_pubKey = "public.key";
            fileName_priKey = "private.key";
        } else if (id==R.id.checkbox_costumfilename_rsakeygenerator) {
           radio_defaultfile.setChecked(false);
            edt_pubkeyname.setEnabled(true);
            edt_prikeyname.setEnabled(true);
            fileName_pubKey = edt_pubkeyname.getText().toString() + ".key";
            fileName_priKey = edt_prikeyname.getText().toString() + ".key";
        }else if (id==android.R.id.home) {
            finish();
        } else if(id==R.id.button_generieren_rsakeygenerator) {
                if(radio_costumfile.isChecked()) {
                        if(edt_pubkeyname.getText().toString().isEmpty() || edt_pubkeyname.getText().toString().contains(" ") || edt_prikeyname.getText().toString().isEmpty() || edt_prikeyname.getText().toString().contains(" ")   ){
                            Toast.makeText(getApplicationContext(), "Filenames can not be null or contain spaces", Toast.LENGTH_SHORT).show();
                        } else {

                            gen();
                            save();
                            Toast.makeText(this, "keys generated", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(this, "keys generated", Toast.LENGTH_SHORT).show();
                    gen();
                    save();
                }
        }
    }

        private void gen() {
            KeyPairGenerator keyGen = null;

            try {
                keyGen = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGen.initialize(keylenght);
            key = keyGen.generateKeyPair();
        }

        private void save() {
            File ordner_main = new File(Environment.getExternalStorageDirectory(), "LuTextCrypt");
            File ordner_keys = new File(ordner_main, "Keys");
            File ordner_rsa = new File(ordner_keys, algo);
            File pubKey = new File(ordner_rsa, fileName_pubKey);
            File priKey = new File(ordner_rsa, fileName_priKey);
                if(!ordner_main.exists())
                    ordner_main.mkdirs();
            if(!ordner_keys.exists())
                ordner_keys.mkdirs();
                 if(!ordner_rsa.exists())
                 ordner_rsa.mkdirs();


            try {
                OutputStream fosPubKey = new FileOutputStream(pubKey);
                FileOutputStream fosPriKey = new FileOutputStream(priKey);
                fosPubKey.write(key.getPublic().getEncoded());
                fosPriKey.write(key.getPrivate().getEncoded());
                fosPubKey.close();
                fosPriKey.close();
            } catch (Exception e) {
                // Fehler
            }
                progBuilder.cancel();
        }

}
