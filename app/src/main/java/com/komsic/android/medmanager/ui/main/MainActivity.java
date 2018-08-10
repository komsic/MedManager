package com.komsic.android.medmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.sync.SyncAlarmService;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.main.list.MedListFragment;
import com.komsic.android.medmanager.ui.main.schedule.MedScheduleFragment;

import static com.komsic.android.medmanager.data.sync.SyncAlarmService.ACTION_NOTIFY;

public class MainActivity extends BaseActivity implements MainMvpView {

    private MainMvpPresenter<MainMvpView> mPresenter;
    private TabLayout tabLayout;
    private ViewPager medPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        medPager = findViewById(R.id.main_view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new MainPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        setUp();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void setUp() {
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    MedScheduleFragment.newInstance(),
                    MedListFragment.newInstance()
            };

            private final String[] mFragmentNames = new String[] {
                    "Med Schedule",
                    "Med List"
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        medPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(medPager);

        FloatingActionButton fabNewMed = findViewById(R.id.fab_new_med);
        fabNewMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openAddMedDialog(getSupportFragmentManager());
//                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
//                startActivity(intent);
            }
        });

        Intent startSyncServiceIntent = new Intent(this, SyncAlarmService.class);
        startSyncServiceIntent.setAction(ACTION_NOTIFY);
        startService(startSyncServiceIntent);
    }
}
