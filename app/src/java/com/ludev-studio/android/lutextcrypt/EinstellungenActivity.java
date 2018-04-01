package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

public class EinstellungenActivity extends AppCompatActivity implements View.OnClickListener{
        Toolbar toolbar;
        Switch switch_vibrieren,switch_drehen;

        SharedPreferences prefs;
        SharedPreferences.Editor prefsEditor;

        Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);
            initKomponenten();
    }

    private void initKomponenten() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_einstellungen);
        setSupportActionBar(toolbar);

        switch_vibrieren = (Switch) findViewById(R.id.switch_vibration);
        switch_vibrieren.setOnClickListener(this);
        switch_drehen = (Switch) findViewById(R.id.switch_ausrichtung);
        switch_drehen.setOnClickListener(this);

        prefs = this.getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        prefsEditor = prefs.edit();
        switch_vibrieren.setChecked(prefs.getBoolean("PREFSKEY_VIBRIEREN", true));

        vibrieren = true;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_vibration: {
               if(switch_vibrieren.isChecked()) {
                    prefsEditor.putBoolean("PREFSKEY_VIBRIEREN", true);
                    prefsEditor.commit();
               }    else {
                   prefsEditor.putBoolean("PREFSKEY_VIBRIEREN", false);
                   prefsEditor.commit();

               }
                break;

            }
        }


    }
}
