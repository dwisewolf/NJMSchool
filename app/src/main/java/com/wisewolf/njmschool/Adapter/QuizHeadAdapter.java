package com.wisewolf.njmschool.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.Models.QuizHead;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;

import java.util.List;

public class QuizHeadAdapter extends RecyclerView.Adapter<QuizHeadAdapter.QuizHeadAdapterViewHolder>{
    private List quizHeads;
    private RecyclerView quizHeads_recyclerView;
    private final  OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(QuizHead s);
    }

    public QuizHeadAdapter(List quizHeads, RecyclerView quizHeads_recyclerView, OnItemClickListener itemClickListener) {
        this.quizHeads = quizHeads;
        this.quizHeads_recyclerView = quizHeads_recyclerView;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public QuizHeadAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.mcq_list,
            parent,
            false
        );

         QuizHeadAdapterViewHolder viewHolder = new  QuizHeadAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHeadAdapterViewHolder holder, int position) {
        holder.set((QuizHead) quizHeads.get(position));
        holder.bind((QuizHead) quizHeads.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return quizHeads.size();
    }

    class  QuizHeadAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView subj,title,desc,time,date;
        public QuizHeadAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            subj=itemView.findViewById(R.id.subj_test);
            title=itemView.findViewById(R.id.title_test);
            desc=itemView.findViewById(R.id.desc_test);
            time=itemView.findViewById(R.id.time_test);
            date=itemView.findViewById(R.id.examDate_test);
        }

        public void set(QuizHead o) {
            subj.setText(o.getSubject());
            title.setText("Title - " +o.getTitle());
            desc.setText("Details -  :"+o.getDescription());
            time.setText("Time (Min) : "+o.getTime());
            date.setText("Exam Date : "+o.getDate());

        }
        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((QuizHead) o);

                }
            });
        }
    }

}
