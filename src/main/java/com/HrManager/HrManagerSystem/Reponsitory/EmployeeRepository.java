package com.HrManager.HrManagerSystem.Reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HrManager.HrManagerSystem.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
