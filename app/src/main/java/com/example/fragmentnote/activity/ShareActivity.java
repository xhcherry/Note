package com.example.fragmentnote.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fragmentnote.R;

public class ShareActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}