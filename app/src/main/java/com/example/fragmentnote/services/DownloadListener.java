package com.example.fragmentnote.services;

public interface DownloadListener {
    void onSuccess();
    void onFailure();
    void showProgress(int progress);
    void onCancel();
    void onStop(int DownloadLength);
}
