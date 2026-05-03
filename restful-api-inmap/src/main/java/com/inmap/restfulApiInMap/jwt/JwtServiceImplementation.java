package com.inmap.restfulApiInMap.jwt;

import com.inmap.restfulApiInMap.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtServiceImplementation implements JwtService {
    //Ver donde se guarda esta secret_key
    //"ProyectoFinalDeGradoInMap12345678" en Base64
    private static final String SECRET_KEY ="UHJveWVjdG9GaW5hbERlR3JhZG9Jbk1hcDEyMzQ1Njc4";
    @Override
    public String jwtGetToken(UserDetails usuario) {
        return getToken(new HashMap<>(),usuario);
    }
    public String getUsernameFromToken(String token)
    {
        return getClaim(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private String getToken(HashMap<String, Object> extraClaims, UserDetails usuario) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*3))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }
    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
}
