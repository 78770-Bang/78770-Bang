package com.inn.restaurant.jwt;


import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String clientAppUrl = "http://localhost:4200";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUserDetailsService Service;

    Claims claims = null;
    private String username = null;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


        if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/resetPassword|/user/signup")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                claims = jwtUtil.extractAllClaims(token);
                username = jwtUtil.extractUsername(token);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = Service.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    public boolean isAdmin() {
        return "admin" .equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser() {
        return "user" .equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        return username;
    }

}
