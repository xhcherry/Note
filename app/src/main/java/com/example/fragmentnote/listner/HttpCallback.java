package com.example.fragmentnote.listner;

import java.io.InputStream;

public interface HttpCallback {
    void onSuccess(InputStream inputStream);
    void onFailure(InputStream inputStream);
}
