package com.sparta.cmung_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EnableSwagger2
@SpringBootApplication
@EnableJpaAuditing
public class CmungProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run ( CmungProjectApplication.class, args );
    }

    // 시간 설정
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
