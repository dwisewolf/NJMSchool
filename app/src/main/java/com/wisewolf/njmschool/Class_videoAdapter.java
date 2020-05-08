package com.wisewolf.njmschool;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimeo.networking.model.Video;

import java.util.ArrayList;

public class Class_videoAdapter extends RecyclerView.Adapter<Class_videoAdapter.ClassVideoViewHolder> {
    String student="4";
    private static int lastClickedPosition = -1;
    private int selectedItem=-1;

    public Class_videoAdapter( ArrayList recent_adds, RecyclerView recent_add_recyclerView, OnItemClickListener listener) {

        this.recent_adds = recent_adds;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = listener;


    }

    private ArrayList recent_adds;
    private RecyclerView recent_add_recyclerView;
    private final OnItemClickListener listener;


    @NonNull
    @Override
    public ClassVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.video_list_class,
            parent,
            false
        );

        ClassVideoViewHolder viewHolder = new ClassVideoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassVideoViewHolder holder, int position) {
        holder.bgrndClr.setBackgroundColor(Color.parseColor("#FFFFFF"));

        if (selectedItem == position) {
            holder.bgrndClr.setBackgroundColor(Color.parseColor("#FFFFCCCC"));
        }

        holder.set((Video) recent_adds.get(position));
        holder.bind(recent_adds.get(position), listener,position);

    }

    @Override
    public int getItemCount() {
        return recent_adds.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Video item);
    }

    class ClassVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView lesson,subject,topic,part;
        LinearLayout bgrndClr;

        public ClassVideoViewHolder(@NonNull View itemView ) {
            super(itemView);
            lesson=itemView.findViewById(R.id.lesson_videoAdded);
            subject=itemView.findViewById(R.id.sub_videoAdded);
            topic=itemView.findViewById(R.id.topic_videoAdded);
            part=itemView.findViewById(R.id.part_id);
            bgrndClr=itemView.findViewById(R.id.bgrndClr);
        }

        public void set(Video video) {
            lesson.setText(video.name);
            try {
                if (video.description==null){
                    subject.setText(video.name);
                    topic.setText("Not mentioned");
                }
                else {String[] x=video.description.split("-");
                    subject.setText(x[0]);
                    topic.setText(x[1]);}


                if (student.equals("4")) {

                    if (video != null) {



                        String cname = video.name;
                        cname = cname.substring(3);

                        String lessonname = cname.substring(0, Math.min(cname.length(), 2));
                        lesson.setText(lessonname);
                        cname = (video.name.substring(5)).substring(0, Math.min(cname.length(), 3));
                        part.setText(cname);


                    }

                }

            }
            catch (Exception e){

            }


        }

        @Override
        public void onClick(View v) {

        }

        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((Video) o);
                    int previousItem = selectedItem;
                    selectedItem = position;
                    notifyItemChanged(previousItem);
                    notifyItemChanged(position);
                }
            });
        }
    }


}
