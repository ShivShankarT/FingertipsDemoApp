package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ClassModel {
    @SerializedName("class")
    String className;
    @NonNull
    @SerializedName("class_id")
    String classId;

    public ClassModel() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return  className ;
    }
}
