package com.example.xender.model;

import java.sql.Timestamp;

public class FileSend {

    public FileSend() {
    }

    public FileSend(String fileName, Timestamp time, String filePath, String receiveAddress, boolean isSend) {
        this.fileName = fileName;
        this.time = time;
        this.filePath = filePath;
        this.receiveAddress = receiveAddress;
        this.isSend = isSend;
    }

    private boolean isSend ;

    private String fileName;
    private Timestamp time;
    private String filePath;

    private String receiveAddress;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
