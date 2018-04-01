package com.ludevstudio.android.lutextcrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class Rot13Activity extends AppCompatActivity implements OnClickListener{
    private Button button_verschluesseln;
    private Button button_entschluesseln;
    private EditText edt_eingabe;
    private TextView tw_ausgabe;
    Toolbar toolbar;

    Boolean vibrieren;
    int vibrationsdauer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rot13);
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



    Vibrator vibrator;
    private void initKomponenten() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_rot13);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        edt_eingabe = (EditText) findViewById(R.id.editText);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausg);
        button_verschluesseln = (Button) findViewById(R.id.button_vers);
        button_verschluesseln.setOnClickListener(this);
        button_entschluesseln = (Button) findViewById(R.id.button_ents);
        button_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


            Bundle extras = getIntent().getBundleExtra("extras");
            vibrieren = extras.getBoolean("vibrieren");
            vibrationsdauer = extras.getInt("vibrationsdauer");

            }

    @Override
    public void onClick(View v) {
       if(vibrieren)
        vibrator.vibrate(vibrationsdauer);
        if (v.getId() == R.id.button_vers) {
            tw_ausgabe.setText(encryptString(edt_eingabe.getText().toString(), 13));
        } else if (v.getId() == R.id.button_ents) {
            tw_ausgabe.setText(decryptString(edt_eingabe.getText().toString(), 13));
        }
    }


    // Cäsar Verschlüsselung mit ASCI-Zeichensatz
    public static String encryptString(String text, int offset) {
        String ret = "";
        for (int i = 0; i < text.length(); i++) {
            ret += encrypt(text.charAt(i), offset);
        }
        return ret;
    }




    public static char encrypt(char c, int offset) {

        return (char)((int)c+offset%128);
    }

// Cäsar Entschhlüsselung

    public static String decryptString(String text, int offset) {
        String ret = "";
        for (int i = 0; i < text.length(); i++) {
            ret += decrypt(text.charAt(i), offset);
        }
        return ret;
    }

    public static char decrypt(char c, int offset) {

        return (char)((int)c-offset%128);
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
    }

    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tw_ausgabe.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }




}
