package com.ludevstudio.android.lutextcrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
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

import org.w3c.dom.Text;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class TextInAsciActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText edt_eingabe;
    private TextView tw_ausgabe;
    private Button btn_umwandeln;
    Vibrator vibrator;
    Boolean vibrieren;
    int vibrationsdauer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_asci);
        toolbar = (Toolbar) findViewById(R.id.toolbar_textinasci);
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
        }else if (itemid==android.R.id.home) {
            finish();
        }

        return true;
    }



    private void initKomponenten() {
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_textinasci);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_textinasci);
        btn_umwandeln = (Button) findViewById(R.id.button_umwandeln_textinasci);
        btn_umwandeln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");

    }


    @Override
    public void onClick(View v) {
      if(vibrieren)
       vibrator.vibrate(vibrationsdauer);
        switch (v.getId()) {
            case R.id.button_umwandeln_textinasci: {
               tw_ausgabe.setText(stringToAsci(edt_eingabe.getText().toString()));
            }
        }
    }

    private String stringToAsci(String text) {
        char[] originaltext = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < originaltext.length; i++) {
            sb.append((int)originaltext[i]);
            sb.append(' ');
        }
        return sb.toString();
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
