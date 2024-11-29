package com.example.piltime.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piltime.R;

import java.util.List;

public class AlarmListFragment extends androidx.fragment.app.DialogFragment {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private List<AlarmSystemActivity.AlarmForm> alarmList;
    private OnAlarmActionListener actionListener;

    public interface OnAlarmActionListener {
        void onAlarmEdit(AlarmSystemActivity.AlarmForm alarm);
        void onAlarmDelete(AlarmSystemActivity.AlarmForm alarm);
    }

    public void setOnAlarmActionListener(OnAlarmActionListener listener) {
        this.actionListener = listener;
    }

    public AlarmListFragment(List<AlarmSystemActivity.AlarmForm> alarmList) {
        this.alarmList = alarmList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAlarms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        alarmAdapter = new AlarmAdapter(alarmList);
        recyclerView.setAdapter(alarmAdapter);

        alarmAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlarmSystemActivity.AlarmForm alarm) {
                // 알람 수정
                if (actionListener != null) {
                    actionListener.onAlarmEdit(alarm);
                }
                dismiss();
            }

            @Override
            public void onItemLongClick(AlarmSystemActivity.AlarmForm alarm) {
                // 알람 삭제
                if (actionListener != null) {
                    actionListener.onAlarmDelete(alarm);
                }
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 다이얼로그 크기 설정
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

