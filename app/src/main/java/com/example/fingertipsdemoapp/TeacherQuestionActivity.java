package com.example.fingertipsdemoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fingertipsdemoapp.remote.APIUtil;
import com.example.fingertipsdemoapp.utils.Constant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherQuestionActivity extends BaseActivity {
    public static final String specialSplitChar = "%<math xmlns=";

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
    private String mSelectQuestionType="All";
    private String mSelectSource="All";
    private TextView tv_total_points;
    private boolean isReqProcessing = false;
    private int mtotalPages = 0;
    private boolean mIsNormalViewRendering;

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

    private static String removeInlineStyle(String text) {
        if (!TextUtils.isEmpty(text))
            return text.replaceAll("style=\"(.*).*[\"]", "");
        else
            return text;
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_teacher_qus;
    }

    private void reduceDragSensitivity(ViewPager2 viewPager2) {
        try {
            Field ff = ViewPager2.class.getDeclaredField("mRecyclerView");
            ff.setAccessible(true);
            RecyclerView recyclerView = (RecyclerView) ff.get(viewPager2);
            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
            touchSlopField.setAccessible(true);
            int touchSlop = (int) touchSlopField.get(recyclerView);
            Log.i("s", "reduceDragSensitivity: " + touchSlop);
            touchSlopField.set(recyclerView, touchSlop * 2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
        mIsNormalViewRendering = getIntent().getBooleanExtra("mIsNormalViewRendering", true);
        Bundle bundle = getIntent().getExtras();
        mSelectedStatus = bundle.getString("STATUS");
        mSelectChapter = bundle.getString("CHAPTER");
        if("-1".equals(mSelectChapter))
            mSelectChapter=null;
        mSelectQuestionType = bundle.getString("QUESTION_TYE");
        mSelectSource = bundle.getString("SOURCE");
        Log.e("testing", "xxxxx: " + mSelectedStatus);
        Log.e("testing", "onCreate: " + mSelectChapter+"SS:"+mSelectSource+"Qu Ty:"+mSelectQuestionType);
        viewPager_teacher_qus.setPageTransformer(new MarginPageTransformer(100));
        quizQuestionAdapter = new QuizQuestionAdapter(this, quslist);
        viewPager_teacher_qus.setOffscreenPageLimit(4);
        viewPager_teacher_qus.setAdapter(quizQuestionAdapter);
        reduceDragSensitivity(viewPager_teacher_qus);
        int question_id = getIntent().getIntExtra("question_id", -1);
        if (question_id == -1)
            fetchQuestion(1);
        else
            fetchQuestionVia(question_id);

        view_sw.setChecked(true);

        onCheckedChangeListener = (buttonView, isChecked) -> {
            int currentItem = viewPager_teacher_qus.getCurrentItem();
            if (currentItem < quizQuestions().size()) {
                QuizQuestion quizQuestion = quizQuestions().get(currentItem);
                quizQuestion.setNormalViewRendering(isChecked);
                quizQuestionAdapter.notifyItemChanged(currentItem);
            }
        };
        view_sw.setOnCheckedChangeListener(onCheckedChangeListener);

        View viewById = findViewById(R.id.ll_prog);
        viewById.setLongClickable(true);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quslist.isEmpty()) {
                    int size = viewPager_teacher_qus.getCurrentItem();
                    size = size + 20;
                    if (size < quslist.size())
                        viewPager_teacher_qus.setCurrentItem(size - 1, false);
                    else
                        viewPager_teacher_qus.setCurrentItem(quslist.size() - 1, false);
                }
            }
        });
        viewById.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!quslist.isEmpty()) {
                    int size = viewPager_teacher_qus.getCurrentItem();
                    size = size + 100;
                    if (size < quslist.size())
                        viewPager_teacher_qus.setCurrentItem(size - 1, false);
                    else
                        viewPager_teacher_qus.setCurrentItem(quslist.size() - 1, false);
                }
                return true;
            }
        });

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
                    if (TextUtils.isEmpty(quizQuestion.getSource()))
                        tv_earn_points.append(" - Old Que");
                    else
                        tv_earn_points.append(" - "+quizQuestion.getSource());

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
                t.printStackTrace();
                if (t instanceof JsonSyntaxException) {
                    Toast.makeText(TeacherQuestionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherQuestionActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void scrollQuestionPage(int pagePos) {
        viewPager_teacher_qus.setCurrentItem(pagePos, true);
    }

    private void fetchQuestion(int page) {
        isReqProcessing = true;
        Call<JsonObject> call = APIUtil.appConfig().getQuestion(mSelectChapter, mSelectedStatus,mSelectSource,mSelectQuestionType, page);
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
                        ArrayList<QuizQuestion> quizQuestions = passData(quArray);
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
                isReqProcessing = false;
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                t.printStackTrace();

                if (t instanceof JsonSyntaxException) {
                    Toast.makeText(TeacherQuestionActivity.this, "No data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherQuestionActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void parseQuestionAndAdd(ArrayList<QuizQuestion> quizQuestions, JsonElement element) {
        JsonObject objQns = element.getAsJsonObject();
        QuizQuestion qnsModel = new QuizQuestion();
        qnsModel.setId(Integer.parseInt(objQns.get("id").getAsString()));
        qnsModel.setPoint(0);
        String text_type = objQns.get("text_type").getAsString();
        boolean specialType = text_type.equalsIgnoreCase("special");
        qnsModel.setSpecialType(specialType);
        boolean expSpecialType = (objQns.get("text_type_explanation").isJsonNull() ? text_type :
                objQns.get("text_type_explanation").getAsString()).equalsIgnoreCase("special");
        qnsModel.setExpSpecialType(expSpecialType);
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
        qnsModel.setQuestionStatus(status);
        String question = objQns.get("question").getAsString();

        if (!specialType) {
            if (question.contains("$$")) {
                specialType = true;
                qnsModel.setSpecialType(specialType);
            }
        }

        String answer = objQns.get("answer").isJsonNull() ? "" : objQns.get("answer").getAsString();
        JsonElement sourcejsonElement = objQns.get("source");
        String source = sourcejsonElement == null || sourcejsonElement.isJsonNull() ? "" : sourcejsonElement.getAsString();
        boolean isOldQusType = !source.equalsIgnoreCase("External");
        qnsModel.setSource(source);

        if (!specialType && isOldQusType) {
            question = removeInlineStyle(question);
            opt_a = removeInlineStyle(opt_a);
            opt_b = removeInlineStyle(opt_b);
            opt_c = removeInlineStyle(opt_c);
            opt_d = removeInlineStyle(opt_d);
        }

        if (specialType || isOldQusType) {
            question = extractLatex(question);
            opt_a = extractLatex(opt_a);
            opt_b = extractLatex(opt_b);
            opt_c = extractLatex(opt_c);
            opt_d = extractLatex(opt_d);

        }
        String question_explaination = objQns.get("question_explaination").getAsString();
        if (!expSpecialType && isOldQusType) {
            question_explaination = removeInlineStyle(question_explaination);
        }
        if (expSpecialType || isOldQusType) {
            question_explaination = extractLatex(question_explaination);
        }


        options[0] = new QuizQuestion.QuestionOption(opt_a, opt_a_type);
        options[1] = new QuizQuestion.QuestionOption(opt_b, opt_b_type);
        options[2] = new QuizQuestion.QuestionOption(opt_c, opt_c_type);
        options[3] = new QuizQuestion.QuestionOption(opt_d, opt_d_type);
        qnsModel.setQuestion(question);
        qnsModel.setAnswer(answer);
        qnsModel.setOptions(options);

        qnsModel.setQuestionExplaination(question_explaination);
        String question_explanation_image = objQns.get("question_explanation_image").isJsonNull() ? "" : objQns.get("question_explanation_image").getAsString();
        qnsModel.setQuestionExplanationImage(question_explanation_image);
        qnsModel.setNormalViewRendering(mIsNormalViewRendering);
        JsonElement ques_image = objQns.get("ques_image");
        if (ques_image != null && !ques_image.isJsonNull())
            qnsModel.setQuestionImage(ques_image.getAsString());
        quizQuestions.add(qnsModel);
    }

    private void fetchQuestionVia(int questionId) {
        mIsNormalViewRendering = false;
        JsonObject jsonObject = new JsonObject();
        JsonArray value = new JsonArray();
        value.add(questionId);
        jsonObject.add("question_id", value);
        ConfigURLs configURLs = APIUtil.appConfig();
        configURLs.getDataCorrespondingQuestionID(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                isReqProcessing = false;
                if (response.isSuccessful() && jsonObject != null) {
                    JsonArray quArray = jsonObject.getAsJsonArray("data");
                    if (jsonObject != null) {
                        ArrayList<QuizQuestion> quizQuestions = passData(quArray);
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
                isReqProcessing = false;
                t.printStackTrace();
                if (t instanceof JsonSyntaxException) {
                    Toast.makeText(TeacherQuestionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherQuestionActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.progressBar).setVisibility(View.GONE);

            }
        });


    }

    private ArrayList<QuizQuestion> passData(JsonArray quArray) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
        for (JsonElement element : quArray) {
            parseQuestionAndAdd(quizQuestions, element);
        }
        return quizQuestions;
    }

    public ArrayList<QuizQuestion> quizQuestions() {
        return quslist;
    }

    private String extractLatex(String s) {
        if (!TextUtils.isEmpty(s)) {
            String[] ss = s.split(specialSplitChar);
            Log.i("d", "extractLatex: " + ss.length);
            if (ss.length > 1) {
                String question = ss[1];
                if (!TextUtils.isEmpty(question))
                   // return "$$" + question + "$$";
                return "<math xmlns=" + question + "";
                else
                    return "";
            }
        }
        return s;
    }


}
