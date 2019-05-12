package com.example.patryk.hairfire;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

public class OwnerSalonView extends AppCompatActivity {

    private static final String TAG = "OwnerSalonView";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_view);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new OwnerInformationTab(), "Informacja");
        adapter.addFragment(new OwnerSalonVisitList(),"Rezerwacje");
        adapter.addFragment(new OwnerSalonServiceList(),"Usługi");
//        adapter.addFragment(new OpinionTab(), "Opinie");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OwnerSalonView.this, OwnerSalonList.class));
        finish();
    }
}
