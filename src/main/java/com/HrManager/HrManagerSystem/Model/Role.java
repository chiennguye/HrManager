package com.HrManager.HrManagerSystem.Model;

public enum Role {
    ADMIN,
    MANAGER,
    EMPLOYEE;

    public String getDisplayRole() {
        switch (this) {
            case ADMIN:
                return "Quản trị";
            case EMPLOYEE:
                return "Nhân viên";
            case MANAGER:
                return "Quản lý";
            default:
                return "Chưa cấp quyền";
        }
    }

    public String getColor() {
        switch (this) {
            case ADMIN:
                return "color: red;"; // Màu đỏ cho trạng thái OFFWORK
            case EMPLOYEE:
                return "color: green;"; // Màu xám cho trạng thái INACTIVE
            case MANAGER:
                return "color: blue;"; // Màu xám cho trạng thái INACTIVE
            default:
                return "color: green;"; // Màu xanh cho trạng thái ACTIVE
        }
    }
}
