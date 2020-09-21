package com.wisewolf.njmschool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wisewolf.njmschool.Activity.NotesActivity;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.NotesMod;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;
import java.util.List;

public class Class_NotesAdapter extends RecyclerView.Adapter<Class_NotesAdapter.ClassVideoViewHolder> {
    String student="4";
    private static int lastClickedPosition = -1;
    private int selectedItem=-1;

    public Class_NotesAdapter(NotesActivity notesActivity, List<NotesMod> body, RecyclerView notesList, Class_NotesAdapter.OnItemClickListener onItemClickListener) {

        this.body = body;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = onItemClickListener;
        this.context = context;


    }

    private List<NotesMod> body;
    private Context context;
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

        holder.set(body.get(position));
        holder.bind(body.get(position), listener,position);

    }

    @Override
    public int getItemCount() {
        return body.size();
    }

    public interface OnItemClickListener {
        void onItemClick(NotesMod item);
    }

    class ClassVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView subject,topic,part;

        LinearLayout bgrndClr;

        public ClassVideoViewHolder(@NonNull View itemView ) {
            super(itemView);

            subject=itemView.findViewById(R.id.sub_videoAdded);
            topic=itemView.findViewById(R.id.topic_videoAdded);
            part=itemView.findViewById(R.id.part_id);
            bgrndClr=itemView.findViewById(R.id.bgrndClr);
        }

        public void set(NotesMod notesMod) {


            try {

                    subject.setText(notesMod.getTitle());
                    topic.setText(notesMod.getSubject());




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
                    listener.onItemClick((NotesMod) o);
                    int previousItem = selectedItem;
                    selectedItem = position;
                    notifyItemChanged(previousItem);
                    notifyItemChanged(position);
                }
            });


        }
    }

}
