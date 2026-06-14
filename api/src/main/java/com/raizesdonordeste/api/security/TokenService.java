package com.raizesdonordeste.api.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.domain.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private final String secretKey = "AnaliseEDesenvolvimentoDeSistemasUninter2026";
    private final long expirationTime = 7200000;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Usuario usuario){
        List<String> nomePerfis = usuario.getPerfis().stream()
            .map(perfil -> perfil.getNome().toUpperCase())
            .toList();

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("perfis", nomePerfis)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validarToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e){
            return null;
        }
    }
}
