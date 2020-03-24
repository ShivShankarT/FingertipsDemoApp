package com.example.fingertipsdemoapp;

import com.google.gson.annotations.SerializedName;

class ApiResponse {

    @SerializedName("question_id")
    private String questionId;

    @SerializedName("status")
    private String questonStatus;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestonStatus() {
        return questonStatus;
    }

    public void setQuestonStatus(String questonStatus) {
        this.questonStatus = questonStatus;
    }
}
