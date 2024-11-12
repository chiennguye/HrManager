package com.HrManager.HrManagerSystem.Model;

public enum Status {
    WORKING,
    OFFWORK;

    public String getDisplayName() {
        switch (this) {
            case OFFWORK:
                return "Đã nghỉ ";
            case WORKING:
                return "Đang làm ";
            default:
                return "Hoạt động";
        }
    }

    public String getColor() {
        switch (this) {
            case OFFWORK:
                return "color: red;"; // Màu đỏ cho trạng thái OFFWORK
            case WORKING:
                return "color: green;"; // Màu xám cho trạng thái INACTIVE
            default:
                return "color: green;"; // Màu xanh cho trạng thái ACTIVE
        }
    }
}
