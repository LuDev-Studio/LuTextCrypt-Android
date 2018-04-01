package com.ludevstudio.android.lutextcrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.StandardCharsets;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class Base32Activity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText edt_eingabe;
    TextView tw_ausgabe;
    Button btn_verschluesseln;
    Button btn_entschluesseln;
    Vibrator vibrator;
    Boolean vibrieren;
    int vibrationsdauer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base32);
        toolbar = (Toolbar) findViewById(R.id.toolbar_base32);
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
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_base32);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_base32);
        btn_verschluesseln = (Button) findViewById(R.id.button_vers_base32);
        btn_entschluesseln = (Button) findViewById(R.id.button_ents_base32);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrieren = getIntent().getExtras().getBoolean("vibrieren");

        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

    }

    @Override
    public void onClick(View v) {
        if(vibrieren)
            vibrator.vibrate(vibrationsdauer);
        if(v.getId() == R.id.button_vers_base32) {
            byte[] bytes = edt_eingabe.getText().toString().getBytes();
            Base32 base32 = new Base32();
            tw_ausgabe.setText(base32.encodeAsString(bytes));

        } else {
            Base32 base32 = new Base32();
            byte[] encoded = edt_eingabe.getText().toString().getBytes();
            byte[] decoded = base32.decode(encoded);
            tw_ausgabe.setText(new String(decoded));

        }
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
