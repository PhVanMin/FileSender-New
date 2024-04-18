package com.example.xender.model;

import java.sql.Timestamp;

public class FileSend {



    public FileSend(int id, String fileName, String filePath, String receiveAddress, Timestamp time, boolean isSend) {
        this.id = id;
        this.fileName = fileName;
        this.time = time;
        this.filePath = filePath;
        this.receiveAddress = receiveAddress;
        this.isSend = isSend;
    }
    private int id;
    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    private boolean isSend ;
    public void setIsSend(boolean isSend){this.isSend = isSend;}
    public boolean getIsSend(){return isSend;}
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
