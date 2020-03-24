package com.example.fingertipsdemoapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfClassModel {

    @SerializedName("data")
    List<ClassModel> classModelList;

    public List<ClassModel> getClassModelList() {
        return classModelList;
    }

    public void setClassModelList(List<ClassModel> classModelList) {
        this.classModelList = classModelList;
    }
}
