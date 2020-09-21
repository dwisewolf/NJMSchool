package com.wisewolf.njmschool.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.R;

public class SubjectAdapterLarge extends  RecyclerView.Adapter<SubjectAdapterLarge.SubjectViewHolder> {


    private String[] subject;
    private RecyclerView recent_add_recyclerView;
    private static int lastClickedPosition = -1;
    private int selectedItem;
    private final  OnItemClickListener listener;



    public SubjectAdapterLarge(String[] subject, RecyclerView recent_add_recyclerView, OnItemClickListener itemClickListener ) {
        this.subject = subject;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = itemClickListener;
        selectedItem = -1;

    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.subject_name_large,
            parent,
            false
        );

        SubjectViewHolder viewHolder = new SubjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectViewHolder holder, final int position) {

        holder.subj.setBackgroundColor(Color.parseColor("#802196F3"));

        if (selectedItem == position) {
            holder.subj.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        holder.set(subject[position]);
        holder.bind(subject[position], listener,position);





    }

    @Override
    public int getItemCount() {
        return subject.length;
    }

    public  class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView subj;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subj=itemView.findViewById(R.id.sub_name);
        }

        public void set(String s) {
            subj.setText(s);
        }

        public void bind(final String s, final OnItemClickListener listeners, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listeners.onItemClick(s);
                    int previousItem = selectedItem;
                    selectedItem = position;
                    notifyItemChanged(previousItem);
                    notifyItemChanged(position);


                }
            });
        }
    }



}
