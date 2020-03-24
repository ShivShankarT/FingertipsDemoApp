package com.example.fingertipsdemoapp;

import com.google.gson.annotations.SerializedName;

public class UpdateRespose {

    @SerializedName("question_id")
    public String questionId;

    @SerializedName("status")
    public String status;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
