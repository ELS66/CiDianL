package com.example.cidianl;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Word  {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String english;
    private String chinese;
    private int studystart = 0;
    private int studyend = 0;
    private boolean isstudy = true;
    private String date;
    private boolean islike = false;


    public Word(String english, String chinese) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.english = english;
        this.chinese = chinese;
        date = format.format(new Date());
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public int getStudystart() {
        return studystart;
    }

    public void setStudystart(int studystart) {
        this.studystart = studystart;
    }

    public int getStudyend() {
        return studyend;
    }

    public void setStudyend(int studyend) {
        this.studyend = studyend;
    }

    public boolean isIsstudy() {
        return isstudy;
    }

    public void setIsstudy(boolean isstudy) {
        this.isstudy = isstudy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIslike() {
        return islike;
    }

    public void setIslike(boolean islike) {
        this.islike = islike;
    }
}

