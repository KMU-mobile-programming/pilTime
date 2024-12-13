package com.example.piltime.model;

public class Notification {
    private String id;                 // 알림 ID
    private String medicineId;         // 관련 약물 ID
    private String time;               // 알림 시간
    private String days;               // 알림 요일

    // 생성자
    public Notification(String id, String medicineId, String time, String days) {
        this.id = id;
        this.medicineId = medicineId;
        this.time = time;
        this.days = days;
    }

    // Getter 및 Setter 메서드
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return id + "," + medicineId + "," + time + "," + days;
    }
}
