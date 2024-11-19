package com.example.piltime.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;          // 유저 이름
    private String userId;            // 유저 ID
    private String profileImagePath;  // 프로필 사진 경로
    private List<Medicine> medicines; // 복용 약 리스트

    // 생성자
    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
        this.medicines = new ArrayList<>();
        this.profileImagePath = ""; // 기본값: 프로필 사진 없음
    }

    // 복용약 추가
    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    // 복용약 리스트 반환
    public List<Medicine> getMedicines() {
        return medicines;
    }

    // 유저 정보 반환
    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    // 프로필 사진 경로 설정
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    // 프로필 사진 경로 반환
    public String getProfileImagePath() {
        return profileImagePath;
    }

    // 복용률 계산
    public double calculateAdherenceRate() {
        if (medicines.isEmpty()) {
            return 0.0; // 약물이 없으면 복용률은 0%
        }

        double totalAdherence = 0.0;
        for (Medicine medicine : medicines) {
            totalAdherence += medicine.getAdherence(); // 각 약물의 복용률을 합산
        }

        return medicines.size() > 0 ? totalAdherence / medicines.size() * 100 : 0.0; // 평균 복용률 계산
    }

    // 복용 완료 여부 체크 (복용률 100%인지 확인)
    public boolean isAdherenceComplete() {
        return calculateAdherenceRate() == 100.0;
    }
}
