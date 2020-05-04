package com.wisewolf.njmschool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimeo.networking.model.Video;

import java.util.ArrayList;

public class SubjectAdapter extends  RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {


    private String[] subject;
    private RecyclerView recent_add_recyclerView;
    private OnItemClickListner itemClickListner;

    public SubjectAdapter(String[] subject, RecyclerView recent_add_recyclerView ) {
        this.subject = subject;
        this.recent_add_recyclerView = recent_add_recyclerView;

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.subject_name,
            parent,
            false
        );

        SubjectViewHolder viewHolder = new SubjectViewHolder(v,itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
       holder.set(subject[position]);
    }

    @Override
    public int getItemCount() {
        return subject.length;
    }

    public  class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView subj;

        public SubjectViewHolder(@NonNull View itemView, OnItemClickListner itemClickListner) {
            super(itemView);
            subj=itemView.findViewById(R.id.sub_name);
        }

        public void set(String s) {
            subj.setText(s);
        }
    }


    private class OnItemClickListner {
    }
}
