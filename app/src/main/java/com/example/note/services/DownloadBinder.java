package com.example.note.services;

import android.os.Binder;

public class DownloadBinder extends Binder {
    private DownloadServices service;

    public DownloadBinder(DownloadServices service) {
        this.service = service;
    }
    public void setListener(DownloadListener listener) {
        this.service.setListener(listener);
    }

    public void start(){
        service.start();
    }
    public void stop(){
        service.stop();
    }
    public void cancel(){
        service.cancel();
    }

}
