package com.lcs.joke.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.lcs.joke.R;
import com.lcs.joke.databinding.ActivityMainBinding;
import com.lcs.joke.ui.adapter.TabPagerAdapter;
import com.lcs.joke.utils.rxbus.Callback;
import com.lcs.joke.utils.rxbus.RxBus;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initLayout();
    }

    private void initLayout() {
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(tabPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

        RxBus.getDefault().subscribe(this, new Callback<Bitmap>() {
            @Override
            public void onEvent(Bitmap bitmap) {
                showBig(bitmap);
            }
        });
    }

    public void showBig(Bitmap bitmap) {
        binding.ivBig.setVisibility(View.VISIBLE);
        binding.ivBig.setImageBitmap(bitmap);
    }

    public void onClickBig(View view) {
        binding.ivBig.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        if (binding.ivBig.isShown()) {
            binding.ivBig.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
