package com.example.fingertipsdemoapp;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fingertipsdemoapp.custom.LaTexTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static com.example.fingertipsdemoapp.MyApp.formateEscapeChar;


public class QuestionWebViewFragment extends Fragment {
    private QuizQuestion quizQuestion;
    private RecyclerView rv_question;
    private AnimationRecyclerAdapter<QuizQuestion.QuestionOption> recyclerAdapter;
    private int questionPosition;
    private LaTexTextView tv_qus_text;
    private ImageView img_question;
    ProgressBar progressBar;


    public static QuestionWebViewFragment newInstance(int position, QuizQuestion quizQuestion) {
        QuestionWebViewFragment teachearQuestionFragment = new QuestionWebViewFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("quizQuestion", quizQuestion);
        teachearQuestionFragment.setArguments(args);
        return teachearQuestionFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionPosition = getArguments().getInt("position", 0);
        quizQuestion = getArguments().getParcelable("quizQuestion");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_webview, container, false);
        WebView webView = view.findViewById(R.id.webview);
        progressBar=view.findViewById(R.id.progressBar);
        setui(webView);
        return view;
    }


    void setui(WebView webView) {
        final String question = formateEscapeChar(quizQuestion.getQuestion());
        //final String question = StringEscapeUtils.unescapeJava(question1);
        Log.e("Test question", "Questions 1: " + question);
        webView.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl("file:///android_asset/question.html");
        byte[] data = new byte[0];
        try {
            Charset utf8 = StandardCharsets.UTF_8;
            data = get().getBytes(utf8);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:displayQuestions('" + base64 + "')");
            }});
        webView.addJavascriptInterface(this,"jsObject");

    }

    @JavascriptInterface
    public  void onQuestionLoad(){
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
        //getView().post(() -> Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show());


    } @JavascriptInterface
    public  void mathjax_done(){
       // Toast.makeText(requireContext(), "dssone", Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public  void onQuestionError(){
        //Toast.makeText(requireContext(), "dssone", Toast.LENGTH_SHORT).show();
    }

   private String get() throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("question",quizQuestion.getQuestion());
        jsonObject.put("answer",quizQuestion.getAnswer());
        jsonObject.put("question_image",quizQuestion.getQuestionImage());
        jsonObject.put("questionExp",quizQuestion.getQuestionExplaination());
        jsonObject.put("questionExpImage",quizQuestion.getQuestionExplanationImage());

        JSONArray optionArray=new JSONArray();

        for (QuizQuestion.QuestionOption questionOption: quizQuestion.getOptions()) {
            JSONObject option = new JSONObject();
            option.put("option",questionOption.getOption());
            option.put("optionType",questionOption.getOptionType());
            optionArray.put(option);
        }
       jsonObject.put("options",optionArray);

        return jsonObject.toString();
    }


}
