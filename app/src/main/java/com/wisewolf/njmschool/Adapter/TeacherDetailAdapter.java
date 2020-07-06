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
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.R;

import java.util.List;

public class TeacherDetailAdapter extends RecyclerView.Adapter<TeacherDetailAdapter.TeacherDetailAdapterViewHolder>{
    private List<TeacherDetails> dailyTask;
    private RecyclerView dailyTask_recyclerView;


    public interface OnItemClickListener {
        void onItemClick(DailyTask s);
    }

    public TeacherDetailAdapter(List<TeacherDetails> dailyTask, RecyclerView dailyTask_recyclerView) {
        this.dailyTask = dailyTask;
        this.dailyTask_recyclerView = dailyTask_recyclerView;

    }

    @NonNull
    @Override
    public TeacherDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.teacher_details,
            parent,
            false
        );

         TeacherDetailAdapterViewHolder viewHolder = new  TeacherDetailAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherDetailAdapterViewHolder holder, int position) {




        holder.set(dailyTask.get(position));
          //  holder.bind(dailyTask.get(position), listener, position);

    }

    @Override
    public int getItemCount() {
        return dailyTask.size();
    }

    class  TeacherDetailAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView teacher;
        public TeacherDetailAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher=itemView.findViewById(R.id.teacherDetails);

        }

        public void set(TeacherDetails o) {

            teacher.setText(o.getNotes());

        }

       /* public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((DailyTask) o);
                    final OfflineDatabase dbb;
                    dbb = new OfflineDatabase(context);
                    new AlertDialog.Builder(context)
                        .setTitle("Daily Task....")
                        .setMessage("Click on COMPLETE if you have finished this task...")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dbb.inserttaskID(GlobalData.regno, String.valueOf(((DailyTask) o).getId()));
                                notifyDataSetChanged();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Not Completed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbb.deletetask(GlobalData.regno, String.valueOf(((DailyTask) o).getId()));
                                notifyDataSetChanged();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                }
            });
        }*/
    }

}
