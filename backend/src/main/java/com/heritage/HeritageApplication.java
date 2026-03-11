package com.heritage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.heritage.mapper")
public class HeritageApplication {
    public static void main(String[] args) {
        SpringApplication.run(HeritageApplication.class, args);
    }
}
