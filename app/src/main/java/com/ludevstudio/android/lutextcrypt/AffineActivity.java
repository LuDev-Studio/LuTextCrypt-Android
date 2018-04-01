package com.ludevstudio.android.lutextcrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class AffineActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    EditText edt_eingabe;
    EditText edt_wertA;
    EditText edt_wertB;
    Button btn_entschluesseln;
    Button btn_verschluesseln;
    TextView tw_ausgabe;
    String text = "";
    static int firstKey = 0;
   static int secondKey = 0;
    static int module = 26;

    Vibrator vibrator;
    Boolean vibrieren;
    int vibrationsdauer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affine);
        toolbar = (Toolbar) findViewById(R.id.toolbar_affine);
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
       int itemid = item.getItemId();
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);

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
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_affine);
        edt_wertA = (EditText) findViewById(R.id.editText_wertA_affine);
        edt_wertB = (EditText) findViewById(R.id.editText_wertB_affine);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_affine);
        btn_verschluesseln = (Button) findViewById(R.id.button_versschluesseln_affine);
        btn_entschluesseln = (Button) findViewById(R.id.button_entschluesseln_affine);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");
    }


    @Override
    public void onClick(View v) {
       if(vibrieren)
           vibrator.vibrate(vibrationsdauer);
        if(v.getId()==R.id.button_versschluesseln_affine){
            try {
                firstKey = Integer.parseInt(edt_wertA.getText().toString());
                secondKey = Integer.parseInt(edt_wertB.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
            }
            text = edt_eingabe.getText().toString().toUpperCase();

            if(text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
                } else if(firstKey%2==0 || firstKey>=26) {
                    Toast.makeText(getApplicationContext(), "Only allowed for value A: 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23 or 25.", Toast.LENGTH_LONG).show();
                } else if (secondKey > 25) {
                Toast.makeText(getApplicationContext(), "Value B have to be lower than 26.", Toast.LENGTH_LONG).show();
            } else if(secondKey==0) {
                Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
                } else {
                tw_ausgabe.setText(encrypt(text));

            }




        } else if(v.getId()==R.id.button_entschluesseln_affine) {
            try {
                firstKey = Integer.parseInt(edt_wertA.getText().toString());
                secondKey = Integer.parseInt(edt_wertB.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
            }
            text = edt_eingabe.getText().toString().toUpperCase();

            if(text.isEmpty()) {
                Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
            } else if(firstKey%2==0 || firstKey>=26) {
                Toast.makeText(getApplicationContext(), "Only allowed 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23 or 25 for Value A.", Toast.LENGTH_LONG).show();
            } else if (secondKey > 25) {
                Toast.makeText(getApplicationContext(), "Value B have to be lower than 26.", Toast.LENGTH_LONG).show();
            } else if(secondKey==0) {
                Toast.makeText(getApplicationContext(), "You have to type in somethink to all fields", Toast.LENGTH_LONG).show();
            } else {
                tw_ausgabe.setText(decrypt(text));

            }
        }

    }

    static String encrypt(String input) {
        StringBuilder builder = new StringBuilder();
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            if (Character.isLetter(character)) {
                character = (char) ((firstKey * (character - 'A') + secondKey) % module + 'A');
            }
            builder.append(character);
        }
        return builder.toString();
    }

    static String decrypt(String input) {
        StringBuilder builder = new StringBuilder();
        // compute firstKey^-1 aka "modular inverse"
        BigInteger inverse = BigInteger.valueOf(firstKey).modInverse(BigInteger.valueOf(module));
        // perform actual decryption
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            if (Character.isLetter(character)) {
                int decoded = inverse.intValue() * (character - 'A' - secondKey + module);
                character = (char) (decoded % module + 'A');
            }
            builder.append(character);
        }
        return builder.toString();
    }



    // Menümethoden:

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
        edt_wertA.setText(null);
        edt_wertB.setText(null);
    }

    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tw_ausgabe.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}
