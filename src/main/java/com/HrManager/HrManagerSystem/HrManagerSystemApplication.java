package com.HrManager.HrManagerSystem;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrManagerSystemApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(HrManagerSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Kiểm tra kết nối
		try (Connection connection = dataSource.getConnection()) {
			if (connection != null) {
				System.out.println("Kết nối đến MySQL thành công!");
			} else {
				System.out.println("Không thể kết nối đến MySQL.");
			}
		} catch (Exception e) {
			System.out.println("Lỗi kết nối MySQL: " + e.getMessage());
		}

	}

}
