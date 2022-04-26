package br.com.dbc.vimserdev.feedbackcontinuo.security;

import java.util.Collections;
import java.util.Date;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class TokenService {
    private static final String PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String getToken(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(expiration));

        String token = Jwts.builder()
                .setIssuer("feedback-continuos")
                .setSubject(user.getUserId())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return PREFIX + token;
    }

    public Authentication getAuthentication(HttpServletRequest request) throws BusinessRuleException {
        String tokenBearer = request.getHeader(HEADER_AUTHORIZATION); // Bearer hfUIfs

        if (tokenBearer != null) {
            String token = tokenBearer.replace(PREFIX, "");
            String user = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }
        return null;
    }
}