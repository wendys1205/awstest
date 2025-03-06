package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //- 스프링부트앱 실행 - "자동"실행
public class BCryptConfig {
     
    @Bean //메소드 리턴객체 스프링빈 등록
    public BCryptPasswordEncoder passwordEncoder() {
    	System.out.println("BCryptConfig - passwordEncoder 실행중");
        return new BCryptPasswordEncoder();
    }
}