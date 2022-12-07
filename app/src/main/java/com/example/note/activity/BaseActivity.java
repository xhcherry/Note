package com.example.note.activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note.util.ActivityCollector;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long nowKeyDownTime = System.currentTimeMillis();
        long delay = nowKeyDownTime - ActivityCollector.lastKeyDownTime;
        if (delay > 3000) {
            Toast toast = Toast.makeText(getApplicationContext(), "再按一次返回键，退出应用", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            ActivityCollector.lastKeyDownTime = System.currentTimeMillis();
        } else {
            //退出
            ActivityCollector.finishAll();
        }
    }
}