package com.sparta.cmung_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class CmungProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run ( CmungProjectApplication.class, args );
    }

}
