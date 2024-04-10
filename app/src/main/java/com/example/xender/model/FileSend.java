package com.example.xender.model;

import java.sql.Timestamp;

public class FileSend {
    private String fileName;
    private Timestamp time;
    private String filePath;

    private String ReceiveAddress;

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
        return ReceiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        ReceiveAddress = receiveAddress;
    }
}
