package com.example.fingertipsdemoapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfChapterModel {
    @SerializedName("data")
    List<ChapterModel> chapterModelList;

    public List<ChapterModel> getChapterModelList() {
        return chapterModelList;
    }

    public void setChapterModelList(List<ChapterModel> chapterModelList) {
        this.chapterModelList = chapterModelList;
    }
}
