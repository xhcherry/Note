package com.example.note.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.adapter.NoteAdapter;
import com.example.note.services.DownloadBinder;
import com.example.note.services.DownloadListener;
import com.example.note.services.DownloadServices;
import com.example.note.vo.ArticleDto;

import java.util.ArrayList;
import java.util.List;

public class DownActivity extends BaseActivity implements View.OnClickListener {
    private Button startBtn;
    private Button stopBtn;
    private Button cancelBtn;
    private ProgressBar progressBar;
    private NoteAdapter adapter;
    private TextView progressTxt;
    private List<ArticleDto> articleDtos = new ArrayList<>();
    private DownloadBinder binder;
    private Handler mHandler;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        startBtn = findViewById(R.id.start_btn);
        stopBtn = findViewById(R.id.stop_btn);
        cancelBtn = findViewById(R.id.cancle_btn);
        progressTxt = findViewById(R.id.progress_txt);
        progressBar = findViewById(R.id.progress_bar);
        stopBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent = new Intent(DownActivity.this, DownloadServices.class);
        intent.putExtra("url", "https://dldir1.qq.com/qqfile/qq/PCQQ9.6.9/QQ9.6.9.28878.exe");
        switch (view.getId()) {
            case R.id.start_btn:
                DownActivity.this.bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        binder = (DownloadBinder) iBinder;
                        binder.setListener(new DownloadListener() {
                            @Override
                            public void onSuccess() {

                                progressTxt.setText("成功");
                                //progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure() {

                                progressBar.setVisibility(View.GONE);
                                progressTxt.setText("下载失败");
                            }

                            @Override
                            public void showProgress(int progress) {
                                progressTxt.setText("已下载" + progress + "%");
                            }

                            @Override
                            public void onCancel() {

                                progressTxt.setText("已取消");
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onStop(int downedLength) {
                                progressTxt.setText("已暂停，当前已下载" + downedLength / 1024 / 1024 + "M");
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        binder.start();
                        progressBar.setVisibility(View.VISIBLE);
                        stopBtn.setEnabled(true);
                        cancelBtn.setEnabled(true);
                        startBtn.setEnabled(false);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                    }
                }, Context.BIND_AUTO_CREATE);
                break;
            case R.id.stop_btn:
                binder.stop();
                stopBtn.setEnabled(false);
                cancelBtn.setEnabled(true);
                startBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.cancle_btn:
                binder.cancel();
                stopBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
                startBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }
}