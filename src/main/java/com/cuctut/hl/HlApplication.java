package com.cuctut.hl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cuctut"})
@MapperScan("com.cuctut.hl.mapper")
public class HlApplication {

    public static void main(String[] args) {
        SpringApplication.run(HlApplication.class, args);
    }

}
