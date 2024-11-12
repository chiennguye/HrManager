package com.HrManager.HrManagerSystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.HrManager.HrManagerSystem.Model.Account;
import com.HrManager.HrManagerSystem.Model.Employee;
import com.HrManager.HrManagerSystem.Service.EmployeeService;
import com.HrManager.HrManagerSystem.Service.UserService;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;

    private void addCommonAttributes(Model model) {
        String username = userService.getLoggedInUsername();
        model.addAttribute("username", username);
        String fullname = userService.getLoggedInFullName();
        model.addAttribute("fullname", fullname);
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        List<Account> acounts = userService.getAllAccount();
        model.addAttribute("acounts", acounts);
    }

    @GetMapping("/admin/index")
    public String index(Model model) {
        addCommonAttributes(model);
        return "Systemview/admin/index"; // Trả về trang index.html
    }

    @GetMapping("/admin/listAccount")
    public String listAccount(Model model) {
        addCommonAttributes(model);
        return "Systemview/admin/listAccount"; // Trả về trang index.html
    }

    @GetMapping("/admin/listEmployee")
    public String listEmployee(Model model) {
        addCommonAttributes(model);
        return "Systemview/admin/listEmployee::maincontent"; // Trả về trang listEmployee.html
    }

    @GetMapping("/admin/employeeManage")
    public String employeeManage(Model model) {
        addCommonAttributes(model);
        return "Systemview/admin/employeeManage::maincontent"; // Trả về trang employeeManage.html
    }

}
