package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ChapterModel {

    @SerializedName("chapter_name")
    String chapterName;
    @SerializedName("chapter_id")
    String chapterId;

    public ChapterModel() {
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    @NonNull
    @Override
    public String toString() {
        return chapterName;
    }
}
