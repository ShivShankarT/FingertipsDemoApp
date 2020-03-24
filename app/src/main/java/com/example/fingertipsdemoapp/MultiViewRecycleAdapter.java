package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ubuntu on 30/8/17.
 */

public class MultiViewRecycleAdapter<T> extends RecyclerView.Adapter<MultiViewRecycleAdapter.ProfileHolder> {

    ReturnView<T> returnView;
    int from;
    private List recycleViewModelList;

    public MultiViewRecycleAdapter(List<T> recycleViewModelList, ReturnView<T> returnView, int from) {
        this.recycleViewModelList = recycleViewModelList;
        this.returnView = returnView;
        this.from = from;
    }

    @Override
    public int getItemCount() {
        return recycleViewModelList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder rideHistoryViewHolder, final int i) {
        returnView.getAdapterView(rideHistoryViewHolder.v, recycleViewModelList, i, from);
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = returnView.returnViewHolder(viewGroup, i);
        return new ProfileHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        return returnView.returnViewType(recycleViewModelList, position);
        // return position;
    }

    public interface ReturnView<T> {
        void getAdapterView(View view, List<T> objects, int position, int from);

        int returnViewType(List<T> recycleViewModelList, int position);

        View returnViewHolder(ViewGroup viewGroup, int i);
    }

    public static class ProfileHolder extends RecyclerView.ViewHolder {
        View v;

        public ProfileHolder(View v) {
            super(v);
            this.v = v;
        }
    }


}