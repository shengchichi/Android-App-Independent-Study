package com.example.app;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private TabLayout mTabs;
    private String date_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        init();
        Intent intent = this.getIntent();
        //取得傳遞過來的資料
        date_str = intent.getStringExtra("date_str");
        Bundle bundle = new Bundle();
        bundle.putString("date_str", date_str);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        FocusFragment focusfgm = new FocusFragment();
        HabitFragment habitfgm = new HabitFragment();
        PostureFragment postfgm = new PostureFragment();
        VideoFragment videofgm = new VideoFragment();

        focusfgm.setArguments(bundle);
        habitfgm.setArguments(bundle);
        postfgm.setArguments(bundle);
        videofgm.setArguments(bundle);

        adapter.addFragment(focusfgm, "專心");
        adapter.addFragment(habitfgm, "習慣");
        adapter.addFragment(postfgm, "姿態");
        adapter.addFragment(videofgm, "影片");
        mViewPager.setAdapter(adapter);

        mTabs.setupWithViewPager(mViewPager);
        mTabs.getTabAt(0).setText("專心");
        mTabs.getTabAt(1).setText("習慣");
        mTabs.getTabAt(2).setText("姿態");
        mTabs.getTabAt(3).setText("影片");
    }

    private void init(){
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabs = (TabLayout)findViewById(R.id.tabs);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private Context context;
        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
