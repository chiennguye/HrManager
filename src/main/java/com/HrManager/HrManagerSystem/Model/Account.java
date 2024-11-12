package com.HrManager.HrManagerSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Enum cho quyền

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private int phonenumber;

    @Column(nullable = false)
    private String verificationCode; // Mã xác nhận

    @ManyToOne(optional = true)
    @JoinColumn(name = "employee_id", nullable = true) // Liên kết với bảng Employee
    private Employee employee; // Mối quan hệ với Employee (một tài khoản có thể liên kết với một nhân viên)

    // Getters và Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() { // Dùng isEnabled thay vì getEnabled cho boolean
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getverificationCode() {
        return verificationCode;
    }

    public void setverificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public int getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDisplayStatusAcount() {
        if (getEnabled()) {
            return "Đang hoạt động";
        } else {
            return "Chưa kích hoạt";
        }
    }

}
