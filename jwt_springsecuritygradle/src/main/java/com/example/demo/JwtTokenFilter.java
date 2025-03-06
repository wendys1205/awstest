package com.example.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//@Configuration  //@Autowired, @Value사용불가
@RequiredArgsConstructor //lombok (final변수 매개변수 선언 생성자 자동 정의)
public class JwtTokenFilter extends OncePerRequestFilter{

	final String mykey ;
	final BCryptPasswordEncoder encoder;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//요청시 반복 - Filter 정의 ( 요청 - 필터(모든 요청 모든 컨트롤러 동일 반복) - 컨트롤러
    	String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    	System.out.println("JwtTokenFilter(헤더값확인용출력): "+authorizationHeader);
        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음 => 로그인 하지 않음
    	// 쿠키 확인
        if(authorizationHeader == null) {
  	      // 화면 로그인 시 쿠키의 "jwtToken"로 Jwt Token을 전송
  	      // 쿠키에도 Jwt Token이 없다면 로그인 하지 않은 것으로 간주
  		  System.out.println("JwtTokenFilter(request.getCookies()확인용출력1): "+request.getCookies());
  	      if(request.getCookies() == null) {
  	          filterChain.doFilter(request, response);
  	          return;
  	      }
  	      System.out.println("JwtTokenFilter(request.getCookies()확인용출력2): "+request.getCookies()[0].getName());
  	      // 쿠키에서 "jwtToken"을 Key로 가진 쿠키를 찾아서 가져오고 없으면 null return
  	      Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("jwtcookie"))
  	        .findFirst()
  	        .orElse(null);
  	     
  	      //쿠키 Jwt Token이 없다면 현재 필터 종료
	      if(jwtTokenCookie == null) {
	          filterChain.doFilter(request, response);
	          return;
	      }

	      // 쿠키 Jwt Token이 있다면 이 토큰으로 인증, 인가 진행
	      String jwtToken = jwtTokenCookie.getValue();
	      System.out.println("JwtTokenFilter(쿠키값확인용출력): " + jwtToken);
	      authorizationHeader = "Bearer " + jwtToken;
         }//if(authorizationHeader == null) end
        
        
        // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 => 잘못된 토큰
        if(!authorizationHeader.startsWith("Bearer ") ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
        String token = authorizationHeader.split(" ")[1];
        //System.out.println("JwtTokenFilter(헤더의 앞부분): " + authorizationHeader.split(" ")[0]);
        //System.out.println("JwtTokenFilter(헤더의 Bearer 뒷부분): " + token);
        
        // 전송받은 Jwt Token이 만료되었으면 => 다음 필터 진행(인증 X)
        if(JwtTokenUtil.isExpired(token, mykey)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        //공통
        // Jwt Token에서 loginId 추출
        String loginId = JwtTokenUtil.getLoginId(token, mykey);
        
        // 시큐리티 User 객체 생성=인증객체(내장객체)
        // Users 사용자정의 객체 생성=인증객체 변경
        Users loginuser = new Users();
        loginuser.setLoginid("user");
        loginuser.setPassword(encoder.encode("1111"));
        loginuser.setName("홍길동");
        loginuser.setRole(UserRole.USER.name());
        
        // 내장인증토큰대신에 Users 객체 인증 
        UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken
        (loginuser, null, List.of(new SimpleGrantedAuthority(loginuser.getRole())));
        
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
        
	}
	
}









