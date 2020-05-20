package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;

public class RecentPlayedAdapter extends RecyclerView.Adapter<RecentPlayedAdapter.RecentPlayedAdapterViewHolder> {
    String student="4";
    private static int lastClickedPosition = -1;
    private int selectedItem=-1;

    public RecentPlayedAdapter(Context context, ArrayList recent_play, RecyclerView recent_play_recyclerView, OnItemClickListener listener) {

        this.recent_play = recent_play;
        this.recent_play_recyclerView = recent_play_recyclerView;
        this.listener = listener;
        this.context = context;


    }

    private ArrayList recent_play;
    private Context context;
    private RecyclerView recent_play_recyclerView;
    private final OnItemClickListener listener;


    @NonNull
    @Override
    public RecentPlayedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.offlinevideo_card,
            parent,
            false
        );

        RecentPlayedAdapterViewHolder viewHolder = new RecentPlayedAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentPlayedAdapterViewHolder holder, int position) {
        holder.set(((VideoUp)recent_play.get(position)));
        holder.bind(recent_play.get(position), listener,position);

    }

    @Override
    public int getItemCount() {
        return recent_play.size();
    }

    public interface OnItemClickListener {
        void onItemClick(VideoUp item);
    }

    class RecentPlayedAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id1,id2,id3;
        ImageView thumb;


        public RecentPlayedAdapterViewHolder(@NonNull View itemView ) {
            super(itemView);
            id1=itemView.findViewById(R.id.id1);
            id2=itemView.findViewById(R.id.id2);
            id3=itemView.findViewById(R.id.id3);
            thumb=itemView.findViewById(R.id.thumb_id);
        }

        public void set(VideoUp o) {
            Glide.with(context)
                .load(o.getThumbnailLink()) // image url



                .centerCrop()
                .into( thumb);
            id1.setText(o.getName());
            id2.setText(o.getName());
            id3.setText(o.getName());


        }

        @Override
        public void onClick(View v) {

        }

        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((VideoUp) o);
                    int previousItem = selectedItem;
                    selectedItem = position;
                    notifyItemChanged(previousItem);
                    notifyItemChanged(position);
                }
            });
        }
    }

}
