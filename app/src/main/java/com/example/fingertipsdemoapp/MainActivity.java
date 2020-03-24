package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fingertipsdemoapp.remote.APIUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.fingertipsdemoapp.TeacherQuestionActivity.optionAnsPos;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    public  AwesomePagerAdapter awesomeAdapter;
    QuizQuestionAdapter quizQuestionAdapter;
    private ImageView iv_close,iv_correct;
    private TextView tv_total_points,tv_qus_no,tv_earn_points,tvTime;
    ProgressBar pogress_bar;
    private RelativeLayout rl_report;
    private int currentPage = 0;

    private String mSelectChapter;
    private String mSelectedStatus;
    private int mtotalPages = 0;
    FloatingActionButton acceptedButton;
    FloatingActionButton rejectedButton;
    private boolean isReqProcessing = false;
    private ArrayList<QuizQuestion> quslist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 =  findViewById(R.id.pager);



        acceptedButton=findViewById(R.id.acceptFab);
        rejectedButton=findViewById(R.id.rejectFab);
        tv_total_points = (TextView) findViewById(R.id.tv_total_points);
        tv_qus_no = (TextView) findViewById(R.id.tv_qus_no);
        tv_earn_points = (TextView) findViewById(R.id.tv_earn_points);
        pogress_bar = findViewById(R.id.pogress_bar);
        tvTime = (TextView) findViewById(R.id.tv_time);
        iv_correct = (ImageView) findViewById(R.id.iv_correct);

        Bundle bundle = getIntent().getExtras();
        mSelectedStatus = bundle.getString("STATUS");
        mSelectChapter = bundle.getString("CHAPTER");
        Log.e("testing", "xxxxx: " + mSelectedStatus);
        Log.e("testing", "onCreate: " + mSelectChapter);

        /*List<QuestionModel> questionModels=getListQuestionModel();
        awesomeAdapter = new AwesomePagerAdapter(this,viewPager2,questionModels);
        viewPager2.setAdapter(awesomeAdapter);
*/
        awesomeAdapter = new AwesomePagerAdapter(this,viewPager2,quslist);
        viewPager2.setAdapter(awesomeAdapter);

      /*  quizQuestionAdapter = new QuizQuestionAdapter(this, quslist);
        viewPager2.setAdapter(quizQuestionAdapter);
        fetchQuestion(1);*/

        fetchQuestion(1);

    }

    @JavascriptInterface
    public void fetchQuestion(int page) {
        isReqProcessing = true;
        Call<JsonObject> call = APIUtil.appConfig().getQuestion(mSelectChapter, mSelectedStatus, page);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                isReqProcessing = false;
                if (response.isSuccessful() && jsonObject != null) {
                    currentPage = page;
                    JsonObject questions = jsonObject.getAsJsonObject("data");
                    JsonObject pageObject = questions.getAsJsonObject("page");
                    if (pageObject != null) {
                        //cur_page: "2",
                        //total_records: "485",
                        //total: 20,
                        //total_pages: 25,
                        String total_records = pageObject.get("total_records").getAsString();
                        mtotalPages = pageObject.get("total_pages").getAsInt();
                        tvTime.setText(total_records);
                    }

                    if (questions != null) {
                        JsonArray quArray = questions.getAsJsonArray("result");
                        ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
                        for (JsonElement element : quArray) {
                            JsonObject objQns = element.getAsJsonObject();
                            QuizQuestion qnsModel = new QuizQuestion();
                            qnsModel.setId(Integer.parseInt(objQns.get("id").getAsString()));
                            qnsModel.setPoint(0);
                            qnsModel.setSpecialType(objQns.get("text_type").getAsString().equalsIgnoreCase("special"));
                            QuizQuestion.QuestionOption[] options = new QuizQuestion.QuestionOption[4];

                            String opt_a = objQns.get("opt_a").isJsonNull() ? "" : objQns.get("opt_a").getAsString();
                            String opt_a_type = objQns.get("opt_a_type").isJsonNull() ? "" : objQns.get("opt_a_type").getAsString();

                            String opt_b = objQns.get("opt_b").isJsonNull() ? "" : objQns.get("opt_b").getAsString();
                            String opt_b_type = objQns.get("opt_b_type").isJsonNull() ? "" : objQns.get("opt_b_type").getAsString();

                            String opt_c = objQns.get("opt_b").isJsonNull() ? "" : objQns.get("opt_c").getAsString();
                            String opt_c_type = objQns.get("opt_c_type").isJsonNull() ? "" : objQns.get("opt_c_type").getAsString();

                            String opt_d = objQns.get("opt_d").isJsonNull() ? "" : objQns.get("opt_d").getAsString();
                            String opt_d_type = objQns.get("opt_d_type").isJsonNull() ? "" : objQns.get("opt_d_type").getAsString();

                            String status = objQns.get("status").isJsonNull() ? "accepted" : objQns.get("status").getAsString();

                            options[0] = new QuizQuestion.QuestionOption(opt_a, opt_a_type);
                            options[1] = new QuizQuestion.QuestionOption(opt_b, opt_b_type);
                            options[2] = new QuizQuestion.QuestionOption(opt_c, opt_c_type);
                            options[3] = new QuizQuestion.QuestionOption(opt_d, opt_d_type);
                            qnsModel.setOptions(options);
                            qnsModel.setQuestionStatus(status);
                            qnsModel.setQuestion(objQns.get("question").getAsString());
                            qnsModel.setQuestionExplaination(objQns.get("question_explaination").getAsString());
                            qnsModel.setQuestionExplanationImage(objQns.get("question_explanation_image").getAsString());
                            JsonElement ques_image = objQns.get("ques_image");
                            if (ques_image != null && !ques_image.isJsonNull())
                                qnsModel.setQuestionImage(ques_image.getAsString());

                            qnsModel.setSelectedOptionPos(optionAnsPos(objQns.get("answer").getAsString()));
                            quizQuestions.add(qnsModel);

                        }
                        quslist.addAll(quizQuestions);
                      //  quizQuestionAdapter.notifyDataSetChanged();
                        tv_qus_no.setText("Question:- " + (viewPager2.getCurrentItem() + 1) + "/" + quslist.size());

                    } else {
                        JsonElement message = jsonObject.get("response_message");
                        if (message != null) {
                            String resmessage = message.getAsString();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


/*

    @JavascriptInterface
    public List<QuestionModel> getListQuestionModel(){

        List<QuestionModel> questionModelList=new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("ques");
            Log.i("response :",m_jArry.toString());
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                final String question=jo_inside.getString("question");
                final String question_image=jo_inside.getString("question_image");
                final String optionA = jo_inside.getString("choiceA");
                final String optionA_pic=jo_inside.getString("choiceImageA");
                final int isOptionAisCorrect=jo_inside.getInt("is_rightA");
                final String optionB= jo_inside.getString("choiceB");
                final String optionB_pic=jo_inside.getString("choiceImageB");
                final int isOptionBisCorrect=jo_inside.getInt("is_rightB");
                final String optionC= jo_inside.getString("choiceC");
                final String optionC_pic=jo_inside.getString("choiceImageC");
                final int isOptionCisCorrect=jo_inside.getInt("is_rightC");
                final String optionD =jo_inside.getString("choiceD");
                final String optionD_pic=jo_inside.getString("choiceImageD");
                final int isOptionDisCorrect=jo_inside.getInt("is_rightD");
                final String optionE =jo_inside.getString("choiceE");
                final String optionE_pic=jo_inside.getString("choiceImageE");
                final int isOptionEisCorrect=jo_inside.getInt("is_rightE");

                QuestionModel model=new QuestionModel();
                model.setQuestion(question);
                model.setQuestin_pic(question_image);
                model.setObtion_a(optionA);
                model.setObtion_a_pic(optionA_pic);
                model.setObtion_b(optionB);
                model.setObtion_b_pic(optionB_pic);
                model.setObtion_c(optionC);
                model.setObtion_b_pic(optionC_pic);
                model.setObtion_d(optionD);
                model.setObtion_b_pic(optionD_pic);
                model.setObtion_e(optionE);
                model.setObtion_b_pic(optionE_pic);
                questionModelList.add(model);
                model.setIsAisCorrect(isOptionAisCorrect);
                model.setIsBisCorrect(isOptionBisCorrect);
                model.setIsCisCorrect(isOptionCisCorrect);
                model.setIsDisCorrect(isOptionDisCorrect);
                model.setIsEisCorrect(isOptionEisCorrect);

                Log.e("whole_model", "getListQuestionModel: "+model );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  questionModelList;
    }
*/

}

