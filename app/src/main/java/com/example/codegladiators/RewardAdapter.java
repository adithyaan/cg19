package com.example.codegladiators;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_rewards, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


    }

    @Override
    public int getItemCount() {
        return 15;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
