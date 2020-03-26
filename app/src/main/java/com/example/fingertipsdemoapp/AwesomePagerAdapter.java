
package com.example.fingertipsdemoapp;

import android.content.Context;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;


class AwesomePagerAdapter extends RecyclerView.Adapter<AwesomePagerAdapter.ViewPager2Holder> {
    Context mContext;
  //  List<QuestionModel> models;

    String data="this is data...";
    ViewPager2  viewPager2;

   /* public AwesomePagerAdapter(Context context,ViewPager2 viewPager2, List<QuestionModel> modelList){
        mContext=context;
        models=modelList;
        this.viewPager2=viewPager2;
    }*/
   List<QuizQuestion> quizQuestions;
    public AwesomePagerAdapter(Context context,ViewPager2 viewPager2, List<QuizQuestion> quizQuestions) {
            mContext=context;
           this.quizQuestions = quizQuestions;
           this.viewPager2=viewPager2;
    }
   /*public AwesomePagerAdapter(Context context,ViewPager2 viewPager2, List<QuestionModel> modelList){
       mContext=context;
       models=modelList;
       this.viewPager2=viewPager2;
   }*/
    @NonNull
    @Override
    public ViewPager2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.question_page_list_item, parent,false);
        return   new ViewPager2Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewPager2Holder holder, int position) {
         //  final QuestionModel q=models.get(position);
         QuizQuestion q=quizQuestions.get(position);
        Log.e("Test question", "Questions : "+q.getQuestion());
            final WebView myWebView=holder.myWebView;
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebContentsDebuggingEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String questionImageUrl;
                if(q.getQuestionImage().equals("")) {
                    questionImageUrl="document.getElementById('question_pic').remove();";
                }
                else{
                    questionImageUrl = "document.getElementById('question_pic').src = '" + q.getQuestionImage() + "';";
                }
                QuizQuestion.QuestionOption [] options=q.getOptions();



                String ansUrlA;
                if(options[0].getOptionType().equals("TEXT")){
                    ansUrlA="document.getElementById('ans_a_pic').remove();"; }
                else{
                    ansUrlA = "document.getElementById('ans_a_pic').src = '" +  options[0].option + "';"; }

                String ansUrlB;
                if(options[1].getOptionType().equals("TEXT")) {
                    ansUrlB="document.getElementById('ans_b_pic').remove();"; }
                else{
                    ansUrlB= "document.getElementById('ans_b_pic').src = '" + options[1].option + "';"; }


                String ansUrlC;
                if(options[2].getOptionType().equals("TEXT")) {
                    ansUrlC="document.getElementById('ans_c_pic').remove();"; }
                else{
                    ansUrlC = "document.getElementById('ans_c_pic').src = '" + options[2].option + "';"; }


                String ansUrlD;
                if( options[3].getOptionType().equals("TEXT") ) {
                    ansUrlD="document.getElementById('ans_d_pic').remove();"; }
                else{
                    ansUrlD= "document.getElementById('ans_d_pic').src = '" + options[3].option+ "';"; }



                String imgUrlans_questionExplanation;
                if(  q.getQuestionExplanationImage()==null || q.getQuestionExplanationImage().equals("") )
                {
                   imgUrlans_questionExplanation="document.getElementById('ans_explanation_pic').remove();";
                    }
                else{
                   imgUrlans_questionExplanation = "document.getElementById('ans_explanation_pic').src = '" + q.getQuestionExplanationImage() + "';";
                      }

                String optionA="";
                String red="#00FF00";
                String value ="document.getElementById('answerA').style.borderColor = '" + red+"';";

                String whitebackColor="#FFFFFF";
                String valueABagColor="document.getElementById('answerA').style.backgroundColor = '" +whitebackColor+"';";
                String innerCA="document.getElementById('innerCircleA').style.backgroundColor = '" +whitebackColor+"';";

                if(q.getAnswer().equals("A"))
                {
                    if (options[0].getOptionType().equals("TEXT")) {
                        optionA = "document.getElementById('opt_a').innerHTML = '" + options[0].option + "';";
                       // value="document.getElementById('answerC').style.borderColor = #FF0000'"+"';";
                        optionA=optionA+value+valueABagColor+innerCA;
                    }
                }
                else {

                    if (options[0].getOptionType().equals("TEXT")) {
                        optionA = "document.getElementById('opt_a').innerHTML = '" + options[0].option + "';";
                    }
                }

                String optionB="";
                String innerCB="document.getElementById('innerCircleB').style.backgroundColor = '" +whitebackColor+"';";
                String valueBBagColor="document.getElementById('answerB').style.backgroundColor = '" +whitebackColor+"';";
                String valueB ="document.getElementById('answerB').style.borderColor = '" + red+"';";
                if(q.getAnswer().equals("B"))
                {
                    if(options[0].getOptionType().equals("TEXT"))
                    {
                        optionB=  "document.getElementById('opt_b').innerHTML = '" + options[1].option + "';";
                        optionB=optionB+valueB+valueBBagColor+innerCB;
                    }
                }
                else {
                    if(options[0].getOptionType().equals("TEXT"))
                    {
                        optionB=  "document.getElementById('opt_b').innerHTML = '" + options[1].option + "';";
                    }
                }

                String optionC="";
                String innerCC="document.getElementById('innerCircleC').style.backgroundColor = '" +whitebackColor+"';";

                String valueC ="document.getElementById('answerC').style.borderColor = '" + red+"';";
                String valueCBagColor="document.getElementById('answerC').style.backgroundColor = '" +whitebackColor+"';";

                 if(q.getAnswer().equals("C"))
                 {
                     if (options[2].getOptionType().equals("TEXT")) {
                         optionC = "document.getElementById('opt_c').innerHTML = '" + options[2].option + "';";
                         optionC=optionC+valueC+valueCBagColor+innerCC;
                     }
                 }
                 else {
                     if (options[2].getOptionType().equals("TEXT")) {
                         optionC = "document.getElementById('opt_c').innerHTML = '" + options[2].option + "';";
                     }
                 }

                String optionD="";
                String innerCD="document.getElementById('innerCircleD').style.backgroundColor = '" +whitebackColor+"';";

                String valueD ="document.getElementById('answerD').style.borderColor = '" + red+"';";
                String valueDBagColor="document.getElementById('answerD').style.backgroundColor = '" +whitebackColor+"';";
                if(q.getAnswer().equals("D"))
                {
                    if(options[3].getOptionType().equals("TEXT"))
                    {
                        optionD=  "document.getElementById('opt_d').innerHTML = '" + options[3].option + "';";
                        optionD=optionD+valueD+valueDBagColor+innerCD;
                    }
                }
               else {
                    if(options[3].getOptionType().equals("TEXT"))
                    {
                        optionD=  "document.getElementById('opt_d').innerHTML = '" + options[3].option + "';";
                    }
                }


                String js = "javascript:" +
                        "document.getElementById('ques').innerHTML = '" + q.getQuestion() + "';" +
                        questionImageUrl+
                          optionA +
                          ansUrlA+

                          optionB +
                          ansUrlB+
                          optionC+
                          ansUrlC+
                          optionD +
                          ansUrlD+
                      "document.getElementById('explanationQuestion').innerHTML = '" + q.getQuestionExplaination() + "';"
                       +imgUrlans_questionExplanation
                        ;
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
        myWebView.setEnabled(true);
        myWebView.loadUrl("file:///android_asset/webview.html");
       // myWebView.loadUrl("javascript:dummyMethod()");
        myWebView.addJavascriptInterface(this, "Android");

    }

    @Override
    public int getItemCount() {
        /*return models.size();*/
        return quizQuestions.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    public class ViewPager2Holder extends RecyclerView.ViewHolder {
        private final WebView myWebView;
        public ViewPager2Holder(@NonNull View itemView) {
            super(itemView);
            myWebView= itemView.findViewById(R.id.webview);
        }
    }

    @JavascriptInterface
    public void callAndroidCallback(final String toast) {
       // Toast.makeText(mContext, toast+viewPager2.getCurrentItem(), Toast.LENGTH_SHORT).show();

        viewPager2.post(new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
            }
        });


    }
}

