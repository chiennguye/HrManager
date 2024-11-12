package com.HrManager.HrManagerSystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.HrManager.HrManagerSystem.Model.Employee;
import com.HrManager.HrManagerSystem.Service.EmployeeService;
import com.HrManager.HrManagerSystem.Service.UserService;

@Controller
public class EmployeeController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/homepage")
    public String homepage(Model model) {
        String username = userService.getLoggedInUsername(); // Lấy username từ service
        model.addAttribute("username", username); // Truyền vào model
        String fullname = userService.getLoggedInFullName(); // Lấy fullname từ service
        model.addAttribute("fullname", fullname); // Truyền vào model
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "Systemview/employee/homepage"; // Trả về trang homepage.html
    }

}
