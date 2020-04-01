package com.example.fingertipsdemoapp;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConfigURLs {

    @GET("new-questions/classes")
    Call<ListOfClassModel> getAllClasses();

    @GET("new-questions/subjects")
    Call<ListOfSubjectModel> getAllSubject(
            @Query("class_id") String classId
    );

    @GET("new-questions/chapters")
    Call<ListOfChapterModel> getAllChapterBySubj(
            @Query("subject_id") String sub_Id
    );

    //status
    @GET("new-questions")
    Call<JsonObject> getQuestion(@Query("chapter_id")  String  chapterId, @Query("status")  String  status,@Query("page") int page);


    @FormUrlEncoded
    @POST("new-questions/update")
    Call<JsonObject> getAllresponseAccQuestionIdAndStatus(@Field("question_id") String userId, @Field("status") String status);

    @POST("new-questions/show")
    Call<QuestionID> getDataCorrespondingQuestionID(@Body QuestionID questionId);

}