package com.ludevstudio.android.lutextcrypt;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import lutextcrypt.java.luca.com.lutextcrypt.R;

import static lutextcrypt.java.luca.com.lutextcrypt.R.*;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Vibrator vibrator;


    Boolean vibrieren;
    int vibrationsdauer;

    public InterstitialAd mInterstitialAd;


    // Permissions
    private static final String permissionWriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permissionReadExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permissionAccessNetworkState = Manifest.permission.ACCESS_NETWORK_STATE;
    private static final String permissionInternet = Manifest.permission.INTERNET;
    private static final int myRequestCode = 0;


    // Fragments for NavDrawer


   private Fragment fragmentStartPage;
    private Fragment fragmentPrimitiveCiphers;
    private Fragment fragmentSymetricCiphers;
    private Fragment fragmentAsymetricCiphers;
    private Fragment fragmentGenerateCipherKeys;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment[] navFragments = new Fragment[] {fragmentStartPage, fragmentSymetricCiphers, fragmentAsymetricCiphers};
    int lastItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        ladeEinstellungen();
        initKomponenten();
        //  showPermissionWriteExternalStorage();
        requestPermissionWriteExternalStorage();
        requestPermissionReadExternalStorage();
        requestPermissionAccessNetworkState();
        requestPermissionInternet();


        MobileAds.initialize(this, "ca-app-pub-***************************");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-*********************");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

            if(id==R.id.nav_home) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainlayout, fragmentStartPage);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                lastItem = 0;
            } else if (id==R.id.nav_ciphers_symetric) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainlayout, fragmentSymetricCiphers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                lastItem = 2;
            }  else if(id==R.id.nav_ciphers_asymetric) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainlayout, fragmentAsymetricCiphers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                lastItem = 3;
            } else if(id==R.id.nav_ciphers_primitive) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainlayout, fragmentPrimitiveCiphers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                lastItem = 1;
            } else if(id==R.id.nav_tools_generate_cipher_keys) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainlayout, fragmentGenerateCipherKeys);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                lastItem = 1;
            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Dialog db;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (vibrieren)
            vibrator.vibrate(vibrationsdauer);
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        int id = item.getItemId();
        if (id == R.id.main_menu_item_help) {
            db = new Dialog(this);
            db.setContentView(R.layout.dialog_hilfe_main);
            Button btn_dialog_ok = (Button) db.findViewById(R.id.button_dialo_hilfe_main);
            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vibrieren)
                        vibrator.vibrate(vibrationsdauer);
                    db.cancel();
                }
            });
            db.show();


        } else if (id == R.id.main_menu_item_settings) {
            Bundle extras = new Bundle();
            extras.putBoolean("vibrieren", vibrieren);
            extras.putInt("vibrationsdauer", vibrationsdauer);

            Intent intent = new Intent(MainActivity.this, EinstellungenActivity.class);
            intent.putExtra("extras", extras);
            startActivity(intent);
        }


        return true;
    }



    public void ladeEinstellungen() {

        SharedPreferences prefs = getSharedPreferences("sharedpreferences", 0);
        vibrieren = prefs.getBoolean("VIBRIEREN_ONOFF", true);
        vibrationsdauer = prefs.getInt("VIBRIEREN_DAUER", 50);

    }

    private void initKomponenten() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        // Fragments and Changer


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentStartPage = (StrartPageFragment) Fragment.instantiate(this, StrartPageFragment.class.getName(), null);
        fragmentPrimitiveCiphers = (PrimitiveCiphersFragment) Fragment.instantiate(this, PrimitiveCiphersFragment.class.getName(), null);
        fragmentSymetricCiphers = (SymetricCiphersFragment) Fragment.instantiate(this, SymetricCiphersFragment.class.getName(), null);
        fragmentAsymetricCiphers = (AsymetricCiphersFragment) Fragment.instantiate(this, AsymetricCiphersFragment.class.getName(), null);
        fragmentGenerateCipherKeys = (GenerateCipherKeysFragment) Fragment.instantiate(this, GenerateCipherKeysFragment.class.getName(), null);

        fragmentTransaction.add(id.mainlayout, fragmentStartPage, null);
        fragmentTransaction.commit();


    }


    private void showPermissionWriteExternalStorage() {
        int permisionCheck = ContextCompat.checkSelfPermission(this, permissionWriteExternalStorage);
        if (permisionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
        } else if (permisionCheck == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
    }


    private void requestPermissionWriteExternalStorage() {
        int permisionCheck = ContextCompat.checkSelfPermission(this, permissionWriteExternalStorage);
        if (permisionCheck == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionWriteExternalStorage)) {
            // Erklärung anzeigen
        }

        ActivityCompat.requestPermissions(this, new String[]{permissionWriteExternalStorage}, myRequestCode);
    }


    private void requestPermissionAccessNetworkState() {
        int permisionCheck = ContextCompat.checkSelfPermission(this, permissionAccessNetworkState);
        if (permisionCheck == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionWriteExternalStorage)) {
            // Erklärung anzeigen
        }

        ActivityCompat.requestPermissions(this, new String[]{permissionAccessNetworkState}, myRequestCode);
    }


    private void requestPermissionReadExternalStorage() {
        int permisionCheck = ContextCompat.checkSelfPermission(this, permissionReadExternalStorage);
        if (permisionCheck == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionReadExternalStorage)) {
            // Erklärung anzeigen
        }

        ActivityCompat.requestPermissions(this, new String[]{permissionReadExternalStorage}, myRequestCode);
    }


    private void requestPermissionInternet() {
        int permisionCheck = ContextCompat.checkSelfPermission(this, permissionInternet);
        if (permisionCheck == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionInternet)) {
            // Erklärung anzeigen
        }

        ActivityCompat.requestPermissions(this, new String[]{permissionReadExternalStorage}, myRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case myRequestCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission just granted", Toast.LENGTH_LONG).show();
                } else if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission just granted", Toast.LENGTH_LONG).show();
                } else if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission just granted", Toast.LENGTH_LONG).show();
                } else if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission just granted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "permission just denied", Toast.LENGTH_LONG).show();
                }
            }
        }


    }
}
