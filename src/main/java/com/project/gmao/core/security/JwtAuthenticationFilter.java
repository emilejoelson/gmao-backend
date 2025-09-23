package com.project.gmao.core.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.gmao.common.enums.TokenType;

import static com.project.gmao.common.constants.SecurityConstants.JWT_HEADER_STRING;
import static com.project.gmao.common.constants.SecurityConstants.JWT_TOKEN_PREFIX;

import java.io.IOException;



@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && tokenProvider.isTokenValid(jwt)) {
                // Ensure it's an access token
                if (tokenProvider.getTokenType(jwt) == TokenType.ACCESS) {
                    String email = tokenProvider.getEmailFromToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
          
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWT_HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

