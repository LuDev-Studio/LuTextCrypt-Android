package com.ludevstudio.android.lutextcrypt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
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
import java.security.Security;

import android.os.Vibrator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lutextcrypt.java.luca.com.lutextcrypt.R;


public class DesKeyGeneratorActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    private RadioButton radio_64bits;

    private RadioButton radio_defaultfile;
    private RadioButton radio_costumfile;
    private EditText edt_secretKey;

    private Button btn_generieren;

    private ProgressDialog progBuilder;

    Boolean vibrieren;
    int vibrationsdauer;

    // Variablen für generation
    int keylenght = 64;
    String algo = "DES";

    SecretKey key;

    String fileName_secKey = "secret.key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des_key_generator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initKomponenten();

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }
    Vibrator vibrator;

    private void initKomponenten() {
        edt_secretKey = (EditText) findViewById(R.id.editText_key);
        edt_secretKey.setEnabled(false);


        radio_64bits = (RadioButton) findViewById(R.id.checkBox_64bits);
        radio_64bits.setOnClickListener(this);
        radio_64bits.setChecked(true);

        radio_defaultfile = (RadioButton) findViewById(R.id.checkbox_defaultfilename);
        radio_costumfile = (RadioButton) findViewById(R.id.checkbox_costumfilename);
        radio_defaultfile.setOnClickListener(this);
        radio_costumfile.setOnClickListener(this);

        btn_generieren = (Button) findViewById(R.id.button_generate);
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

        if(id==R.id.checkBox_64bits) {
            keylenght = 64;
        } else if(id==R.id.checkbox_defaultfilename){
            radio_costumfile.setChecked(false);
            edt_secretKey.setEnabled(false);
            fileName_secKey = "secret.key";
        } else if (id==R.id.checkbox_costumfilename) {
            radio_defaultfile.setChecked(false);
            edt_secretKey.setEnabled(true);
            fileName_secKey = edt_secretKey.getText().toString() + ".key";
        }else if (id==android.R.id.home) {
            finish();
        } else if(id==R.id.button_generate) {
            if(radio_costumfile.isChecked()) {
                if(edt_secretKey.getText().toString().isEmpty() || edt_secretKey.getText().toString().contains(" ") ){
                    Toast.makeText(getApplicationContext(), "Filenames can not be null or contain spaces", Toast.LENGTH_SHORT).show();
                } else {

                    gen();
                    save();
                    Toast.makeText(this, "Key generated", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Key generated", Toast.LENGTH_SHORT).show();
                gen();
                save();
            }
        }
    }

    private void gen() {
        KeyGenerator keyGen = null;

        try {

            keyGen = KeyGenerator.getInstance(algo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(keylenght);
        key = keyGen.generateKey();
    }

    private void save() {
        File ordner_main = new File(Environment.getExternalStorageDirectory(), "LuTextCrypt");
        File ordner_keys = new File(ordner_main, "Keys");
        File ordner_DES = new File(ordner_keys, algo);
        File secKey = new File(ordner_DES, fileName_secKey);
        if(!ordner_main.exists())
            ordner_main.mkdirs();
        if(!ordner_keys.exists())
            ordner_keys.mkdirs();
        if(!ordner_DES.exists())
            ordner_DES.mkdirs();


        try {
            OutputStream fosSecKey = new FileOutputStream(secKey);
            fosSecKey.write(key.getEncoded());
            fosSecKey.close();
        } catch (Exception e) {
            // Fehler
        }
        progBuilder.cancel();
    }

}
