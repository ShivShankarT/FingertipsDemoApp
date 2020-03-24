package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class QuizQuestionAdapter extends FragmentStateAdapter {

    List<QuizQuestion> quizQuestions;
    public QuizQuestionAdapter(@NonNull FragmentActivity fragmentActivity, List<QuizQuestion> quizQuestions) {
        super(fragmentActivity);
        this.quizQuestions = quizQuestions;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return TeacherQuestionFragment.newInstance(position, quizQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return quizQuestions.size();
    }
}
