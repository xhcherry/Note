package com.example.fragmentnote.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.example.fragmentnote.listner.HttpCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    private static String ROOT = "http://10.11.48.196:8800/shiyi/";
    public static String login;
    public static String regsit;
    public static String articleList;

    static {
        login = ROOT + "user/emailLogin";
        regsit = ROOT + "user/emailRegister";
        articleList = ROOT + "web/article/list";
    }

    public static boolean checkNetworkEnable(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            Network activeNetwork = manager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            } else {
                return true;
            }
        } else {
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String decodeAsString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
        }
        return builder.toString();
    }

    public static void requestApi(String urlStr, String method, String param, int mineType, HttpCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(method);
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(16000);
                    if (method.equalsIgnoreCase("POST")) {
                        conn.setUseCaches(true);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        if (mineType == 1) {//json
                            conn.setRequestProperty("Content-Type", "application/json");
                        }
                        OutputStream out = conn.getOutputStream();
                        out.write(param.getBytes());
                        out.flush();
                    }
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        callback.onSuccess(in);
                    } else {
                        InputStream err = conn.getErrorStream();
                        callback.onFailure(err);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}