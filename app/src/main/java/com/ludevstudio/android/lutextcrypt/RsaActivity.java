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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class RsaActivity extends AppCompatActivity implements View.OnClickListener{
        EditText edt_eingabe;
        EditText edt_filename_pubKey;
        EditText edt_filename_priKey;
        TextView tw_ausgabe;
        Button btn_verschluesseln, btn_entschluesseln;
        Toolbar toolbar;
        RadioButton radio_filename_default, radio_filename_costum;


        Vibrator vibrator;
        Boolean vibrieren;
        int vibrationsdauer;

        File pubKeyfile, priKeyfile;
        PublicKey pubKey = null;
        PrivateKey priKey = null;
        File ordner_main, ordner_keys, ordner_rsa;

        SecretKey keyAes = null;

        String text = null;
        byte[] encryptedBytes = null;
        String encrypted = null;
        byte[] enc = null;
        byte[] decryptedBytes = null;
        String decrypted = null;
        
        String algo = "RSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);
            toolbar = (Toolbar) findViewById(R.id.toolbar_rsa);
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_rsa);
        edt_filename_pubKey = (EditText) findViewById(R.id.editText_eingabe_pubkey_rsa);
        edt_filename_priKey = (EditText) findViewById(R.id.editText_eingabe_prikey_rsa);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_rsa);
        radio_filename_default = (RadioButton) findViewById(R.id.checkbox_defaultfilename_rsa);
        radio_filename_costum = (RadioButton) findViewById(R.id.checkbox_costumfilename_rsa);
        radio_filename_default.setChecked(true);
        radio_filename_default.setOnClickListener(this);
        radio_filename_costum.setOnClickListener(this);
        btn_verschluesseln = (Button) findViewById(R.id.button_verschluesseln_rsa);
        btn_entschluesseln = (Button) findViewById(R.id.button_entschluesseln_rsa);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

        ordner_main = new File(Environment.getExternalStorageDirectory(), "LuTextCrypt");
        ordner_keys = new File(ordner_main, "Keys");
        ordner_rsa = new File(ordner_keys, algo);
        pubKeyfile = new File(ordner_rsa, "public.key");
        priKeyfile = new File(ordner_rsa, "private.key");
    }

    @Override
    public void onClick(View v) {
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);

        int id = v.getId();
        if(id==R.id.checkbox_defaultfilename_rsa){
            radio_filename_default.setChecked(true);
            radio_filename_costum.setChecked(false);
            edt_filename_pubKey.setEnabled(false);
            edt_filename_priKey.setEnabled(false);
            pubKeyfile = new File(ordner_keys, "public.key");
            priKeyfile = new File(ordner_keys, "private.key");

        } else if(id==R.id.checkbox_costumfilename_rsa) {
            radio_filename_default.setChecked(false);
            radio_filename_costum.setChecked(true);
                edt_filename_pubKey.setEnabled(true);
            edt_filename_priKey.setEnabled(true);
            pubKeyfile = new File(ordner_keys, edt_filename_pubKey.getText().toString() + ".key");
            priKeyfile = new File(ordner_keys, edt_filename_priKey.getText().toString() + ".key");
        } else if (id == R.id.button_verschluesseln_rsa) {
            pubKey = setPublicKey();

            if(pubKey!=null) {
                text = edt_eingabe.getText().toString();
                encryptedBytes = verschluesseln(text.getBytes());
                encrypted = android.util.Base64.encodeToString(encryptedBytes, 0);
                tw_ausgabe.setText(encrypted);
            } else {
                Toast.makeText(this, "PublicKeyfile not found", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.button_entschluesseln_rsa) {
           priKey = setPrivateKey();

        if(priKey!=null) {
            encrypted = edt_eingabe.getText().toString();
            enc = android.util.Base64.decode(encrypted, 0);
            decryptedBytes = entschluesseln(enc);
            decrypted = new String(decryptedBytes);
            tw_ausgabe.setText(decrypted);
        } else {
            Toast.makeText(this, "PrivateKeyfile not found", Toast.LENGTH_SHORT).show();
        }

        }


        }






    private PublicKey setPublicKey() {
        byte[] keyBytes = new byte[(int) pubKeyfile.length()];
        try {
            BufferedInputStream bufPubKey = new BufferedInputStream(new FileInputStream(pubKeyfile));
            bufPubKey.read(keyBytes, 0, keyBytes.length);
            bufPubKey.close();

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algo);

            return kf.generatePublic(spec);
        } catch (Exception e) {
        }

        return null;
    }


    private byte[] verschluesseln(byte[] textBytes) {
       try {
           Cipher cipher = Cipher.getInstance(algo);
           cipher.init(Cipher.ENCRYPT_MODE, pubKey);
           return cipher.doFinal(textBytes);
       } catch (Exception e) {}
        return "Error while encrypting".getBytes();
    }


    private PrivateKey setPrivateKey() {
        byte[] keyBytes = null;
        try {
            keyBytes = new byte[(int) priKeyfile.length()];
            BufferedInputStream bufPriKey = new BufferedInputStream(new FileInputStream(priKeyfile));
            bufPriKey.read(keyBytes, 0, keyBytes.length);
            bufPriKey.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algo);

            return kf.generatePrivate(spec);
        } catch (Exception e) {
        }

        return null;

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
            cipher.init(Cipher.DECRYPT_MODE, priKey);
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





    public void gen() {
        KeyPair keyPair = null;
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance(algo);
            keyGen.initialize(1024);
            keyPair = keyGen.generateKeyPair();
        } catch (Exception e) {}
            pubKey = keyPair.getPublic();
            priKey = keyPair.getPrivate();
        }


        private void genAesKey() {
            KeyGenerator keyGen = null;

            try {
                keyGen = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyAes = keyGen.generateKey();

        }

        private byte[] encryptAes(byte[] textBytes) {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, keyAes);
            } catch (Exception e) {

            }
            try {

                textBytes = cipher.doFinal(textBytes);
                return textBytes;
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }




    private byte[] decryptAes(byte[] textBytes) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keyAes);
        } catch (Exception e) {

        }
        try {
           textBytes = cipher.doFinal(textBytes);
            return textBytes;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
