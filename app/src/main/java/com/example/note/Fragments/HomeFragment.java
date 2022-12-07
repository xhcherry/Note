package com.example.note.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.note.App;
import com.example.note.R;
import com.example.note.activity.EditActivity;
import com.example.note.adapter.NoteAdapter;
import com.example.note.listener.HttpCallback;
import com.example.note.util.HttpUtils;
import com.example.note.util.MysqliteOpenHelper;
import com.example.note.vo.ArticleDto;
import com.example.note.vo.ResponseListData;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Nullable
    private ListView station_lv;
    private MysqliteOpenHelper helper;
    private SQLiteDatabase db;
    private NoteAdapter adapter;
    private List<ArticleDto> articleDtos = new ArrayList<>();
    private Handler mhandler;

    @SuppressLint("HandlerLeak")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.viewpager_home_fragment, container, false);
        mhandler = new Handler() {
            public void handleMessage(Message msg) {
                Gson gson = new Gson();
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ResponseListData data = gson.fromJson(msg.obj.toString(), ResponseListData.class);
                    if (data.getCode() == 200) {
                        ResponseListData listData = gson.fromJson(gson.toJson(data.getData()), ResponseListData.class);
                        if (listData != null) {
                            List<ArticleDto> records = listData.getRecords();
                            articleDtos.clear();
                            articleDtos.addAll(records);
                            adapter.notifyDataSetChanged();
                            //将上一次保存的最新数据删除
                            deleteHistoryNewestArticle();
                            //保存到数据库
                            saveNewestNote(records);
                        }
                    } else {
                        queryHistoryArticles();
                        Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    queryHistoryArticles();
                    Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        //adapter = new NoteAdapter(articleDtos, getContext());

        App application=(App)getActivity().getApplication();
        db=application.db;
        initView(view);
        if (HttpUtils.checkNetworkEnable(getContext())) {
            requestData();
        } else {
            //从数据库sqlite拿到上一次最新的数据
            Toast.makeText(getContext(), "手机不能联网，检查网络", Toast.LENGTH_SHORT).show();
        }

        /*ListView lv1 = view.findViewById(R.id.lv);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                startActivity(intent);
            }
        });*/
       /* @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        List<note> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            note note1 = new note();
            note1.setTitle("笔记标题" + i);
            note1.setGoodNum("点赞");
            note1.setReadNum("浏览");
            note1.setContent("笔记概述");
            note1.setTime(formatter.format(date));
            list.add(note1);
        }
        noteadapter1 myAdapter = new noteadapter1(getContext(), list);
        //lv1.setAdapter(myAdapter);*/
        return view;
    }

    @SuppressLint("Range")
    private void queryHistoryArticles() {
        Cursor cursor=db.query("article",null,null,null,null,null,null);
        if(cursor!=null){
            articleDtos.clear();
            while(cursor.moveToNext()){
                ArticleDto articleDto=new ArticleDto();
                articleDto.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                articleDto.setCreateTime(cursor.getString(cursor.getColumnIndex("title")));
                articleDto.setTitle(cursor.getString(cursor.getColumnIndex("ceatetime")));
                articleDtos.add(articleDto);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteHistoryNewestArticle() {
        db.delete("article", null, null);
    }

    private void initView(View view) {
        station_lv = view.findViewById(R.id.lv);
        adapter = new NoteAdapter(articleDtos, getContext());
        station_lv.setAdapter(adapter);
        station_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:17755610719"));*/
                startActivity(intent);
            }
        });
    }

    private void saveNewestNote(List<ArticleDto> records) {

        if (records != null && records.size() > 0) {
            for (ArticleDto articleDto : records) {
                ContentValues values = new ContentValues();
                ///values.put("id",articleDto.getId());
                values.put("title", articleDto.getTitle());
                values.put("contentMd", articleDto.getContentMd());
                values.put("avatar", articleDto.getAvatar());
                values.put("isStick", articleDto.getIsStick());
                values.put("createTime", articleDto.getCreateTime());
                values.put("categoryName", articleDto.getCategoryName());
                //values.put("tagVOList",articleDto.getTagVOList().toString());
                db.insert("article", null, values);
            }
        }
    }

    private void requestData() {


//2通知listview更新数据
//3将上一次数据删除
//4保存最新的数据
//1调用接口
        HttpUtils.requestApi(HttpUtils.articleList, "GET", null, 0, new HttpCallback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = HttpUtils.decodeAsString(inputStream);
                mhandler.sendMessage(msg);
            }

            @Override
            public void onFailure(InputStream inputStream) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = HttpUtils.decodeAsString(inputStream);
                mhandler.sendMessage(msg);
            }
        });

    }
}
