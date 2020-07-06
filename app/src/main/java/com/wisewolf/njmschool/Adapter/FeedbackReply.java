package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.Activity.DailyTask_Activity;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.FeedbackReplyModel;
import com.wisewolf.njmschool.R;

import java.util.List;

public class FeedbackReply extends RecyclerView.Adapter<FeedbackReply.FeedbackReplyViewHolder>{
    private List<FeedbackReplyModel> dailyTask;
    private RecyclerView dailyTask_recyclerView;


    public interface OnItemClickListener {
        void onItemClick(FeedbackReplyModel s);
    }

    public FeedbackReply(List<FeedbackReplyModel> dailyTask, RecyclerView dailyTask_recyclerViewr) {
        this.dailyTask = dailyTask;
        this.dailyTask_recyclerView = dailyTask_recyclerView;

    }

    @NonNull
    @Override
    public FeedbackReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.replycard,
            parent,
            false
        );

         FeedbackReplyViewHolder viewHolder = new  FeedbackReplyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackReplyViewHolder holder, int position) {




        holder.set(dailyTask.get(position));


    }

    @Override
    public int getItemCount() {
        return dailyTask.size();
    }

    class  FeedbackReplyViewHolder extends RecyclerView.ViewHolder {

        TextView user_feed,njms_reply;
        public FeedbackReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_feed=itemView.findViewById(R.id.user_feed);
            njms_reply=itemView.findViewById(R.id.njms_reply);

        }

        public void set(FeedbackReplyModel o) {

            user_feed.setText(o.getFeedback());
            njms_reply.setText(o.getReply());



        }


    }

}
