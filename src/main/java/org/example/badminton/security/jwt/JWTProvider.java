package org.example.badminton.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt-secret}")
    private String jwtSecret;
    @Value("${jwt-expired}")
    private Long jwtExpired;

    public String generateToken(String username){
        try{
            Date today = new Date();
            Date expired = new Date(today.getTime()+jwtExpired);
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .subject(username)
                    .signWith(key)
                    .issuedAt(today)
                    .expiration(expired)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không tạo được chuỗi jwt ",e);
        }
    }

    public boolean validateToken(String token){
        try{
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (UnsupportedJwtException e){
            log.info("Hệ thống không hỗ trợ jwt");
            throw new RuntimeException("Hệ thống không hỗ trợ jwt ",e);
        }catch (ExpiredJwtException e){
            log.info("Chuỗi jwt hết hạn");
            throw new RuntimeException("Chuỗi jwt hết hạn ",e);
        }catch (SignatureException e){
            log.info("Chữ kí jwt không đúng");
            throw new RuntimeException("Chữ kí jwt không đúng ",e);
        }catch (IllegalArgumentException e){
            log.info("Chuỗi jwt rỗng");
            throw new RuntimeException("Chuỗi jwt rỗng ",e);
        }catch (JwtException e){
            log.info("Không xác thực được chuỗi jwt");
            throw new RuntimeException("Không xác thực được chuỗi jwt ",e);
        }
    }

    public String getUsernameFromToken(String token){
        try{
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        }catch (Exception e){
            log.info("Không lấy được username từ chuỗi jwt");
            throw new RuntimeException("Không lấy được username từ chuỗi jwt ",e);
        }
    }

    public long getExpirationFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload().getExpiration().getTime();
        } catch (Exception e) {
            throw new RuntimeException("Không lấy được thời gian hết hạn từ token", e);
        }
    }
}