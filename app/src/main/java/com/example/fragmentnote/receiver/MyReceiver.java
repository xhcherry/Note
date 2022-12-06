package com.example.fragmentnote.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"手机飞行模式发生变化",Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }
}
