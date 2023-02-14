package com.quick.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CShisan
 */
@MapperScan({"com.quick.domain.mapper"})
@SpringBootApplication(scanBasePackages = {"com.quick"})
public class QuickLaunchStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickLaunchStartApplication.class, args);
    }

}
