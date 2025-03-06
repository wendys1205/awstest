package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Value("${jwt.secretkey}") //application.properties 자동주입
	String mykey ;
	
	@Autowired    //@Bean 객체 자동주입
	BCryptPasswordEncoder encoder;
	
 //수많은filter내장
// 404, 405, 400, 500 .... 시큐리티 403  오류 변환 -- 개발자 자세 오류메시지
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		System.out.println("SecurityConfig - securityFilterChain 실행중");
		//a 인증통과url(로그인창 자동이동 /login url 자동이동), /b 인증불필요
		// '/' url 요청 --> ????
		return httpSecurity
				.httpBasic(AbstractHttpConfigurer::disable) //username, password 검증  http 기반 인증방식 사용x
				.sessionManagement(
						sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(new JwtTokenFilter(mykey, encoder), UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests( request-> request
						.requestMatchers("/userinfo").authenticated()
						.anyRequest().permitAll()
				)
				.csrf(csrf -> csrf.disable()) 
		.build();
	}
	
}










