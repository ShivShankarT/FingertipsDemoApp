package com.example.fingertipsdemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fingertipsdemoapp.remote.APIUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SpinnerActivity extends AppCompatActivity {
    Button searchButton;

    List<ClassModel> classModels = new ArrayList<>();
    List<SubjectModel> subjectModels = new ArrayList<>();
    List<ChapterModel> chapterModels=new ArrayList<>();

    ArrayAdapter<ClassModel> classArrayOdopter;
    ArrayAdapter<SubjectModel> subjectArrayAdopter;
    ArrayAdapter<ChapterModel> chapterArrayAdopter;
    private Spinner spinnerClass, spinnerSubject, spinnerChapter, spinnerAnsStatus;

    String mSelectChapter;
    String mSelelectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        spinnerClass = findViewById(R.id.classes_spinner);
        spinnerSubject = findViewById(R.id.subjects_spinner);
        spinnerChapter = findViewById(R.id.chapters_spinner);
        spinnerAnsStatus = findViewById(R.id.pending_ans_status_spinner);
        searchButton = findViewById(R.id.button_search_Id);


        classArrayOdopter = new ArrayAdapter<ClassModel>(SpinnerActivity.this, android.R.layout.simple_spinner_item, classModels);
        Log.i("jgggu", "onCreate: ");
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               ClassModel data= (ClassModel) parent.getItemAtPosition(position);
                addItemOnSpinnerSubject(data.getClassId());
                subjectModels.clear();
                subjectArrayAdopter.notifyDataSetChanged();
                chapterModels.clear();
                chapterArrayAdopter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classArrayOdopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classArrayOdopter);



        subjectArrayAdopter = new ArrayAdapter<SubjectModel>(SpinnerActivity.this,
                android.R.layout.simple_spinner_item, subjectModels);
         spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubjectModel data= (SubjectModel) parent.getItemAtPosition(position);
                addItemOnSpinnerChapter(data.getSubjectId());
                chapterModels.clear();
                chapterArrayAdopter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        subjectArrayAdopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectArrayAdopter);


        chapterArrayAdopter=new ArrayAdapter<ChapterModel>(SpinnerActivity.this,android.R.layout.simple_spinner_dropdown_item,chapterModels);
        spinnerChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChapterModel dataChapter=(ChapterModel) parent.getItemAtPosition(position);
                 mSelectChapter=dataChapter.chapterId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        chapterArrayAdopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChapter.setAdapter(chapterArrayAdopter);

        spinnerAnsStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelelectStatus= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addItemsOnspinnerClass();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectChapter!=null) {

                    Intent intent = new Intent(SpinnerActivity.this, TeacherQuestionActivity.class);
                    intent.putExtra("STATUS", mSelelectStatus);
                    intent.putExtra("CHAPTER", mSelectChapter);

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SpinnerActivity.this, "Please Select Chapter.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            addItemOnSpinnerQuestionStatus();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause() + "here is the reason");
        }

    }

    private void addItemOnSpinnerChapter(String chapterId) {
        Call<ListOfChapterModel> call=APIUtil.appConfig().getAllChapterBySubj(chapterId);
        call.enqueue(new Callback<ListOfChapterModel>() {
            @Override
            public void onResponse(Call<ListOfChapterModel> call, Response<ListOfChapterModel> response) {
                List<ChapterModel> subjectModels = response.body().chapterModelList;
                SpinnerActivity.this.chapterModels.clear();
                SpinnerActivity.this.chapterModels.addAll(subjectModels);
                chapterArrayAdopter.notifyDataSetChanged();
                spinnerChapter.setSelection(0);
            }
            @Override
            public void onFailure(Call<ListOfChapterModel> call, Throwable t) {
            }
        });
    }
    private void addItemOnSpinnerSubject(String classId) {
        Call<ListOfSubjectModel> call = APIUtil.appConfig().getAllSubject(classId);
        call.enqueue(new Callback<ListOfSubjectModel>() {
            @Override
            public void onResponse(Call<ListOfSubjectModel> call, Response<ListOfSubjectModel> response) {
                List<SubjectModel> subjectModels = response.body().getSubjectModelList();
                SpinnerActivity.this.subjectModels.clear();
                SpinnerActivity.this.subjectModels.addAll(subjectModels);
                subjectArrayAdopter.notifyDataSetChanged();
                spinnerSubject.setSelection(0);
            }

            @Override
            public void onFailure(Call<ListOfSubjectModel> call, Throwable t) {

            }
        });

    }
    private void addItemOnSpinnerQuestionStatus() {
        List<String> chapterList = new ArrayList<String>();
        chapterList.add("All");
        chapterList.add("Pending");
        chapterList.add("Approved");
        chapterList.add("Rejected");

        ArrayAdapter<String> qustinonSatusAdopter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, chapterList);
        qustinonSatusAdopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnsStatus.setAdapter(qustinonSatusAdopter);

    }
    private void addItemsOnspinnerClass() {

        Call<ListOfClassModel> call = APIUtil.appConfig().getAllClasses();
        call.enqueue(new Callback<ListOfClassModel>() {
            @Override
            public void onResponse(Call<ListOfClassModel> call, Response<ListOfClassModel> response) {

                List<ClassModel> classModels = response.body().getClassModelList();
                SpinnerActivity.this.classModels.addAll(classModels);
                classArrayOdopter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ListOfClassModel> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }

}
