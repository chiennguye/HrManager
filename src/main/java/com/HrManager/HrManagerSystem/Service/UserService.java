package com.HrManager.HrManagerSystem.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.HrManager.HrManagerSystem.Model.Account;
import com.HrManager.HrManagerSystem.Model.Role;
import com.HrManager.HrManagerSystem.Model.VerificationResult;
import com.HrManager.HrManagerSystem.Reponsitory.AccountRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public Account registerUser(String username, String password, String email, String fullname, int phonenumber) {
        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if (accountRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại. Vui lòng chọn tên khác!!!");
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (accountRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email đã được sử dụng. Vui lòng sử dụng email khác!!!");
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setEmail(email);
        account.setFullname(fullname);
        account.setPhonenumber(phonenumber);
        account.setRole(Role.EMPLOYEE);
        account.setEnabled(false);
        account.setverificationCode(generateVerificationCode()); // Tạo mã xác thực

        Account savedAccount = accountRepository.save(account);

        // Gửi email xác thực
        sendVerificationEmail(email, savedAccount.getverificationCode());

        return savedAccount;
    }

    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Xác thực tài khoản");
        message.setText("Mã xác thực của bạn là: " + verificationCode);
        mailSender.send(message);
    }

    public VerificationResult verifyCode(String code, String email) {
        if (code == null || code.trim().isEmpty()) {
            return new VerificationResult(false, "Mã xác thực không hợp lệ.");
        }

        // Lấy người dùng từ cơ sở dữ liệu, sử dụng Optional để xử lý an toàn
        Optional<Account> optionalUser = accountRepository.findByEmail(email);
        logger.info("Đang kiểm tra email: {}", email);

        // Kiểm tra xem người dùng có tồn tại không
        if (!optionalUser.isPresent()) {
            logger.warn("Người dùng không tồn tại với email: {}", email);
            return new VerificationResult(false, "Người dùng không tồn tại.");
        }

        Account user = optionalUser.get(); // Lấy đối tượng Account từ Optional

        if (user.isEnabled()) {
            return new VerificationResult(false, "Tài khoản đã được kích hoạt.");
        }

        // Kiểm tra mã xác thực
        if (user.getverificationCode() == null || user.getverificationCode().isEmpty()) {
            return new VerificationResult(false, "Mã xác thực đã được sử dụng.");
        }

        if (!code.equals(user.getverificationCode())) {
            return new VerificationResult(false, "Mã xác thực không đúng.");
        }

        logger.info("Kiểm tra xác thực cho email: {} với mã: {}", email, code);

        // Kích hoạt tài khoản
        enableAccount(email);

        // Cập nhật lại mã xác thực
        user.setverificationCode(null); // Xóa mã xác thực sau khi dùng
        accountRepository.save(user); // Lưu thay đổi vào cơ sở dữ liệu

        return new VerificationResult(true, "Xác thực thành công.");
    }

    public void enableAccount(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email); // Sử dụng Optional

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get(); // Lấy tài khoản nếu có
            logger.info("Tìm thấy tài khoản: {}", account);

            if (!account.isEnabled()) {
                account.setEnabled(true);
                accountRepository.save(account);
                logger.info("Tài khoản đã được kích hoạt: {}", account);
            } else {
                logger.warn("Tài khoản đã được kích hoạt trước đó.");
            }
        } else {
            logger.error("Không tìm thấy tài khoản với email: {}", email);
        }
    }

    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername(); // email là username trong Spring Security

            // Tìm username từ database dựa trên email
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return account.getUsername(); // Trả về username từ database
        }

        return null; // Trường hợp không có người dùng đăng nhập
    }

    // Lấy fullname từ email đã đăng nhập
    public String getLoggedInFullName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername(); // email được sử dụng làm 'username' trong Spring
                                                                    // Security

            // Tìm tài khoản từ database dựa trên email
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return account.getFullname(); // Trả về fullname của tài khoản
        }

        return null; // Trường hợp không có người dùng đăng nhập
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

}
