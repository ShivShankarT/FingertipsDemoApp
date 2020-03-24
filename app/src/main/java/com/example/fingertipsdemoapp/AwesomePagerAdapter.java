
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
                String imgUrl;
                if(q.getQuestionImage().equals("")) {
                    imgUrl="document.getElementById('question_pic').remove();";
                }
                else{
                    imgUrl = "document.getElementById('question_pic').src = '" + q.getQuestionImage() + "';"; }
                String imgUrlans_a;
                if(  q.getAnsImageA()==null || q.getAnsImageA().equals("") ) {
                    imgUrlans_a="document.getElementById('ans_a_pic').remove();"; }
                else{
                    imgUrlans_a = "document.getElementById('ans_a_pic').src = '" + q.getAnsImageA() + "';"; }

                String imgUrlForB;

                if( q.getAnsImageB()==null ||  q.getAnsImageB().equals("null") ||q.getAnsImageB().equals("") ) {
                    imgUrlForB="document.getElementById('ans_b_pic').remove();";
                }
                else{
                    imgUrlForB= "document.getElementById('ans_b_pic').src = '" + q.getAnsImageB() + "';";
                }
                String imgUrlans_c;
                if( q.getAnsImageC()==null ||q.getAnsImageC().equals("")) {
                    imgUrlans_c="document.getElementById('ans_c_pic').remove();";
                }
                else{
                    imgUrlans_c = "document.getElementById('ans_c_pic').src = '" + q.getAnsImageC() + "';";
                }
                String imgUrlans_d;
                if( q.getAnsImageD()==null ||q.getAnsImageD().equals("") ) {
                    imgUrlans_d="document.getElementById('ans_d_pic').remove();"; }
                else{
                    imgUrlans_d = "document.getElementById('ans_d_pic').src = '" + q.getAnsImageD() + "';"; }

                String imgUrlans_questionExplanation;
                if(  q.getQuestionExplanationImage()==null || q.getQuestionExplanationImage().equals("") ) {
                    imgUrlans_questionExplanation="document.getElementById('explanationQuestion_image').remove();"; }
                else{
                    imgUrlans_questionExplanation = "document.getElementById('explanationQuestion_image').src = '" + q.getQuestionExplanationImage() + "';"; }

                QuizQuestion.QuestionOption [] options=q.getOptions();

                String js = "javascript:" +
                        "document.getElementById('ques').innerHTML = '" + q.getQuestion() + "';" +
                        imgUrl+
                        "document.getElementById('opt_a').innerHTML = '" + options[0].option + "';" +
                        imgUrlans_a+
                        "document.getElementById('opt_b').innerHTML = '" + options[1].option+ "';" +
                        imgUrlForB+
                        "document.getElementById('opt_c').innerHTML = '" + options[2].option + "';" +
                        imgUrlans_c+
                        "document.getElementById('opt_d').innerHTML = '" + options[3].option + "';" +
                        imgUrlans_d+
                       "document.getElementById('explanationQuestion').innerHTML = '" + q.getQuestionExplaination() + "';"+
                        imgUrlans_questionExplanation
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
        myWebView.loadUrl("javascript:dummyMethod()");
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

