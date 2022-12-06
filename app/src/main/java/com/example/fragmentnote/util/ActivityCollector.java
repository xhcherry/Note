package com.example.fragmentnote.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static long lastKeyDownTime=0;//上次按下的时间默认为0
    private static final List<Activity> openedActivities=new ArrayList<>();
    public static void addActivity(Activity activity){
        openedActivities.add(activity);
    }
    public static void finishAll(){
        for (Activity openActivity:openedActivities) {
            openActivity.finish();
        }
        openedActivities.clear();
    }
    public static void removeActivity(Activity activity){
        openedActivities.remove(activity);
    }
}