package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class VigenereActivity extends AppCompatActivity implements View.OnClickListener {
   private Toolbar toolbar;
    String text;
    String schluessel;
    private EditText Edittext_eingabe;
    private TextView tw_ausgabe;
    private EditText Edittext_schluessel;
    private Button btn_generieren;
    private Button btn_verschluessen;
    private Button btn_entschluesseln;
    private Button btn_asci;
    Vibrator vibrator;

    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vigenere);
        toolbar = (Toolbar) findViewById(R.id.toolbar_vigenere);
        setSupportActionBar(toolbar);
        initKomponenten();
    }

   private void initKomponenten() {
        Edittext_eingabe = (EditText) findViewById(R.id.edittext_input);
        Edittext_schluessel = (EditText) findViewById(R.id.Edittext_key);
        tw_ausgabe = (TextView) findViewById(R.id.textView5);
        btn_asci = (Button) findViewById(R.id.button9);
       btn_asci.setOnClickListener(this);

        btn_generieren = (Button) findViewById(R.id.button6);
        btn_generieren.setOnClickListener(this);
       btn_verschluessen = (Button) findViewById(R.id.button7);
       btn_verschluessen.setOnClickListener(this);
       btn_entschluesseln = (Button) findViewById(R.id.button8);
       btn_entschluesseln.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

       vibrieren = getIntent().getExtras().getBoolean("vibrieren");

   }


    @Override
    public void onClick(View v) {
       if(vibrieren)
        vibrator.vibrate(50);
        if(v.getId() == R.id.button6) {
        Edittext_schluessel.setText(zufallsGen());
    } else if(v.getId() == R.id.button7) {
        text = Edittext_eingabe.getText().toString();
        char[] plain = text.toCharArray();
        schluessel = Edittext_schluessel.getText().toString();
        char[] key = schluessel.toCharArray();
        int richtung = 1;
       String crypted = new String(crypt(plain, key, richtung));
        tw_ausgabe.setText(crypted);
    }   else if (v.getId() == R.id.button8){
        text = Edittext_eingabe.getText().toString();
        char[] plain = text.toCharArray();
        schluessel = Edittext_schluessel.getText().toString();
        char[] key = schluessel.toCharArray();
        int richtung = 0;
        String crypted = new String(crypt(plain, key, richtung));
        tw_ausgabe.setText(crypted);
    } else {
        asciTabelleAufrufen();
    }
    }



    //Zufallspasswort
    public String zufallsGen() {
        Random zufall = new Random();
        String text = "";
        int länge = zufall.nextInt(25)+8;
        for(int i = 0; i<=länge; i++) {
            char zeichen = (char)(zufall.nextInt(129)+1);
            text = (text + zeichen);
        }
        return text;
    }


    // Ver/Entschlüsselung

    public static char[] crypt(char[] plain, char[] key, int richtung) {

        char[] output = new char[plain.length];

        for (int i = 0; i < plain.length; i++) {
            if (richtung == 1) {
                int result = (plain[i] + key[i % key.length]) % 128;
                output[i] = (char) result;
            }
            else if (richtung == 0){
                int result;
                if (plain[i] - key[i % key.length] < 0) result =
                        (plain[i] - key[i % key.length]) + 128;
                else result = (plain[i] - key[i % key.length]) % 128;
                output[i] = (char) result;

            }
        }

        return output;
    }

    // ASCI Tabelle aufrufen
    public void asciTabelleAufrufen() {
        String url = "http://www.tabelle.info/ascii_zeichen_tabelle.html";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
