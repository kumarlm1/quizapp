package com.dash.quiz.Auth;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.quiz.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class recycler_adaptor extends RecyclerView.Adapter<recycler_adaptor.my_recycler_view> {
    ArrayList<MyData> data;
    public recycler_adaptor(ArrayList<MyData> data) {
        this.data=data;

    }
    public class my_recycler_view extends RecyclerView.ViewHolder {
        TextView topic;
        CardView cardView;

         my_recycler_view(@NonNull View itemView) {
            super(itemView);
            this.topic = itemView.findViewById(R.id.subject);
            this.cardView = itemView.findViewById(R.id.card_view);

        }
    }

    @NonNull
    @Override
    public my_recycler_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View root= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
         CardView cardView=root.findViewById(R.id.card_view);
         cardView.setOnClickListener(Student.onClickListener);
        cardView.setOnClickListener(Staff.onClickListener);

        my_recycler_view recycler_view=new my_recycler_view(root);
        return recycler_view;
    }

    @Override
    public void onBindViewHolder(@NonNull my_recycler_view holder, int position) {
         holder.topic.setText(data.get(position).get_subject());
         holder.cardView.setBackgroundColor(Color.CYAN);
         holder.cardView.setRadius((float)30.0);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



}
