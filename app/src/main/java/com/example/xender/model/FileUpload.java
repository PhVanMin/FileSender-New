package com.example.xender.model;

import java.sql.Date;

public class FileUpload {
    int id;
    String name;
    String uri;

    Date date;

    public FileUpload(int id, String name, String uri, Date date) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.date = date;
    }

    public FileUpload() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
