package com.komsic.android.medmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.sync.SyncAlarmService;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.main.add_med.AddMedDialog;
import com.komsic.android.medmanager.ui.main.list.MedListFragment;
import com.komsic.android.medmanager.ui.main.schedule.MedScheduleFragment;
import com.komsic.android.medmanager.ui.splash.SplashActivity;

public class MainActivity extends BaseActivity implements MainMvpView {


    private MainMvpPresenter<MainMvpView> mPresenter;
    private TabLayout tabLayout;
    private ViewPager medPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isServiceCreated = getIntent()
                .getBooleanExtra(SyncAlarmService.EXTRA_MAIN_ACTIVITY_SERVICE_STATUS, false);

        tabLayout = findViewById(R.id.tab_layout);
        medPager = findViewById(R.id.main_view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new MainPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        setUp();

        if (!isServiceCreated) {
            mPresenter.startSyncAlarmService();
        }
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void setUp() {
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    MedScheduleFragment.newInstance(),
                    MedListFragment.newInstance()
            };

            private final String[] mFragmentNames = new String[] {
                    "Med Schedule",
                    "Med List"
            };

            @NonNull
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
                mPresenter.openAddMedDialog();
            }
        });
    }

    @Override
    public void openSyncAlarmService() {
        Intent intent = SyncAlarmService.getStartIntent(this);
        intent.setAction(SyncAlarmService.MAIN_ACTIVITY_SERVICE_STATUS);
        startService(intent);
    }

    @Override
    public void openSplashActivity() {
        startActivity(SplashActivity.getStartIntent(this));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void openAddMedDialog() {
        AddMedDialog dialogAddMed = new AddMedDialog();
        dialogAddMed.show(getSupportFragmentManager(), "DialogAddMed");
    }
}
