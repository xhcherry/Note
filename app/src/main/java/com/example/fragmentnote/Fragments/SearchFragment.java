package com.example.fragmentnote.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fragmentnote.R;
import com.example.fragmentnote.activity.down;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private Button downloadBtn;
    private Button down1;
    private ImageView imageView;
    private Handler handler;
    private ListView contactlv;
    private ArrayAdapter<String> adapter;
    private List<String> datas = new ArrayList<>();

    @SuppressLint({"HandlerLeak", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.viewpager_search_fragment, container, false);
        Intent intent = new Intent("我的广播");
        getContext().sendOrderedBroadcast(intent, null);

        downloadBtn = view.findViewById(R.id.download_btn);
        imageView = view.findViewById(R.id.show_img);
        down1 = view.findViewById(R.id.down);
        contactlv = view.findViewById(R.id.contact_lv);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, datas);
        contactlv.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            readContact();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CONTACTS
            }, 1);
        }
        readContact();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        InputStream in = (InputStream) msg.obj;
                        imageView.setImageBitmap(BitmapFactory.decodeStream(in));
                        break;
                    case 2:
                        break;
                }
            }
        };
        down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), down.class);
                startActivity(intent);
            }
        });
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlStr = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F5476e32631957.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1670632921&t=3d116f315eccd93bbd62be624cdedf5c";
                        try {
                            URL url = new URL(urlStr);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(8000);
                            conn.setReadTimeout(10000);
                            conn.connect();
                            Message msg = new Message();
                            if (conn.getResponseCode() == 200) {
                                InputStream in = conn.getInputStream();
                                msg.obj = in;
                                msg.what = 1;
                            } else {
                                msg.obj = conn.getErrorStream();
                                msg.what = 2;
                            }
                            handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return view;
    }

    private void readContact() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor query = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                @SuppressLint("Range") String phone = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String name = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                datas.add(name + ":" + phone);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults != null && grantResults[0] != 0) {
                readContact();
            }
        }
    }
}