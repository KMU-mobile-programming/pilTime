package com.example.piltime.model;

import java.util.List;

public class Medicine {
    private String user_id;
    private String name;               // 약 이름
    private int dosage;             // 복용량
    private String type;               // 약의 종류 (영양제/처방약)
    private double adherence;          // 복용율 (0.0 ~ 1.0 사이의 값)

    // 시간 관련 정보
    private String startDateString;    // 복용 시작 날짜
    private List<Integer> hourList;    // 알림 시간(시)
    private List<Integer> minuteList;  // 알림 시간(분)
    private List<Integer> selectedDays; // 알림 요일 (예: "월", "화")

    // 생성자
    public Medicine(String user_id,String name, int dosage, String type, double adherence,
                    String startDateString, List<Integer> hourList, List<Integer> minuteList, List<Integer> selectedDays) {
        this.user_id = user_id;
        this.name = name;
        this.dosage = dosage;
        this.type = type;
        this.adherence = adherence;
        this.startDateString = startDateString;
        this.hourList = hourList;
        this.minuteList = minuteList;
        this.selectedDays = selectedDays;
    }

    // Getter 및 Setter 메서드
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAdherence() {
        return adherence;
    }

    public void setAdherence(double adherence) {
        if (adherence < 0.0 || adherence > 1.0) {
            throw new IllegalArgumentException("복용율은 0.0에서 1.0 사이여야 합니다.");
        }
        this.adherence = adherence;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public List<Integer> getHourList() {
        return hourList;
    }

    public void setHourList(List<Integer> hourList) {
        this.hourList = hourList;
    }

    public List<Integer> getMinuteList() {
        return minuteList;
    }

    public void setMinuteList(List<Integer> minuteList) {
        this.minuteList = minuteList;
    }

    public List<Integer> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<Integer> selectedDays) {
        this.selectedDays = selectedDays;
    }

    @Override
    public String toString() {
        return "Medicine{name='" + name + "', dosage='" + dosage + "', type='" + type + "', adherence=" + adherence +
                ", startDateString='" + startDateString + "', hourList=" + hourList +
                ", minuteList=" + minuteList + ", selectedDays=" + selectedDays + "}";
    }
}
