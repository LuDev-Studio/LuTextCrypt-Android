package lutextcrypt.java.luca.com.lutextcrypt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainActivityViewPager extends FragmentPagerAdapter {
    private String[] tabTitles = {"Verschlüsselungen", "Werkzeuge"};

    public MainActivityViewPager(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new MainActivityTabVerschluesselungen();
            case 1: return new MainActivityTabWerkzeuge();
        }



        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
