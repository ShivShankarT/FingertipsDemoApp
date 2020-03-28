package com.example.fingertipsdemoapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class QuizQuestionAdapter extends FragmentStateAdapter {

    List<QuizQuestion> quizQuestions;

    public QuizQuestionAdapter(@NonNull FragmentActivity fragmentActivity, List<QuizQuestion> quizQuestions) {
        super(fragmentActivity);
        this.quizQuestions = quizQuestions;
    }

    @Override
    public long getItemId(int position) {
        QuizQuestion quizQuestion = quizQuestions.get(position);
        long itemId = quizQuestion.getId()+(quizQuestion.isNormalViewRendering()?10:9990);
        return itemId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        QuizQuestion quizQuestion = quizQuestions.get(position);
        boolean normalViewRendering = quizQuestion.isNormalViewRendering();
        Log.i("sss", "createFragment: "+normalViewRendering+position);

        if (normalViewRendering)
            return TeacherQuestionFragment.newInstance(position, quizQuestion);
        else
            return QuestionWebViewFragment.newInstance(position, quizQuestion);
    }

    @Override
    public int getItemCount() {
        return quizQuestions.size();
    }
}
