package com.example.xender.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class FileCloud {
    int id;
    String name;
    String uri;

    Timestamp time;

    public FileCloud(int id, String name, String uri, Timestamp time) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.time = time;
    }

    public FileCloud() {
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

    public Timestamp getTime() {
        return time;
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

    public void setTime(Date date) {
        this.time = time;
    }
}
