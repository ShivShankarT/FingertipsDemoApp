package com.example.fingertipsdemoapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Created by ubuntu on 9/16/2016.
 */
public class AnimationRecyclerAdapter<T> extends RecyclerView.Adapter<AnimationRecyclerAdapter.ProfileHolder> {

    private List<T> recycleViewModelList;
    private Context context;
    private int layout;
    private ReturnView<T> returnView;
    private int from;
    private int lastPosition = -1;
    private boolean enableSlidAnim = true;

    public AnimationRecyclerAdapter(List<T> recycleViewModelList, Context context, int layout, ReturnView<T> returnView, int from) {
        this.recycleViewModelList = recycleViewModelList;
        this.context = context;
        this.layout = layout;
        this.returnView = returnView;
        this.from = from;
    }

    public void setEnableSlidAnim(boolean enableSlidAnim) {
        this.enableSlidAnim = enableSlidAnim;
    }

    @Override
    public int getItemCount() {
        return recycleViewModelList.size();
    }

    @Override
    public void onBindViewHolder(ProfileHolder rideHistoryViewHolder, final int i) {
        returnView.getAdapterView(rideHistoryViewHolder.v, recycleViewModelList, i, from);


    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        if (enableSlidAnim)
            setAnimation(viewGroup, viewType);
        return new ProfileHolder(itemView);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public interface ReturnView<T> {
        void getAdapterView(View view, List<T> objects, int position, int from);

    }

    public static class ProfileHolder extends RecyclerView.ViewHolder {
        View v;

        public ProfileHolder(View v) {
            super(v);
            this.v = v;

        }
    }

}