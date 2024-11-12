package com.HrManager.HrManagerSystem.Model;

public class VerificationResult {
    private boolean success; // Trạng thái xác thực
    private String message; // Thông điệp liên quan

    // Constructor
    public VerificationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getter và Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
