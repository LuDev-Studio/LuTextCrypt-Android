package lutextcrypt.java.luca.com.lutextcrypt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity  {

    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Vibrator vibrator;


    Boolean vibrieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            ladeEinstellungen();
            initKomponenten();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(vibrieren)
           vibrator.vibrate(50);

        int id = item.getItemId();
        if(id==R.id.main_menu_item_help) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_hilfe_main, null);
            AlertDialog.Builder db = new AlertDialog.Builder(MainActivity.this);
            db.setView(view);

            db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   if(vibrieren)
                    vibrator.vibrate(50);
                }
            });
            db.create().show();



        } else if (id==R.id.main_menu_item_settings) {
            Intent intent = new Intent(MainActivity.this, EinstellungenActivity.class);
            startActivity(intent);
        }


        return true;
    }


    private void ladeEinstellungen() {
        SharedPreferences prefs = getSharedPreferences("sharedpreferences", 0);
        vibrieren = prefs.getBoolean("PREFSKEY_VIBRIEREN", false);
    }

    private void initKomponenten() {
        // ToolBar und Tabs

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        MainActivityViewPager mainActivityViewPager = new MainActivityViewPager(getSupportFragmentManager());
        viewPager.setAdapter(mainActivityViewPager);

        tabLayout = (TabLayout) findViewById(R.id.TabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });








        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }





}