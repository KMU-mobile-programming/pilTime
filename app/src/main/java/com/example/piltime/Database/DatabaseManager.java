package com.example.piltime.Database;

import java.util.List;

public interface DatabaseManager {
    // 유저 관련 메서드
    void addUser(String userId, String userName, String profileImagePath);
    List<String> getAllUsers();
    String getUserName(String userId);
    boolean userExists(String userId);

    // 약물 관련 메서드
    void addMedicine(String userId, String medicineName, String dosage, String time, String type, float adherence);
    List<String> getAllMedicines();
    List<String> getMedicinesByUserId(String userId);

}
