package com.lustycoder.myapplication.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lustycoder.myapplication.MainActivity;
import com.lustycoder.myapplication.R;
import com.lustycoder.myapplication.model.MeetingModel;

import java.util.List;

public class Meetingadapter extends RecyclerView.Adapter<Meetingadapter.ViewHolder> {
    Context context;
    List<MeetingModel> meetingModelList;

    public Meetingadapter(Context context, List<MeetingModel> meetingModelList) {
        this.context=context;
        this.meetingModelList=meetingModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meetinglistdata,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.meetingtime.setText(meetingModelList.get(position).getStartTime()+"-"+meetingModelList.get(position).getEndTime());
       if ((meetingModelList.get(position).getDescription().length())<21) {
           holder.meetingtext.setText(meetingModelList.get(position).getDescription().substring(0, 17)+"...");
       }
       else
       {
           holder.meetingtext.setText(meetingModelList.get(position).getDescription());
       }
    }

    @Override
    public int getItemCount() {
        return meetingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView meetingtime,meetingtext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            meetingtime=itemView.findViewById(R.id.meetingtime);
            meetingtext=itemView.findViewById(R.id.meetingtext);

        }
    }
}
