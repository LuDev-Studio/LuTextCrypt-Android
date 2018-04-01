package com.ludevstudio.android.lutextcrypt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;
import org.w3c.dom.Text;

import lutextcrypt.java.luca.com.lutextcrypt.R;

public class PasswortgeneratorActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edt_laenge;
    private Button btn_generieren;
    private TextView tw_ausgabe;
    private CheckBox check_kleinbuchstaben;
    private CheckBox check_großbuchstaben;
    private CheckBox check_zahlen;
    private CheckBox check_sonderzeichen;
    Vibrator vibrator;
    Boolean vibrieren;
    int vibrationsdauer;
    int länge;
    private MenuItem paste;
    private MenuItem share;
    private ProgressDialog pd_generieren;
    public Button btn_dialog_ok;
    public Button btn_dialo_cancel;
    Dialog db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwortgenerator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_passwortgenerator);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initKompoenten();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_allactivities, menu);
        paste = (MenuItem) menu.findItem(R.id.allactivities_menu_item_paste);
        paste.setEnabled(false);

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
        } else if(itemid==R.id.allactivities_menu_item_share) {
            share();
        } else if (itemid==android.R.id.home) {
            finish();
        }

        return true;
    }


    private void initKompoenten() {
        edt_laenge = (EditText) findViewById(R.id.editText_eingabe_passwortgenerator);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_passwortgenerator);
        check_kleinbuchstaben = (CheckBox) findViewById(R.id.checkBox_kleinbuchstaben_passwortgenerator);
        check_großbuchstaben = (CheckBox) findViewById(R.id.checkBox_großbuchstaben_passwortgenerator);
        check_zahlen = (CheckBox) findViewById(R.id.checkBox_zahlen_passwortgenerator);
        check_sonderzeichen = (CheckBox) findViewById(R.id.checkbox_sonderzeichen_passwortgenerator);
        btn_generieren = (Button) findViewById(R.id.button_generieren_passwortgenerator);
        btn_generieren.setOnClickListener(this);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
      Bundle extras = getIntent().getBundleExtra("extras");
        vibrieren = extras.getBoolean("vibrieren");
        vibrationsdauer = extras.getInt("vibrationsdauer");




    }


    @Override
    public void onClick(View v) {
        if (vibrieren)
            vibrator.vibrate(vibrationsdauer);
        if (v.getId() == R.id.button_generieren_passwortgenerator) {
            länge = 0;
            try {
                länge = Integer.parseInt(edt_laenge.getText().toString());

            } catch (Exception e) {

            }
            if (länge >= 100000) {
              db = new Dialog(PasswortgeneratorActivity.this);

                db.setContentView(R.layout.dialog_passwortgenerator_laenge);

                db.show();
                btn_dialog_ok = (Button) db.findViewById(R.id.button_dialo_ok_passwortenerator);
                btn_dialog_ok.setOnClickListener(this);
                btn_dialo_cancel = (Button) db.findViewById(R.id.button_dialo_cancel_passwortenerator);
                btn_dialo_cancel.setOnClickListener(this);

            } else {
                generierePasswort();
            }

        } else if (v.getId()==R.id.button_dialo_ok_passwortenerator) {
            db.cancel();
            generierePasswort();
        } else if (v.getId()==R.id.button_dialo_cancel_passwortenerator) {
            db.cancel();
        }
    }

    public void copy() {
        Toast.makeText(getApplicationContext(), getString(R.string.allactivities_menu_item_copy), Toast.LENGTH_SHORT).show();
        String text = tw_ausgabe.getText().toString();
        android.content.ClipboardManager clipboard = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clipDate = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clipDate);

    }




    public void clear() {
        Toast.makeText(getApplicationContext(), getString(R.string.allactivities_menu_item_clear), Toast.LENGTH_SHORT).show();
        edt_laenge.setText(null);
        tw_ausgabe.setText(null);
    }



    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tw_ausgabe.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void generierePasswort() {

        Boolean kleinbuchstabenChecked = check_kleinbuchstaben.isChecked();
        Boolean großbuchstabenChecked = check_großbuchstaben.isChecked();
        Boolean zahlenChecked = check_zahlen.isChecked();
        Boolean sonderzeichenChecked = check_sonderzeichen.isChecked();

        //Zeichensätze
        char[] kleinbuchstaben = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o',	'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; //26
        char[] großbuchstaben = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O',	'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; //26
        char[] zahlen = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};                         //10
        char[] sonderzeichen = {'ä', 'Ä', 'ö', 'Ö', 'ü', 'Ü', 'ß', '!', '?', '@', '(', ')', '<', '>', '-', '/', '%', '#', '*', '+', '&'}; //20

        Random zufall = new Random();






        char[] passwort = new char[länge];

        StringBuilder sb = new StringBuilder();


        if (kleinbuchstabenChecked==false && großbuchstabenChecked==false && zahlenChecked==false && sonderzeichenChecked==false) {
            Toast.makeText(getApplicationContext(), "Sie müssen mindestens eine Checkbox auswählen!", Toast.LENGTH_LONG).show();
        } else if(länge==0) {
            Toast.makeText(getApplicationContext(), "Die Länge darf nicht Null sein", Toast.LENGTH_LONG).show();
        } else if (kleinbuchstabenChecked==false && großbuchstabenChecked==false && zahlenChecked==false && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                passwort[i] = sonderzeichen[zufall.nextInt(21)];
                sb.append(passwort[i]);
            }
            tw_ausgabe.setText(sb.toString());






        } else if (kleinbuchstabenChecked==false && großbuchstabenChecked==false && zahlenChecked==true && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                passwort[i] = zahlen[zufall.nextInt(10)];
                sb.append(passwort[i]);
            }
            tw_ausgabe.setText(sb.toString());









        } else if(kleinbuchstabenChecked==false && großbuchstabenChecked==false && zahlenChecked==true && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(2);
                if(wahl==0) {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());






        } else if (kleinbuchstabenChecked==false && großbuchstabenChecked==true && zahlenChecked==false && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                passwort[i] = großbuchstaben[zufall.nextInt(26)];
                sb.append(passwort[i]);
            }
            tw_ausgabe.setText(sb.toString());





        } else if(kleinbuchstabenChecked==false && großbuchstabenChecked==true && zahlenChecked==true && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(2);
                if(wahl==0) {
                    passwort[i] = großbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());





        } else if(kleinbuchstabenChecked==false && großbuchstabenChecked==true && zahlenChecked==true && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(3);
                if(wahl==0) {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                } else if(wahl==1){
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = großbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());





        } else if (kleinbuchstabenChecked==true && großbuchstabenChecked==false && zahlenChecked==false && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                sb.append(passwort[i]);
            }
            tw_ausgabe.setText(sb.toString());





        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==false && zahlenChecked==false && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(2);
                if(wahl==0) {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());







        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==false && zahlenChecked==true && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(2);
                if(wahl==0) {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());






        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==false && zahlenChecked==true && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(3);
                if(wahl==0) {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                } else if(wahl==1){
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());






        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==true && zahlenChecked==false && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(2);
                if(wahl==0) {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = großbuchstaben[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());





        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==true && zahlenChecked==false && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(3);
                if(wahl==0) {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                } else if(wahl==1){
                    passwort[i] = großbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());








        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==true && zahlenChecked==true && sonderzeichenChecked==false) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(3);
                if(wahl==0) {
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else if(wahl==1){
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = großbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());



        } else if(kleinbuchstabenChecked==true && großbuchstabenChecked==true && zahlenChecked==true && sonderzeichenChecked==true) {
            for(int i = 0; i<länge; i++) {
                int wahl = zufall.nextInt(4);
                if(wahl==0) {
                    passwort[i] = sonderzeichen[zufall.nextInt(20)];
                    sb.append(passwort[i]);
                } else if(wahl==1){
                    passwort[i] = zahlen[zufall.nextInt(10)];
                    sb.append(passwort[i]);
                } else if (wahl==2){
                    passwort[i] = kleinbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                } else {
                    passwort[i] = großbuchstaben[zufall.nextInt(26)];
                    sb.append(passwort[i]);
                }
            }
            tw_ausgabe.setText(sb.toString());








        } else
            Toast.makeText(getApplicationContext(), "Es sit ein Fehler aufgetreten", Toast.LENGTH_LONG).show();



    }


}
