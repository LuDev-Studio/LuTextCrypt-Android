package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextInAsciActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText edt_eingabe;
    private TextView tw_ausgabe;
    private Button btn_umwandeln;
    Vibrator vibrator;
    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_asci);
        toolbar = (Toolbar) findViewById(R.id.toolbar_textinasci);
        setSupportActionBar(toolbar);
         initKomponenten();
    }

    private void initKomponenten() {
        edt_eingabe = (EditText) findViewById(R.id.editText_eingabe_textinasci);
        tw_ausgabe = (TextView) findViewById(R.id.textview_ausgabe_textinasci);
        btn_umwandeln = (Button) findViewById(R.id.button_umwandeln_textinasci);
        btn_umwandeln.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrieren = getIntent().getExtras().getBoolean("vibrieren");

    }


    @Override
    public void onClick(View v) {
      if(vibrieren)
       vibrator.vibrate(50);
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

}
