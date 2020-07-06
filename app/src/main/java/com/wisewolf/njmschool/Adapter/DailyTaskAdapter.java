package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.Activity.DailyTask_Activity;
import com.wisewolf.njmschool.Activity.MainActivity;
import com.wisewolf.njmschool.Activity.OfflineScreen;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.R;

import java.util.List;

public class DailyTaskAdapter extends RecyclerView.Adapter<DailyTaskAdapter.DailyTaskAdapterViewHolder>{
    private List<DailyTask> dailyTask;
    private RecyclerView dailyTask_recyclerView;
    private final  OnItemClickListener listener;
    private final Context context;
    private final  String formattedDate;

    public interface OnItemClickListener {
        void onItemClick(DailyTask s);
    }

    public DailyTaskAdapter(List<DailyTask> dailyTask, RecyclerView dailyTask_recyclerView, DailyTask_Activity context, String formattedDate, OnItemClickListener itemClickListener) {
        this.dailyTask = dailyTask;
        this.dailyTask_recyclerView = dailyTask_recyclerView;
        this.listener = itemClickListener;
        this.formattedDate = formattedDate;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyTaskAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.task_card,
            parent,
            false
        );

         DailyTaskAdapterViewHolder viewHolder = new  DailyTaskAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTaskAdapterViewHolder holder, int position) {




        holder.set(dailyTask.get(position));
            holder.bind(dailyTask.get(position), listener, position);

    }

    @Override
    public int getItemCount() {
        return dailyTask.size();
    }

    class  DailyTaskAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView subject,note1,note2,note3,completeflag;
        public DailyTaskAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.subject);
            note1=itemView.findViewById(R.id.note1);
            note2=itemView.findViewById(R.id.note2);
            note3=itemView.findViewById(R.id.note3);
            completeflag=itemView.findViewById(R.id.complete_status);
        }

        public void set(DailyTask o) {

                subject.setText(o.getSubject());
                note1.setText(o.getVideo());
                note2.setText(o.getTextbook());
                note3.setText(o.getNotes());

            final OfflineDatabase dbb;
            dbb = new OfflineDatabase(context);
            String task=dbb.taskId(GlobalData.regno);
            String[] tasklist=task.split(",");
            try {
                int f=0;
                for (int i=0;i<tasklist.length;i++){
                    if (tasklist[i].equals(String.valueOf(o.getId()))) {
                        f=1;

                    }

                }
                if (f==1){
                    completeflag.setText("    Completed");
                    completeflag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.read, 0, 0, 0);

                }
                else {
                    completeflag.setText("    Not Completed");
                    completeflag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.to_read, 0, 0, 0);

                }
            }
            catch (Exception e){
                String a="";
            }




        }

        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((DailyTask) o);
                    final OfflineDatabase dbb;
                    dbb = new OfflineDatabase(context);
                    new AlertDialog.Builder(context)
                        .setTitle("Daily Task....")
                        .setMessage("Click on COMPLETED if you have finished this task...")

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
        }
    }

}
