package com.example.fragmentnote;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.fragmentnote.Fragments.ViewPagerTab;
import com.example.fragmentnote.Fragments.HomeFragment;
import com.example.fragmentnote.Fragments.SearchFragment;
import com.example.fragmentnote.Fragments.MyFragment;
import com.example.fragmentnote.activity.BaseActivity;
import com.example.fragmentnote.adapter.FragmentAdapter;
import com.example.fragmentnote.receiver.MyReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends BaseActivity {
    private ViewPagerTab viewPagerTab;
    private ViewPager viewPager;
    private final String TAB = "MainTabActivity";
    private final FragmentManager fm = getSupportFragmentManager();
    private final List<Fragment> fragmentList = new ArrayList();
    private FragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        MyReceiver receiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("我的广播");
        intentFilter.setPriority(1);
        registerReceiver(receiver,intentFilter);
        Log.e(TAB,"onCreate() in MainTabActivity is running!");
        viewPagerTab = new ViewPagerTab();
        fm.beginTransaction().replace(R.id.viewpager_tab, viewPagerTab).commit();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new MyFragment());

        viewPager = findViewById(R.id.viewpager_content);
        adapter = new FragmentAdapter(fm, 0, fragmentList);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        adapter = new FragmentAdapter(fm,0,fragmentList);
        viewPager.setAdapter(adapter);

        viewPagerTab.setViewPager(viewPager);
    }

    //当你从stop状态之间进行激活状态的话...
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //正在启动的状态：看得到&不能交互
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAB,"onStart() in MainTabActivity is running!");
    }

    //启动的完成：看得到&能交互
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAB,"onResume() in MainTabActivity is running!");
    }

    //暂停的状态：看得见+不能交互（很快），关闭资源（轻量级，不能执行过程的时间）
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAB,"onPause() in MainTabActivity is running!");
    }

    //停止的状态：看不到+不能交互，释放资源（轻量级，不能执行过程的时间）
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAB,"onStop() in MainTabActivity is running!");
    }

    //销毁的状态：看不到+不能交互，释放资源（重量级）
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAB,"onDestroy() in MainTabActivity is running!");
    }
}
