package com.HrManager.HrManagerSystem.Model;

public enum Gender {
    MALE,
    FEMALE;

    public String getDisplayName() {
        switch (this) {
            case MALE:
                return "Nam ";
            case FEMALE:
                return "Nữ ";
            default:
                return "Hoạt động";
        }
    }
}
