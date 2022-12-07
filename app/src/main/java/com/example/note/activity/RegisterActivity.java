package com.example.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.R;
import com.example.note.listener.HttpCallback;
import com.example.note.util.HttpUtils;
import com.example.note.vo.RegisterParam;
import com.example.note.vo.ResponseListData;
import com.google.gson.Gson;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdt;
    private EditText passwordEdt;
    private EditText repasswordEdt;
    private EditText nicknameEdt;
    private Button submitBtn;
    private Handler mHandler;

    @SuppressLint({"MissingInflatedId", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", emailEdt.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {//请求服务器资源失败
                    Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        emailEdt = findViewById(R.id.register_email);
        passwordEdt = findViewById(R.id.register_password);
        repasswordEdt = findViewById(R.id.register_repassword);
        nicknameEdt = findViewById(R.id.register_nickname);
        submitBtn = findViewById(R.id.register_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                String repassword = repasswordEdt.getText().toString();
                String nickname = nicknameEdt.getText().toString();
                if (email == null || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (password == null || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!repassword.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    RegisterParam param = new RegisterParam();
                    param.setEmail(email);
                    param.setNickname(nickname);
                    param.setPassword(password);
                    HttpUtils.requestApi(HttpUtils.regsit, "POST", new Gson().toJson(param), 1, new HttpCallback() {
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