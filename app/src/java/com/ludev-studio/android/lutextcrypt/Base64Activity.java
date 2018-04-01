package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Base64Activity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText edt_eingabe;
    TextView tw_ausgabe;
    Button btn_verschluesseln;
    Button btn_entschluesseln;
    Vibrator vibrator;
    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base64);
        toolbar = (Toolbar) findViewById(R.id.toolbar_base64);
        setSupportActionBar(toolbar);
        initKomponenten();
    }

    private void initKomponenten() {
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_base64);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_base64);
        btn_verschluesseln = (Button) findViewById(R.id.button_vers_base64);
        btn_entschluesseln = (Button) findViewById(R.id.button_ents_base64);
        btn_verschluesseln.setOnClickListener(this);
        btn_entschluesseln.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrieren = getIntent().getExtras().getBoolean("vibrieren");

    }

    @Override
    public void onClick(View v) {
               if(vibrieren)
                vibrator.vibrate(50);
              if(v.getId() == R.id.button_vers_base64) {
            byte[] bytes = edt_eingabe.getText().toString().getBytes();
            tw_ausgabe.setText(Base64.encodeToString(bytes, 0));
        } else {
            byte[] encoded = edt_eingabe.getText().toString().getBytes();
            byte[] decoded = Base64.decode(encoded, 0);
            tw_ausgabe.setText(new String(decoded));

        }
    }
}
