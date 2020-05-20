package com.wisewolf.njmschool.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Models.Response;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter  extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{
    private List students;
    private RecyclerView recent_add_recyclerView;
    private final  OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Response s);
    }

    public StudentAdapter(List recent_adds, RecyclerView recent_add_recyclerView, OnItemClickListener itemClickListener) {
        this.students = recent_adds;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.studeny_card,
            parent,
            false
        );

         StudentViewHolder viewHolder = new  StudentViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.set((Response) students.get(position));
        holder.bind((Response) students.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    class  StudentViewHolder extends RecyclerView.ViewHolder {

        TextView name,clss,sect,regno;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_id);
            clss=itemView.findViewById(R.id.class_id);
            sect=itemView.findViewById(R.id.section_id);
            regno=itemView.findViewById(R.id.regno_id);
        }

        public void set(Response o) {
            name.setText(o.getName());
            clss.setText("Class " +o.getClas());
            sect.setText("Div :"+o.getSection());
            regno.setText("srno : "+String.valueOf(o.getRegNum()));

        }
        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((Response) o);

                }
            });
        }
    }


}
