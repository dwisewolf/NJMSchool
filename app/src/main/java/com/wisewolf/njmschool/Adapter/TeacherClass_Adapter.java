package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;

public class TeacherClass_Adapter extends RecyclerView.Adapter<TeacherClass_Adapter.TeacherViewHolder>{
    private String[] TeacherClass;
    private Context context;
    private RecyclerView recent_play_recyclerView;
    private final OnItemClickListener listener;

    public TeacherClass_Adapter(String[] TeacherClass, Context context, RecyclerView recent_play_recyclerView, OnItemClickListener listener) {
        this.TeacherClass = TeacherClass;
        this.context = context;
        this.recent_play_recyclerView = recent_play_recyclerView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.class_card,
            parent,
            false
        );

        TeacherViewHolder viewHolder = new TeacherViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.set(TeacherClass[position]);
        holder.bind(TeacherClass[position], listener,position);
    }

    @Override
    public int getItemCount() {
        return TeacherClass.length;
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView classId;
        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            classId=itemView.findViewById(R.id.class_id);
        }

        @Override
        public void onClick(View v) {

        }

        public void set(String teacherClass) {
            classId.setText(teacherClass);

        }

        public void bind(final String teacherClass, final OnItemClickListener listener, int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(teacherClass);
                }
            });
        }
    }
}
