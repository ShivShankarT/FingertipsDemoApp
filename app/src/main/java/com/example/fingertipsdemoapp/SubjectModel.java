package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SubjectModel {

    @SerializedName("subject")
    String subjectName;
    @SerializedName("subject_id")
    String subjectId;

    public SubjectModel() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @NonNull
    @Override
    public String toString() {
        return subjectName;
    }
}
