package com.doiet.lab.security;

import com.doiet.lab.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(UserEntity userEntity)
    {
        //기한은 지금부터 1일로 설정
        Date expiryDate = Date.from(
        Instant.now()
                .plus(1, ChronoUnit.DAYS));

        //JWT 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) //sub
                .setIssuer("demo app") //iss
                .setIssuedAt(new Date()) //iat
                .setExpiration(expiryDate) //exp
                .compact();
    }

    public String validateAndGetUserID(String token){
        // parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명 한 후 토큰의 서명과비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
        // userId가 필요하므로 ,  getBody 호출
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
