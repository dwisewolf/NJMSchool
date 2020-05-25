package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Activity.VideoListing;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Models.OfflineVideos;
import com.wisewolf.njmschool.R;

import java.io.File;
import java.util.ArrayList;

public class OfflineMainAdapter extends RecyclerView.Adapter<OfflineMainAdapter.OfflineMainAdapterHolder> {
    private ArrayList offlineList;
    private Context context;
    private RecyclerView recent_add_recyclerView;
    private final  OnItemClickListener listener;
    private final  OnLongClickListener longlistener;

    public OfflineMainAdapter(ArrayList offlineList, Context context, RecyclerView recent_add_recyclerView, OnItemClickListener listener , OnLongClickListener longlistner) {
        this.offlineList = offlineList;
        this.context = context;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = listener;
        this.longlistener = longlistner;
    }

    @NonNull
    @Override
    public OfflineMainAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.offline_card_main,
            parent,
            false
        );

        OfflineMainAdapterHolder viewHolder = new OfflineMainAdapterHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineMainAdapterHolder holder, int position) {
        holder.set(((OfflineVideos)offlineList.get(position)));
        holder.bind(offlineList.get(position), listener,position,longlistener);
    }

    @Override
    public int getItemCount() {
        return offlineList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(OfflineVideos item);
    }
    public interface OnLongClickListener {
        void onItemLongClick(OfflineVideos item);
    }

    class OfflineMainAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView id1,id2;
        ImageView thumb;

        public OfflineMainAdapterHolder(@NonNull View itemView) {
            super(itemView);
            id1=itemView.findViewById(R.id.topic_tv_id);
            id2=itemView.findViewById(R.id.teacher_id_tv);
            thumb=itemView.findViewById(R.id.thumb_nail_id);
        }

        @Override
        public void onClick(View v) {

        }

        public void set(OfflineVideos o) {
            Glide.with(context)
                .load(o.getExtra1()) // image url



                .centerCrop()
                .into( thumb);
            try {

                String[] split=o.getExtra2().split("-");
                id1.setText(split[1]);
                id2.setText(split[2]);

            }catch (Exception e)
            {
                id1.setText("");
                id2.setText("");
            }

        }

        public void bind(final Object o, final OnItemClickListener listener, final int position, final OnLongClickListener longlistener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick((OfflineVideos) o);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longlistener.onItemLongClick((OfflineVideos) o);

                    new AlertDialog.Builder(context)
                        .setTitle("SECURITY ALERT")
                        .setMessage("Do you want to delete the video ")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(((OfflineVideos) o).getLocat());
                                file = new File(file, ((OfflineVideos) o).getName() + ".des");
                                file.delete();
                                file = new File(file, ((OfflineVideos) o).getSalt());
                                file.delete();
                                file = new File(file, ((OfflineVideos) o).getInv());
                                file.delete();
                                OfflineDatabase dbb;
                                dbb = new OfflineDatabase(context);
                                dbb.deletetable(((OfflineVideos) o).getName());
                                dbb.OfflineVideoss();
                                offlineList.remove(position);
                                notifyItemRemoved(position);

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                    return true;
                }
            });



        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    }
}

