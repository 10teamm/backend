package com.swyp.catsgotogedog.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expire-min}")
    private long accessMin;

    @Value("${jwt.refresh-expire-day}")
    private long refreshDay;

    private Key key;

    public String createAccessToken(String sub) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessMin * 60_000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String sub) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshDay * 86_400_000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
