package com.indev.jubicare_assistants.signup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;

public class SignupActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPrefHelper sharedPrefHelper;
    private final String[] PAGE_TITLES = new String[]{
            "As Patient ",
            "As Agent "
    };
    private final Fragment[] PAGES = new Fragment[]{
            new SignupPatientFragment(),
            new SignupAgentFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        setTitle("Sign Up");
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.viewpager);
        sharedPrefHelper=new SharedPrefHelper(this);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


    }
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }
}
