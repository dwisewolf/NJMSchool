package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;

public class PackageVideoAdapter extends RecyclerView.Adapter<PackageVideoAdapter.PackageVideoAdapterViewHolder> {




    public PackageVideoAdapter(Context context, ArrayList videoList, RecyclerView videoList_recyclerView, int imageWidth, int pageLength, OnItemClickListener itemClickListner) {
        this.videoList = videoList;
        this.videoList_recyclerView = videoList_recyclerView;
        this.imageWidth = imageWidth;
        this.pageLength = pageLength;
        this.context = context;
        this.itemClickListner = itemClickListner;
    }

    Context context;
    private ArrayList videoList;
    private RecyclerView videoList_recyclerView;
    private OnItemClickListener itemClickListner;
    int imageWidth, pageLength;

    @NonNull
    @Override
    public PackageVideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.packagevideo,
            parent,
            false
        );

        PackageVideoAdapterViewHolder viewHolder = new PackageVideoAdapterViewHolder(v, itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageVideoAdapterViewHolder holder, int position) {

        holder.set((Video) videoList.get(position));
        holder.bind(videoList.get(position), itemClickListner,position);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class PackageVideoAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView head, subhead;
        ImageView thumb;
        CardView cardWidth;

        public PackageVideoAdapterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            subhead = itemView.findViewById(R.id.subHead);
            thumb = itemView.findViewById(R.id.thumbnail);
            cardWidth = itemView.findViewById(R.id.pack_card);
            setWidth();
        }

        public void set(Video video) {

            try {
                if (video.description == null) {
                    head.setText(video.name);
                    head.setText("Not mentioned");
                } else {
                    String[] x = video.description.split("-");
                    head.setText(x[0]);
                    subhead.setText(x[1]);
                }
                int i = video.pictures.sizes.size();

                Glide.with(context)
                    .load((video.pictures.sizes.get(i - 1).link)).into(thumb);

            } catch (Exception e) {

            }


        }
        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((Video) o);

                }
            });


        }

        private void setWidth() {
            ViewTreeObserver viewTreeObserver = cardWidth.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        cardWidth.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                        cardWidth.setLayoutParams(new LinearLayout.LayoutParams((int) (imageWidth / 2.3), (int) (pageLength / 3.5)));


                    }
                });
            }


        }
    }
    public interface OnItemClickListener {
        void onItemClick(Video item);
    }
}


