package com.example.demo;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil {
	public static String createToken(String loginId, String key, long expiredTimeMs) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
		Claims claims = Jwts.claims();
		claims.put("loginId", loginId);//사용자클레임 + 내장클레임(발급시간, 만료시간)
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))//발급
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))//만료
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
	}
	
    // Claims에서 loginId 꺼내기
    public static String getLoginId(String token, String secretKey) {
    	System.out.println("JwtTokenUtil:getLoginId()메소드실행");
        return extractClaims(token, secretKey).get("loginId").toString();
    }
    
    // 발급된 Token이 만료 시간이 지났는지 체크
    public static boolean isExpired(String token, String secretKey) {
    	System.out.println("JwtTokenUtil:isExpired()메소드실행");
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }
    
    // SecretKey를 사용해 Token Parsing
    private static Claims extractClaims(String token, String secretKey) {
    	System.out.println("JwtTokenUtil:extractClaims()메소드실행");
    	Claims claims = null;
    	try {
    		claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    	}
    	catch(ExpiredJwtException e) {
    		return e.getClaims();
    	}
        return claims;
    }
}
