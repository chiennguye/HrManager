package com.HrManager.HrManagerSystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.HrManager.HrManagerSystem.Model.Account;
import com.HrManager.HrManagerSystem.Model.VerificationResult;
import com.HrManager.HrManagerSystem.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "Systemview/login"; // Trả về trang login.html
    }

    // Xác thực email người dùng
    @GetMapping("/verify")
    public String showVerificationPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "Systemview/verify"; // Hiển thị trang nhập mã xác thực
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String code,
            @RequestParam String email,
            Model model) {
        logger.info("Bắt đầu xác thực mã với email: {}", email);

        if (code == null || code.isEmpty()) {
            model.addAttribute("error", "Mã xác thực hoặc email không hợp lệ.");
            logger.warn("Mã xác thực hoặc email không hợp lệ.");
            return "Systemview/verify"; // Quay lại trang xác thực
        }

        try {
            VerificationResult verificationResult = userService.verifyCode(code, email);

            if (verificationResult.isSuccess()) {
                model.addAttribute("message", "Đăng ký thành công! Tài khoản của bạn đã được kích hoạt.");
                logger.info("Xác thực thành công cho email: {}", email);
            } else {
                model.addAttribute("error", verificationResult.getMessage());
                logger.warn("Xác thực không thành công: {}", verificationResult.getMessage());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình xác thực. Vui lòng thử lại.");
            logger.error("Lỗi trong quá trình xác thực: {}", e.getMessage());
        }

        return "Systemview/verify";
    }

    // Chức năng đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "Systemview/register"; // Hiển thị trang đăng ký
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("account") Account account, Model model) {
        try {
            // Sử dụng thông tin từ đối tượng Account
            Account newAccount = userService.registerUser(account.getUsername(), account.getPassword(),
                    account.getEmail(), account.getFullname(),
                    account.getPhonenumber());
            return "redirect:/verify?email=" + account.getEmail(); // Thêm email vào URL
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "Systemview/register"; // Trở lại trang đăng ký nếu có lỗi
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            return "Systemview/register"; // Trở lại trang đăng ký nếu có lỗi
        }
    }

}
