package com.example.fingertipsdemoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fingertipsdemoapp.remote.APIUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherQuestionActivity extends BaseActivity {

    TextView tv_earn_points;
    FloatingActionButton acceptButton, rejectButton;
    QuizQuestionAdapter quizQuestionAdapter;
    ImageView imageView;
    SwitchCompat view_sw;
    CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private int currentPage = 0;
    private ViewPager2 viewPager_teacher_qus;
    private RelativeLayout rl_points_earn;
    private int maxTime;
    private TextView tvTime, tv_qus_no;
    private Runnable Update;
    private ArrayList<QuizQuestion> quslist = new ArrayList<>();
    private String mSelectedStatus;
    private String mSelectChapter;
    private TextView tv_total_points;
    private boolean isReqProcessing = false;
    private int mtotalPages = 0;
    private  boolean mIsNormalViewRendering;

    public static int optionAnsPos(String ans) {

        switch (ans) {

            case "A":
                return 0;

            case "B":
                return 1;

            case "C":
                return 2;

            case "D":
                return 3;

            default:
                return -1;

        }

    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_teacher_qus;

        /*  intent.putExtra("STATUS",mSelelectStatus);
                intent.putExtra("CHAPTER",mSelectChapter);*/
    }

    @Override
    public void initialize() {
        viewPager_teacher_qus = findViewById(R.id.viewPager_teacher_qus);
        View pogress_bar = findViewById(R.id.pogress_bar);
        tvTime = findViewById(R.id.tv_time);
        imageView = findViewById(R.id.iv_close);
        tv_qus_no = findViewById(R.id.tv_qus_no);
        rl_points_earn = findViewById(R.id.rl_points_earn);
        tv_total_points = findViewById(R.id.tv_total_points);
        tv_earn_points = findViewById(R.id.tv_earn_points);
        acceptButton = findViewById(R.id.acceptFab);
        rejectButton = findViewById(R.id.rejectFab);
        view_sw = findViewById(R.id.view_sw);
        mIsNormalViewRendering=getIntent().getBooleanExtra("mIsNormalViewRendering",true);
        Bundle bundle = getIntent().getExtras();
        mSelectedStatus = bundle.getString("STATUS");
        mSelectChapter = bundle.getString("CHAPTER");
        Log.e("testing", "xxxxx: " + mSelectedStatus);
        Log.e("testing", "onCreate: " + mSelectChapter);
        viewPager_teacher_qus.setPageTransformer(new MarginPageTransformer(100));
        quizQuestionAdapter = new QuizQuestionAdapter(this, quslist);
        viewPager_teacher_qus.setOffscreenPageLimit(4);
        viewPager_teacher_qus.setAdapter(quizQuestionAdapter);
        fetchQuestion(1);
        view_sw.setChecked(true);

        onCheckedChangeListener = (buttonView, isChecked) -> {
            int currentItem = viewPager_teacher_qus.getCurrentItem();
            QuizQuestion quizQuestion = quizQuestions().get(currentItem);
            quizQuestion.setNormalViewRendering(isChecked);
            quizQuestionAdapter.notifyItemChanged(currentItem);
        };
        view_sw.setOnCheckedChangeListener(onCheckedChangeListener);


    }

    @Override
    public void init(Bundle save) {

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentReq(true, viewPager_teacher_qus.getCurrentItem());
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentReq(false, viewPager_teacher_qus.getCurrentItem());

            }
        });

        viewPager_teacher_qus.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                if (pos < quslist.size()) {
                    tv_qus_no.setText("Question " + (pos + 1) + "/" + quslist.size());

                    QuizQuestion quizQuestion = quizQuestions().get(pos);
                    String questionStatus = String.valueOf(quizQuestion.getQuestionStatus());

                    if (questionStatus.equals("1")) {
                        tv_earn_points.setText("Pending");
                        rejectButton.setVisibility(View.VISIBLE);
                        acceptButton.setVisibility(View.VISIBLE);
                    } else if (questionStatus.equals("accepted")) {
                        tv_earn_points.setText("Approved");
                        rejectButton.setVisibility(View.INVISIBLE);
                        acceptButton.setVisibility(View.INVISIBLE);
                    } else if (questionStatus.equals("2")) {
                        tv_earn_points.setText("Rejected");
                        rejectButton.setVisibility(View.INVISIBLE);
                        acceptButton.setVisibility(View.INVISIBLE);
                    }

                    String id = String.valueOf(quslist.get(pos).getId());
                    tv_total_points.setText(id);

                    int total = quslist.size();

                    if (pos >= (total - 15) && !isReqProcessing && currentPage < mtotalPages) {
                        fetchQuestion(currentPage + 1);
                    }

                    view_sw.setOnCheckedChangeListener(null);
                    view_sw.setChecked(quizQuestion.isNormalViewRendering());
                    view_sw.setOnCheckedChangeListener(onCheckedChangeListener);


                }
            }
        });


    }

    private void sentReq(boolean isAccept, int pos) {
        ConfigURLs configURLs = APIUtil.appConfig();
        QuizQuestion quizQuestion = quizQuestions().get(pos);
        configURLs.getAllresponseAccQuestionIdAndStatus(String.valueOf(quizQuestion.getId()),
                isAccept ? "approve" : "reject").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    if (isAccept) {
                        quizQuestion.setQuestionStatus("accepted");
                        Toast.makeText(TeacherQuestionActivity.this, " Question Accepted ", Toast.LENGTH_SHORT).show();
                    } else {
                        quizQuestion.setQuestionStatus("2");
                        Toast.makeText(TeacherQuestionActivity.this, " Question Rejected ", Toast.LENGTH_SHORT).show();
                    }

                    int nextPage = viewPager_teacher_qus.getCurrentItem() + 1;
                    quizQuestionAdapter.notifyDataSetChanged();
                    if (nextPage < quizQuestions().size())
                        scrollQuestionPage(nextPage);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }


    public void scrollQuestionPage(int pagePos) {
        viewPager_teacher_qus.setCurrentItem(pagePos, true);
    }

    private void fetchQuestion(int page) {
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
                            qnsModel.setExpSpecialType(objQns.get("text_type_explanation").getAsString().equalsIgnoreCase("special"));
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
                            qnsModel.setAnswer(objQns.get("answer").getAsString());
                            qnsModel.setQuestionExplaination(objQns.get("question_explaination").getAsString());
                            qnsModel.setQuestionExplanationImage(objQns.get("question_explanation_image").getAsString());
                            qnsModel.setNormalViewRendering(mIsNormalViewRendering);
                            JsonElement ques_image = objQns.get("ques_image");
                            if (ques_image != null && !ques_image.isJsonNull())
                                qnsModel.setQuestionImage(ques_image.getAsString());

                            qnsModel.setSelectedOptionPos(optionAnsPos(objQns.get("answer").getAsString()));
                            quizQuestions.add(qnsModel);

                        }
                        quslist.addAll(quizQuestions);
                        quizQuestionAdapter.notifyDataSetChanged();
                        tv_qus_no.setText("Question:- " + (viewPager_teacher_qus.getCurrentItem() + 1) + "/" + quslist.size());

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


    public ArrayList<QuizQuestion> quizQuestions() {

        return quslist;
    }


}
