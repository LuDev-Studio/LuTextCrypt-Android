package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class CaesarActivity extends AppCompatActivity implements View.OnClickListener {
   private Toolbar toolbar;
    private SeekBar seekbar;
    private TextView tw_verschiebung;
    private EditText edt_eingabe;
    private TextView tw_ausgabe;
    private Button btn_verschluesseln;
    private Button btn_entschluesseln;

    private RadioButton radio_asci;
    private RadioButton radio_nurklein;
    private RadioButton radio_nurgross;

    private String text;
    private int offset;

    Vibrator vibrator;

    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caesar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_caesar);
        setSupportActionBar(toolbar);
        initKomponenten();
    }

    private void initKomponenten() {
        tw_verschiebung = (TextView) findViewById(R.id.textView2);

        seekbar = (SeekBar) findViewById(R.id.seekBar5);
        seekbar.setMax(128);
        seekbar.setProgress(100);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tw_verschiebung.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edt_eingabe = (EditText) findViewById(R.id.edittext_eingabe);
        tw_ausgabe = (TextView) findViewById(R.id.textView_ausgabe);

        radio_asci = (RadioButton) findViewById(R.id.radioButton_asci);
        radio_nurklein = (RadioButton) findViewById(R.id.radioButton_nurklein);
        radio_nurgross = (RadioButton) findViewById(R.id.radioButton_nurgross);
        radio_asci.setOnClickListener(this);
        radio_nurklein.setOnClickListener(this);
        radio_nurgross.setOnClickListener(this);

        btn_verschluesseln = (Button) findViewById(R.id.button);
        btn_entschluesseln = (Button) findViewById(R.id.button5);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vibrieren = getIntent().getExtras().getBoolean("vibrieren");


    }

    @Override
    public void onClick(View v) {
       if(vibrieren)
        vibrator.vibrate(50);
        if (v.getId() == R.id.radioButton_asci) {
            radio_nurklein.setChecked(false);
            radio_nurgross.setChecked(false);
        } else if (v.getId() == R.id.radioButton_nurklein) {
            radio_asci.setChecked(false);
            radio_nurgross.setChecked(false);
        } else if (v.getId() == R.id.radioButton_nurgross) {
            radio_asci.setChecked(false);
            radio_nurklein.setChecked(false);
        } else if (v.getId() == R.id.button) {
            if (radio_asci.isChecked()) {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                 tw_ausgabe.setText(encryptString(text, offset));
             } else if (radio_nurklein.isChecked()) {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                tw_ausgabe.setText(encryptString1(text, offset));
            } else {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                tw_ausgabe.setText(encryptString3(text, offset));
            }
    } else {
            if (radio_asci.isChecked()) {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                tw_ausgabe.setText(decryptString(text, offset));
            } else if (radio_nurklein.isChecked()) {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                tw_ausgabe.setText(decryptString1(text, offset));
            } else {
                text = edt_eingabe.getText().toString();
                offset = Integer.parseInt(tw_verschiebung.getText().toString());
                tw_ausgabe.setText(decryptString3(text, offset));
            }
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


    // Cäsar nur kleinbuchstaben

    public static String encryptString1(String text, int offset) {
        String ret = "";
        for (int i = 0; i < text.length(); i++) {
            ret += encrypt1(text.charAt(i), offset);
        }
        return ret;
    }

    public static char encrypt1(char c, int offset) {

        if ((int)c >= 96 && (int)c <= 123) {
            offset = (offset%26);
            if ((int)c+offset > 122) {
                return (char)((int)c+offset-118+92);
            } else {
                return (char)((int)c+offset);
            }
        } else {
            return (char)((int)c);
        }
    }



    // Cäsar Verschlüsselung nur Großbuchstaben

    public static String encryptString3(String text, int offset) {
        String ret = "";
        for (int i = 0; i < text.length(); i++) {
            ret += encrypt3(text.charAt(i), offset);
        }
        return ret;
    }

    public static char encrypt3(char c, int offset) {

        if ((int)c >= 59 && (int)c <= 91) {
            offset = (offset%26);
            if ((int)c+offset > 96) {
                return (char)((int)c+offset-118+92);
            } else {
                return (char)((int)c+offset);
            }
        } else {
            return (char)((int)c);
        }
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




//Cäsar nur kleinbuchstaben

    public static String decryptString1(String text, int offset) {
        String ret = "";

        for (int i = 0; i < text.length(); i++) {
            ret += decrypt1(text.charAt(i), offset);
        }
        return ret;
    }


    public static char decrypt1(char c, int offset) {

        if ((int)c >= 96 && (int)c <= 123) {
            offset = (offset%26);
            if ((int)c-offset < 97) {
                return (char)((int)c-offset+26);
            } else {
                return (char)((int)c-offset);
            }
        } else {
            return (char)((int)c);
        }

    }
//Cäsar Entschlüsselung nur Großbuchstaben

    public static String decryptString3(String text, int offset) {
        String ret = "";

        for (int i = 0; i < text.length(); i++) {
            ret += decrypt3(text.charAt(i), offset);
        }
        return ret;
    }


    public static char decrypt3(char c, int offset) {

        if ((int)c >= 65 && (int)c <= 90) {
            offset = (offset%26);
            if ((int)c-offset < 65) {
                return (char)((int)c-offset+26);
            } else {
                return (char)((int)c-offset);
            }
        } else {
            return (char)((int)c);
        }
    }










}
