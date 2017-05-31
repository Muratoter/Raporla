package com.muratoter.isg.raporla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewPager=(ViewPager)findViewById(R.id.container);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if(sharedPreferences.getString("activity_name","").equals("isg_uzmani_activity")){
                startActivity(new Intent(getApplicationContext(),IsgUzmaniActivity.class));
                finish();
            }
            else if(sharedPreferences.getString("activity_name","").equals("uretim_sorumlu_activity")){
                startActivity(new Intent(getApplicationContext(),UretimSorumlusuActivity.class));
                finish();
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new RegisterFragment();


                default:return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Giriş Yap";
                case 1:
                    return "Kaydol";
            }
            return null;
        }
    }


}
