package com.example.fragmentnote.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragmentnote.MainTabActivity;
import com.example.fragmentnote.R;
import com.example.fragmentnote.listner.HttpCallback;
import com.example.fragmentnote.util.HttpUtils;
import com.example.fragmentnote.vo.LoginParam;
import com.example.fragmentnote.vo.ResponseListData;
import com.google.gson.Gson;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdt;
    private EditText passwordEdt;
    private Button submitBtn;
    private Button registerBtn;
    private CheckBox rememberCb;
    private Handler mHandler;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint({"MissingInflatedId", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //1、 Context
        preferences = getSharedPreferences("historyAccount", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //2、 Activity
        ///preferences = getPreferences( Context.MODE_PRIVATE);
        mHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                String result = msg.obj.toString();
                if (msg.what == 1) {//请求服务器资源成功
                    ResponseListData data = gson.fromJson(result, ResponseListData.class);
                    if (data.getCode() == 200) { //regist success
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        if (rememberCb.isChecked()) {
                            //存"account":"username:password"
                            String account = emailEdt.getText().toString() + ":" + passwordEdt.getText().toString();
                            editor.putString("account", account);
                            editor.commit();
                        } else {
                            //删除已经存的数据
                            editor.remove("account");
                            editor.commit();
                        }
                        Intent intent=new Intent(LoginActivity.this, MainTabActivity.class);
                        startActivity(intent);
                        //finish();

                    } else {
                        Toast.makeText(LoginActivity.this,
                                data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {//请求服务器资源失败
                    Toast.makeText(LoginActivity.this,
                            result, Toast.LENGTH_SHORT).show();
                }
            }
        };

        emailEdt = findViewById(R.id.login_email);
        passwordEdt = findViewById(R.id.login_password);
        submitBtn = findViewById(R.id.login_submit);
        registerBtn = findViewById(R.id.login_register);
        rememberCb = findViewById(R.id.login_remember);
        if (rememberCb.isChecked()) {
            //取出用户名和密码，填充到对应编辑框
            //"account":"username:password"
            String account = preferences.getString("account", "");
            if (!account.equals("")) {
                String[] split = account.split(":");
                emailEdt.setText(split[0]);
                passwordEdt.setText(split[1]);
            }
        }
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    LoginParam param = new LoginParam();
                    param.setEmail(email);
                    param.setPassword(password);
                    HttpUtils.requestApi(HttpUtils.login, "POST", new Gson().toJson(param), 1, new HttpCallback() {
                        @Override
                        public void onSuccess(InputStream inputStream) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = HttpUtils.decodeAsString(inputStream);
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onFailure(InputStream inputStream) {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = HttpUtils.decodeAsString(inputStream);
                            mHandler.sendMessage(msg);
                        }
                    });
                }
            }
        });
    }
}