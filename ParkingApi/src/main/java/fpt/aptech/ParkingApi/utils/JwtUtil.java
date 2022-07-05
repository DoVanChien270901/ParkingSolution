/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIEN
 */
@Service
public class JwtUtil {

    private final String SERRET_KEY = "secret";

    public String extracUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extracExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SERRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExprired(String token) {
        return extracExpiration(token).before(new Date());
    }

    public String generrateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Object[] listRole =  userDetails.getAuthorities().toArray();
        claims.put("roles", listRole[0].toString());
    //ObjectMapper oMapper = new ObjectMapper();
    //Map<String, Object> claims = oMapper.convertValue(userDetails, Map.class);
        return createToken(claims, userDetails.getUsername());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SERRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extracUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExprired(token) );
    }
}
