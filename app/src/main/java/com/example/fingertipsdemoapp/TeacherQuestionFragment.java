package com.example.fingertipsdemoapp;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fingertipsdemoapp.custom.LaTexTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TeacherQuestionFragment extends Fragment implements AnimationRecyclerAdapter.ReturnView<QuizQuestion.QuestionOption> {
    private QuizQuestion quizQuestion;
    private RecyclerView rv_question;
    private AnimationRecyclerAdapter<QuizQuestion.QuestionOption> recyclerAdapter;
    private int questionPosition;
    private LaTexTextView tv_qus_text;
    private ImageView img_question;


    public static TeacherQuestionFragment newInstance(int position, QuizQuestion quizQuestion) {
        TeacherQuestionFragment teachearQuestionFragment = new TeacherQuestionFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("quizQuestion", quizQuestion);
        teachearQuestionFragment.setArguments(args);
        return teachearQuestionFragment;
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {

        if (source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1);
    }

    public static String optionPositionToString(int pos) {
        if (pos == 0) {
            return "A";

        } else if (pos == 1) {
            return "B";

        } else if (pos == 2) {
            return "C";
        } else if (pos == 3) {
            return "D";
        }

        return null;
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
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        View questionExplayout = view.findViewById(R.id.questionExplayout);
        rv_question = view.findViewById(R.id.rv_question);
        tv_qus_text = view.findViewById(R.id.tv_qus_text);
        TextView tv_qus_text_plain = view.findViewById(R.id.tv_qus_text_plain);
        LaTexTextView tv_qus_exp = view.findViewById(R.id.tv_qus_exp);
        img_question = view.findViewById(R.id.img_question);
        ImageView img_question_exp=view.findViewById(R.id.img_question_exp);

        ViewGroup horizontal_scroll = view.findViewById(R.id.horizontal_scroll);
        String qustionImage = quizQuestion.getQuestionImage();
        if (!TextUtils.isEmpty(qustionImage)) {
            img_question.setVisibility(View.VISIBLE);
            Picasso.get().load(qustionImage).into(img_question);
        } else {
            img_question.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(quizQuestion.getQuestionExplaination())&&TextUtils.isEmpty(quizQuestion.getQuestionExplanationImage())){
            questionExplayout.setVisibility(View.GONE);
        }
        else {
            questionExplayout.setVisibility(View.VISIBLE);
        }



        getData();
        String question = quizQuestion.getQuestion();

        Log.i("hhh", "onCreateView: "+quizQuestion.getId());
        String question_explaination = quizQuestion.getQuestionExplaination();
        if (quizQuestion.isSpecialType()) {
            tv_qus_text.setLinketext(question);
            tv_qus_text_plain.setVisibility(View.GONE);
            horizontal_scroll.setVisibility(View.VISIBLE);
        } else {
            String Qnhtml = question;
            Qnhtml = Qnhtml.replace("<p>&nbsp;</p>", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CharSequence qntrimmed = trimTrailingWhitespace(Html.fromHtml(Qnhtml, Html.FROM_HTML_MODE_COMPACT));
                tv_qus_text_plain.setText(qntrimmed);
            } else {
                tv_qus_text_plain.setText(Html.fromHtml(Qnhtml));
            }
            horizontal_scroll.setVisibility(View.GONE);
            tv_qus_text_plain.setVisibility(View.VISIBLE);
        }



        String question_explanation_image = quizQuestion.getQuestionExplanationImage();
        if (!TextUtils.isEmpty(question_explanation_image)) {
            img_question_exp.setVisibility(View.VISIBLE);
            Picasso.get().load(question_explanation_image).into(img_question_exp);
        } else {
            img_question_exp.setVisibility(View.GONE);
        }



        if (quizQuestion.isExpSpecialType()) {
            tv_qus_exp.setLinketext(question_explaination);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CharSequence qntrimmed = Html.fromHtml(question_explaination, Html.FROM_HTML_MODE_COMPACT);
                tv_qus_exp.setText(qntrimmed);
            } else {
                tv_qus_exp.setText(Html.fromHtml(question_explaination));
            }

        }

        return view;
    }

    private void getData() {

        List<QuizQuestion.QuestionOption> list = new ArrayList<>();
        list.add(quizQuestion.getOptions()[0]);
        list.add(quizQuestion.getOptions()[1]);
        list.add(quizQuestion.getOptions()[2]);
        list.add(quizQuestion.getOptions()[3]);
        recyclerAdapter = new AnimationRecyclerAdapter<QuizQuestion.QuestionOption>(list, getActivity(), R.layout.item_question, this, 0);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        rv_question.setLayoutManager(llm);
        rv_question.setAdapter(recyclerAdapter);
    }

    @Override
    public void getAdapterView(View view, final List<QuizQuestion.QuestionOption> options, final int position, int from) {
        TextView tv_option_no = view.findViewById(R.id.tv_option_no);
        final RelativeLayout rlMain = view.findViewById(R.id.rl_option);
        LaTexTextView tv_option = view.findViewById(R.id.tv_option);
        ImageView option_Image = view.findViewById(R.id.option_image);
        tv_option_no.setText(optionPositionToString(position));
        QuizQuestion.QuestionOption questionOption = options.get(position);
        String option = questionOption.getOption();
        if (position == TeacherQuestionActivity.optionAnsPos(quizQuestion.getAnswer())) {
            rlMain.setBackground(getResources().getDrawable(R.drawable.rounded_tealish_border_5dp));
            tv_option.setTextColor(getResources().getColor(R.color.nunito_bold));
            tv_option_no.setTextColor(getResources().getColor(R.color.nunito_extra_bold));
        } else {
            rlMain.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border));
            tv_option.setTextColor(getResources().getColor(R.color.nunito_bold));
            tv_option_no.setTextColor(getResources().getColor(R.color.nunito_extra_bold));


        }

        String optionType = questionOption.getOptionType();
        if (quizQuestion.isSpecialType()) {
            tv_option.setLinketext(option);
            option_Image.setVisibility(View.GONE);
            tv_option.setVisibility(View.VISIBLE);
        } else if (optionType.equalsIgnoreCase("image")&&!TextUtils.isEmpty(option)) {
            option_Image.setVisibility(View.VISIBLE);
            tv_option.setVisibility(View.GONE);
            Picasso.get().load(option).into(option_Image);
            option_Image.setVisibility(View.VISIBLE);
        } else {
            tv_option.setVisibility(View.VISIBLE);
            option_Image.setVisibility(View.GONE);
            String html = option;
            html = html.replace("<p>&nbsp;</p>", "");
            html = html.replace("<div>&nbsp;</div>", "");
            CharSequence trimmed;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                trimmed = trimTrailingWhitespace(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
                tv_option.setText(trimmed);
            } else {
                trimmed = trimTrailingWhitespace(Html.fromHtml(html));
                tv_option.setText(trimmed);
            }

        }


    }


}
