package com.example.fingertipsdemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    public  AwesomePagerAdapter awesomeAdapter;
    private ImageView iv_close,iv_correct;
    private TextView tv_total_points,tv_qus_no,tv_earn_points,tvTime;
    ProgressBar pogress_bar;
    private RelativeLayout rl_report;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 =  findViewById(R.id.pager);

        tv_total_points = (TextView) findViewById(R.id.tv_total_points);
        tv_qus_no = (TextView) findViewById(R.id.tv_qus_no);
        tv_earn_points = (TextView) findViewById(R.id.tv_earn_points);
        pogress_bar = findViewById(R.id.pogress_bar);
        tvTime = (TextView) findViewById(R.id.tv_time);
        iv_correct = (ImageView) findViewById(R.id.iv_correct);
        List<QuestionModel> questionModels=getListQuestionModel();
        awesomeAdapter = new AwesomePagerAdapter(this,viewPager2,questionModels);
        viewPager2.setAdapter(awesomeAdapter);
           }



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
                final String optionB= jo_inside.getString("choiceB");
                final String optionB_pic=jo_inside.getString("choiceImageB");
                final String optionC= jo_inside.getString("choiceC");
                final String optionC_pic=jo_inside.getString("choiceImageC");
                final String optionD =jo_inside.getString("choiceD");
                final String optionD_pic=jo_inside.getString("choiceImageD");
                final String optionE =jo_inside.getString("choiceE");
                final String optionE_pic=jo_inside.getString("choiceImageE");

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

                Log.e("whole_model", "getListQuestionModel: "+model );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  questionModelList;
    }


        private String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is=getAssets().open("questios.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                Log.e("hhh", "loadJSONFromAsset: ",ex );
                ex.printStackTrace();
                return null;
            }
            Log.e("here is the json", "loadJSONFromAsset: "+json );
            return json;
        }

}

