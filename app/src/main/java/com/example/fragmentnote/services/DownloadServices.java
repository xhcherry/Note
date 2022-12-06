package com.example.fragmentnote.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.fragmentnote.R;
import com.example.fragmentnote.activity.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadServices extends Service {
    private String address;
    private DownloadListener listener;
    private int totalLength;
    private int downloadedLength;
    private boolean isCancel = false;
    private boolean isStop = false;

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void start() {
        isStop = false;
        isCancel = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder builder;
                Notification notification;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    NotificationChannel channel = new NotificationChannel("001", "downloadnotification", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                    builder = new Notification.Builder(DownloadServices.this, "001");
                } else {
                    builder = new Notification.Builder(DownloadServices.this);
                }
                RemoteViews views=new RemoteViews(getPackageName(),R.layout.station_list_item);
                Intent intent=new Intent(DownloadServices.this, LoginActivity.class);
                PendingIntent pendingIntent=PendingIntent.getService(DownloadServices.this,1,intent,0);
                notification = builder
                        .setContentTitle("下载")
                        .setSmallIcon(R.mipmap.baocun)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.baocun))
                        .setSound(null, null)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
                        .setCustomContentView(views)
                        .build();
                manager.notify(1, notification);
                try {
                    URL url = new URL(address);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(10000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        totalLength = conn.getContentLength();
                        String[] names = address.split("/");
                        String fileName = names[names.length - 1];
                        File existFile = new File(getFilesDir(),  "/" + fileName);
                        if (!existFile.exists()) {
                            existFile.createNewFile();
                            downloadedLength = 0;
                        } else {
                            downloadedLength = (int) existFile.length();
                        }
                        if (downloadedLength == totalLength) {
                            listener.onSuccess();
                            stopSelf();
                        } else {
                            RandomAccessFile saveFile = new RandomAccessFile(existFile, "rw");
                            saveFile.setLength(downloadedLength);
                            byte[] buff = new byte[1024];
                            int len;
                            while ((len = in.read(buff)) != -1) {
                                if (isCancel) {
                                    existFile.delete();
                                    downloadedLength = 0;
                                    stopSelf();
                                    listener.onCancel();
                                    break;
                                }
                                if (isStop) {
                                    listener.onStop(downloadedLength);
                                    builder.setContentText("已暂停，一下载" + downloadedLength / 1024 / 1024 + "M");
                                    manager.notify(1, builder.build());
                                    break;
                                }
                                saveFile.write(buff);
                                downloadedLength += len;
                                if (downloadedLength == totalLength) {
                                    listener.onSuccess();
                                    stopSelf();
                                    break;
                                }
                                int percent = (int) (100 * (float) downloadedLength / totalLength);
                                listener.showProgress(percent);
                                //builder.setContentText(percent + "%");
                                builder.setProgress(100, percent, true);
                                manager.notify(1, builder.build());
                            }
                        }
                    } else {
                        listener.onFailure();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        isStop = true;
    }

    public void cancel() {
        isCancel = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DownloadService", "onStartCommand() is Running");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        address = intent.getStringExtra("url");
        return new DownloadBinder(this);
    }
}