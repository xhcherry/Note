package com.example.note.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.note.R;
public class ViewPagerTab extends Fragment implements View.OnClickListener {
    private LinearLayout jobArea;
    private LinearLayout messageArea;
    private LinearLayout mineArea;
    private ImageView jobImg;
    private ImageView messageImg;
    private ImageView mineImg;
    private TextView jobTxt;
    private TextView messageTxt;
    private TextView mineTxt;
    private ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.bottom_bar_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        jobArea = view.findViewById(R.id.job_area);
        jobArea.setTag(0);
        messageArea = view.findViewById(R.id.message_area);
        messageArea.setTag(1);
        mineArea = view.findViewById(R.id.mine_area);
        mineArea.setTag(2);
        jobImg = view.findViewById(R.id.job_img);
        messageImg = view.findViewById(R.id.message_img);
        mineImg = view.findViewById(R.id.mine_img);
        jobTxt = view.findViewById(R.id.job_txt);
        messageTxt = view.findViewById(R.id.message_txt);
        mineTxt = view.findViewById(R.id.mine_txt);
        jobArea.setOnClickListener(this);
        messageArea.setOnClickListener(this);
        mineArea.setOnClickListener(this);
        jobImg.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        mineImg.setOnClickListener(this);
        jobTxt.setOnClickListener(this);
        messageTxt.setOnClickListener(this);
        mineTxt.setOnClickListener(this);
    }

    public void setCurrentTab(int position) {
        jobImg.setImageResource(R.mipmap.zhuye1);
        jobTxt.setTextColor(getResources().getColor(R.color.bottom_tab_NORMAL_COLOR));
        messageImg.setImageResource(R.mipmap.sousuo);
        messageTxt.setTextColor(getResources().getColor(R.color.bottom_tab_NORMAL_COLOR));
        mineImg.setImageResource(R.mipmap.geren);
        mineTxt.setTextColor(getResources().getColor(R.color.bottom_tab_NORMAL_COLOR));
        switch (position) {
            case 0:
                jobImg.setImageResource(R.mipmap.zhuyeselect);
                jobTxt.setTextColor(getResources().getColor(R.color.zhuyao));
                break;
            case 1:
                messageImg.setImageResource(R.mipmap.sousuoselect);
                messageTxt.setTextColor(getResources().getColor(R.color.zhuyao));
                break;
            case 2:
                mineImg.setImageResource(R.mipmap.gerenselect);
                mineTxt.setTextColor(getResources().getColor(R.color.zhuyao));
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.job_area:
            case R.id.job_img:
            case R.id.job_txt:
                viewPager.setCurrentItem(0);
                break;
            case R.id.message_area:
            case R.id.message_img:
            case R.id.message_txt:
                viewPager.setCurrentItem(1);
                break;
            case R.id.mine_area:
            case R.id.mine_img:
            case R.id.mine_txt:
                viewPager.setCurrentItem(2);
                break;
        }
    }
}