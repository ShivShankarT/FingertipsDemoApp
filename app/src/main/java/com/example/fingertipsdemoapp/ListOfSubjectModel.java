package com.example.fingertipsdemoapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfSubjectModel {
    @SerializedName("data")

    List<SubjectModel> subjectModelList;

    public List<SubjectModel> getSubjectModelList() {
        return subjectModelList;
    }

    public void setSubjectModelList(List<SubjectModel> subjectModelList) {
        this.subjectModelList = subjectModelList;
    }
}
