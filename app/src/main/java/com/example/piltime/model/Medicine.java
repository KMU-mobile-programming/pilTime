package com.example.piltime.model;

public class Medicine {
    private String name;      // 약 이름
    private String dosage;    // 복용량
    private String time;      // 복용 시간
    private String type;      // 약의 종류 (영양제/처방약)
    private double adherence; // 복용율 (0.0 ~ 1.0 사이의 값)

    // 생성자
    public Medicine(String name, String dosage, String time, String type, double adherence) {
        this.name = name;
        this.dosage = dosage;
        this.time = time;
        this.type = type;
        this.adherence = adherence;
    }

    // Getter 및 Setter 메서드
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        this.adherence = adherence;
    }

    @Override
    public String toString() {
        return name + "," + dosage + "," + time + "," + adherence + "," + type;
    }
}
