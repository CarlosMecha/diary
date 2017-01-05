package com.carlosmecha.diary;

import com.carlosmecha.diary.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Main entry point for the application.
 */
@SpringBootApplication
@EntityScan(basePackageClasses = User.class)
public class DiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiaryApplication.class, args);
	}
}
