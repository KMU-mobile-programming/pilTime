package com.example.piltime.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.piltime.R;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<AlarmSystemActivity.AlarmForm> alarmList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AlarmSystemActivity.AlarmForm alarm);
        void onItemLongClick(AlarmSystemActivity.AlarmForm alarm);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AlarmAdapter(List<AlarmSystemActivity.AlarmForm> alarmList) {
        this.alarmList = alarmList;
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewTime;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewAlarmName);
            textViewTime = itemView.findViewById(R.id.textViewAlarmTime);
        }
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        final AlarmSystemActivity.AlarmForm alarm = alarmList.get(position);
        holder.textViewName.setText(alarm.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(alarm);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onItemLongClick(alarm);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}

