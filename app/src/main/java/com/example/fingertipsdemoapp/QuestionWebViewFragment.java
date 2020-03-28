package com.example.fingertipsdemoapp;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fingertipsdemoapp.custom.LaTexTextView;

import static com.example.fingertipsdemoapp.MyApp.formateEscapeChar;


public class QuestionWebViewFragment extends Fragment {
    private QuizQuestion quizQuestion;
    private RecyclerView rv_question;
    private AnimationRecyclerAdapter<QuizQuestion.QuestionOption> recyclerAdapter;
    private int questionPosition;
    private LaTexTextView tv_qus_text;
    private ImageView img_question;


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
                setui(webView);
        return view;
    }


    void setui(WebView webView) {
        final String question = formateEscapeChar(quizQuestion.getQuestion());
        //final String question = StringEscapeUtils.unescapeJava(question1);
        Log.e("Test question", "Questions 1: " + question);
        webView.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String questionImageUrl;
                if (quizQuestion.getQuestionImage().equals("")) {
                    questionImageUrl = "document.getElementById('question_pic').remove();";
                } else {
                    questionImageUrl = "document.getElementById('question_pic').src = '" + quizQuestion.getQuestionImage() + "';";
                }

                QuizQuestion.QuestionOption[] options = quizQuestion.getOptions();

                String ansUrlA;
                if (options[0].getOptionType().equals("TEXT")) {
                    ansUrlA = "document.getElementById('ans_a_pic').remove();";
                } else {
                    ansUrlA = "document.getElementById('ans_a_pic').src = '" + options[0].getOptionForwebview() + "';";
                }

                String ansUrlB;
                if (options[1].getOptionType().equals("TEXT")) {
                    ansUrlB = "document.getElementById('ans_b_pic').remove();";
                } else {
                    ansUrlB = "document.getElementById('ans_b_pic').src = '" + options[1].getOptionForwebview() + "';";
                }


                String ansUrlC;
                if (options[2].getOptionType().equals("TEXT")) {
                    ansUrlC = "document.getElementById('ans_c_pic').remove();";
                } else {
                    ansUrlC = "document.getElementById('ans_c_pic').src = '" + options[2].getOptionForwebview() + "';";
                }


                String ansUrlD;
                if (options[3].getOptionType().equals("TEXT")) {
                    ansUrlD = "document.getElementById('ans_d_pic').remove();";
                } else {
                    ansUrlD = "document.getElementById('ans_d_pic').src = '" + options[3].getOptionForwebview() + "';";
                }


                String imgUrlans_questionExplanation;
                if (quizQuestion.getQuestionExplanationImage() == null || quizQuestion.getQuestionExplanationImage().equals("")) {
                    imgUrlans_questionExplanation = "document.getElementById('ans_explanation_pic').remove();";
                } else {
                    imgUrlans_questionExplanation = "document.getElementById('ans_explanation_pic').src = '" + quizQuestion.getQuestionExplanationImage() + "';";
                }

                String optionA = "";
                String red = "#00FF00";
                String value = "document.getElementById('answerA').style.borderColor = '" + red + "';";

                String whitebackColor = "#FFFFFF";
                String valueABagColor = "document.getElementById('answerA').style.backgroundColor = '" + whitebackColor + "';";
                String innerCA = "document.getElementById('innerCircleA').style.backgroundColor = '" + whitebackColor + "';";

                if (quizQuestion.getAnswer().equals("A")) {
                    if (options[0].getOptionType().equals("TEXT")) {
                        optionA = "document.getElementById('opt_a').innerHTML = '" + options[0].getOptionForwebview() + "';";
                        // value="document.getElementById('answerC').style.borderColor = #FF0000'"+"';";
                        optionA = optionA + value + valueABagColor + innerCA;
                    }
                } else {

                    if (options[0].getOptionType().equals("TEXT")) {
                        optionA = "document.getElementById('opt_a').innerHTML = '" + options[0].getOptionForwebview() + "';";
                    }
                }

                String optionB = "";
                String innerCB = "document.getElementById('innerCircleB').style.backgroundColor = '" + whitebackColor + "';";
                String valueBBagColor = "document.getElementById('answerB').style.backgroundColor = '" + whitebackColor + "';";
                String valueB = "document.getElementById('answerB').style.borderColor = '" + red + "';";
                if (quizQuestion.getAnswer().equals("B")) {
                    if (options[0].getOptionType().equals("TEXT")) {
                        optionB = "document.getElementById('opt_b').innerHTML = '" + options[1].getOptionForwebview() + "';";
                        optionB = optionB + valueB + valueBBagColor + innerCB;
                    }
                } else {
                    if (options[0].getOptionType().equals("TEXT")) {
                        optionB = "document.getElementById('opt_b').innerHTML = '" + options[1].getOptionForwebview() + "';";
                    }
                }

                String optionC = "";
                String innerCC = "document.getElementById('innerCircleC').style.backgroundColor = '" + whitebackColor + "';";

                String valueC = "document.getElementById('answerC').style.borderColor = '" + red + "';";
                String valueCBagColor = "document.getElementById('answerC').style.backgroundColor = '" + whitebackColor + "';";

                if (quizQuestion.getAnswer().equals("C")) {
                    if (options[2].getOptionType().equals("TEXT")) {
                        optionC = "document.getElementById('opt_c').innerHTML = '" + options[2].getOptionForwebview() + "';";
                        optionC = optionC + valueC + valueCBagColor + innerCC;
                    }
                } else {
                    if (options[2].getOptionType().equals("TEXT")) {
                        optionC = "document.getElementById('opt_c').innerHTML = '" + options[2].getOptionForwebview() + "';";
                    }
                }

                String optionD = "";
                String innerCD = "document.getElementById('innerCircleD').style.backgroundColor = '" + whitebackColor + "';";

                String valueD = "document.getElementById('answerD').style.borderColor = '" + red + "';";
                String valueDBagColor = "document.getElementById('answerD').style.backgroundColor = '" + whitebackColor + "';";
                if (quizQuestion.getAnswer().equals("D")) {
                    if (options[3].getOptionType().equals("TEXT")) {
                        optionD = "document.getElementById('opt_d').innerHTML = '" + options[3].getOptionForwebview() + "';";
                        optionD = optionD + valueD + valueDBagColor + innerCD;
                    }
                } else {
                    if (options[3].getOptionType().equals("TEXT")) {
                        optionD = "document.getElementById('opt_d').innerHTML = '" + options[3].getOptionForwebview() + "';";
                    }
                }


                String questionExplaination = formateEscapeChar(quizQuestion.getQuestionExplaination());
                String js = "javascript:" +
                        "document.getElementById('ques').innerHTML = '" + question + "';" +
                        questionImageUrl +
                        optionA +
                        ansUrlA +
                        optionB +
                        ansUrlB +
                        optionC +
                        ansUrlC +
                        optionD +
                        ansUrlD +
                        "document.getElementById('explanationQuestion').innerHTML = '" + questionExplaination + "';"
                        + imgUrlans_questionExplanation;
                webView.loadUrl("javascript:resetMM()");
                if (Build.VERSION.SDK_INT >= 19) {
                    view.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                } else {
                    view.loadUrl(js);
                }
            }
        });
        webView.setEnabled(true);
        webView.loadUrl("file:///android_asset/webview.html");
        // webView.loadUrl("javascript:dummyMethod()");
    }


}
