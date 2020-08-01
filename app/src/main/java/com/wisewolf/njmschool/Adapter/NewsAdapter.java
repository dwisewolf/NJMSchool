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
import com.wisewolf.njmschool.Activity.NEwsListActivity;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterrViewHolder>{
    private List<News> news;
    private RecyclerView dailyTask_recyclerView;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(DailyTask s);
    }

    public NewsAdapter(List<News> news, RecyclerView dailyTask_recyclerView, NEwsListActivity context) {
        this.news = news;
        this.dailyTask_recyclerView = dailyTask_recyclerView;

        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapterrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.news_card,
            parent,
            false
        );

         NewsAdapterrViewHolder viewHolder = new  NewsAdapterrViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterrViewHolder holder, int position) {




        holder.set(news.get(position));
          //  holder.bind(news.get(position), listener, position);

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class  NewsAdapterrViewHolder extends RecyclerView.ViewHolder {

        TextView date,newslist;
        public NewsAdapterrViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date_exp);
            newslist=itemView.findViewById(R.id.news_list);
        }

        public void set(News o) {

                date.setText(o.getExpiryDate());
                newslist.setText(o.getNews());



        }

        /*public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((News) o);
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
        }*/
    }

}
