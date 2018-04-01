package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Rot13Activity extends AppCompatActivity implements OnClickListener{
    private Button button_verschluesseln;
    private Button button_entschluesseln;
    private EditText edt_eingabe;
    private TextView tw_ausgabe;
    Toolbar toolbar;

    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rot13);
            initKomponenten();
    }
    Vibrator vibrator;
    private void initKomponenten() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_rot13);
        setSupportActionBar(toolbar);

        edt_eingabe = (EditText) findViewById(R.id.editText);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausg);
        button_verschluesseln = (Button) findViewById(R.id.button_vers);
        button_verschluesseln.setOnClickListener(this);
        button_entschluesseln = (Button) findViewById(R.id.button_ents);
        button_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            vibrieren = getIntent().getExtras().getBoolean("vibrieren");

            }

    @Override
    public void onClick(View v) {
       if(vibrieren)
        vibrator.vibrate(50);
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


}
