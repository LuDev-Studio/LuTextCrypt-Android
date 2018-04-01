package com.ludevstudio.android.lutextcrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class AesActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edt_eingabe;
    EditText edt_filename_secKey;
    TextView tw_ausgabe;
    Button btn_verschluesseln, btn_entschluesseln;
    Toolbar toolbar;
    RadioButton radio_filename_default, radio_filename_costum;


    Vibrator vibrator;
    Boolean vibrieren;
    int vibrationsdauer;

    File secKeyfile;

    
    File ordner_main, ordner_aes, ordner_keys;



    String text = null;
    byte[] encryptedBytes = null;
    String encrypted = null;
    byte[] enc = null;
    byte[] decryptedBytes = null;
    String decrypted = null;


    SecretKey key = null;
    String algo = "AES";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        
        initKomponenten();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_allactivities, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);
        int itemid = item.getItemId();
        if(itemid==R.id.allactivities_menu_item_clear) {
            clear();
        } else if(itemid==R.id.allactivities_menu_item_copy) {
            copy();
        }  else if(itemid==R.id.allactivities_menu_item_paste) {
            paste();
        } else if(itemid==R.id.allactivities_menu_item_share) {
            share();
        } else if (itemid==android.R.id.home) {
            finish();
        }

        return true;
    }





    private void initKomponenten() {
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe);
        edt_filename_secKey = (EditText) findViewById(R.id.editText_eingabe_seckey);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe);
        radio_filename_default = (RadioButton) findViewById(R.id.checkbox_defaultfilename);
        radio_filename_costum = (RadioButton) findViewById(R.id.checkbox_costumfilename);
        radio_filename_default.setChecked(true);
        radio_filename_default.setOnClickListener(this);
        radio_filename_costum.setOnClickListener(this);
        btn_verschluesseln = (Button) findViewById(R.id.button_verschluesseln);
        btn_entschluesseln = (Button) findViewById(R.id.button_entschluesseln);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

        ordner_main = new File(Environment.getExternalStorageDirectory(), "LuTextCrypt");
        ordner_keys = new File(ordner_main, "Keys");
        ordner_aes = new File(ordner_keys, algo);
        secKeyfile = new File(ordner_aes, "secret.key");
          }

    @Override
    public void onClick(View v) {
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);

        int id = v.getId();
        if(id==R.id.checkbox_defaultfilename){
            radio_filename_default.setChecked(true);
            radio_filename_costum.setChecked(false);
            edt_filename_secKey.setEnabled(false);
            secKeyfile = new File(ordner_aes, "secret.key");
           

        } else if(id==R.id.checkbox_costumfilename) {
            radio_filename_default.setChecked(false);
            radio_filename_costum.setChecked(true);
            edt_filename_secKey.setEnabled(true);
            secKeyfile = new File(ordner_aes, edt_filename_secKey.getText().toString() + ".key");
        } else if (id == R.id.button_verschluesseln) {
            key = setKey();

            if(key!=null) {
                text = edt_eingabe.getText().toString();
                encryptedBytes = verschluesseln(text.getBytes());
                encrypted = android.util.Base64.encodeToString(encryptedBytes, 0);
                tw_ausgabe.setText(encrypted);
            } else {
                Toast.makeText(this, "Secretkey File not found", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.button_entschluesseln) {
            key = setKey();

            if(key!=null) {
                encrypted = edt_eingabe.getText().toString();
                enc = android.util.Base64.decode(encrypted, 0);
                decryptedBytes = entschluesseln(enc);
                decrypted = new String(decryptedBytes);
                tw_ausgabe.setText(decrypted);
            } else {
                Toast.makeText(this, "Secretkey File not found", Toast.LENGTH_SHORT).show();
            }

        }


    }






    private SecretKey setKey() {
        byte[] keyBytes = new byte[(int) secKeyfile.length()];
        try {
            BufferedInputStream bufsecKey = new BufferedInputStream(new FileInputStream(secKeyfile));
            bufsecKey.read(keyBytes, 0, keyBytes.length);
            bufsecKey.close();


            return new SecretKeySpec(keyBytes, 0, keyBytes.length, algo);
        } catch (Exception e) {
        }

        return null;
    }


    private byte[] verschluesseln(byte[] textBytes) {
        try {
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(textBytes);
        } catch (Exception e) {}
        return "Error while encrypting".getBytes();
    }


   


    private byte[] entschluesseln(byte[] textBytes) {

        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(algo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            textBytes = cipher.doFinal(textBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        return textBytes;
    }


    public void copy() {
        Toast.makeText(getApplicationContext(), getString(R.string.allactivities_menu_item_copy), Toast.LENGTH_SHORT).show();
        String text = tw_ausgabe.getText().toString();
        android.content.ClipboardManager clipboard = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clipDate = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clipDate);

    }

    public void paste() {
        Toast.makeText(getApplicationContext(), getString(R.string.allactivities_menu_item_paste), Toast.LENGTH_SHORT).show();
        android.content.ClipboardManager clipboard = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ContentResolver contentResolver = this.getContentResolver();
        ClipData clipData = clipboard.getPrimaryClip();
        if(clipData !=null) {
            ClipData.Item item = clipData.getItemAt(0);
            CharSequence text = (String) item.getText();
            edt_eingabe.setText(text);
        }
    }

    public void clear() {
        Toast.makeText(getApplicationContext(), getString(R.string.allactivities_menu_item_clear), Toast.LENGTH_SHORT).show();
        edt_eingabe.setText(null);
        tw_ausgabe.setText(null);
    }

    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tw_ausgabe.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }















}
