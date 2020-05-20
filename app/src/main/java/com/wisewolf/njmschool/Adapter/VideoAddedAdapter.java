package com.wisewolf.njmschool.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;
import java.util.List;

public class VideoAddedAdapter extends RecyclerView.Adapter<VideoAddedAdapter.VideoAddedViewHolder> {
    String student= GlobalData.clas;

    public VideoAddedAdapter(ArrayList recent_adds, RecyclerView recent_add_recyclerView) {
        this.recent_adds = recent_adds;
        this.recent_add_recyclerView = recent_add_recyclerView;

    }

    private ArrayList recent_adds;
    private RecyclerView recent_add_recyclerView;
    private  OnItemClickListner itemClickListner;


    @NonNull
    @Override
    public VideoAddedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.addded_videos,
            parent,
            false
        );

        VideoAddedViewHolder viewHolder = new VideoAddedViewHolder(v,itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAddedViewHolder holder, int position) {

        holder.set((Video) recent_adds.get(position));


    }

    @Override
    public int getItemCount() {
        return recent_adds.size();
    }

    class VideoAddedViewHolder extends RecyclerView.ViewHolder{

        TextView lesson,subject,topic,part;

        public VideoAddedViewHolder(@NonNull View itemView, OnItemClickListner onItemClickListener) {
            super(itemView);
            lesson=itemView.findViewById(R.id.lesson_videoAdded);
            subject=itemView.findViewById(R.id.sub_videoAdded);
            topic=itemView.findViewById(R.id.topic_videoAdded);
            part=itemView.findViewById(R.id.part_id);
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
    }

    private class OnItemClickListner {

    }
}
