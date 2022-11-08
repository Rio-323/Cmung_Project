package com.sparta.cmung_project.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cmung_project.exception.ErrorResponse;
import com.sparta.cmung_project.global.dto.GlobalResDto;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getHeaderToken ( request, "Access" );
        String refreshToken = jwtUtil.getHeaderToken ( request, "Refresh" );

        if(accessToken != null) {
            if(!jwtUtil.tokenValidation ( accessToken )) {
                jwtExceptionHandler(response, "Access 토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
                return;
            }

            setAuthentication ( jwtUtil.getUserIdFromToken ( accessToken ) );

        } else if (refreshToken != null) {
            if(!jwtUtil.tokenValidation ( refreshToken )) {
                jwtExceptionHandler(response, "Refresh 토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
                return;
            }

            setAuthentication ( jwtUtil.getUserIdFromToken ( refreshToken ) );
        }

        filterChain.doFilter ( request, response );
    }

    public void setAuthentication(String userId) {
        Authentication authentication = jwtUtil.createAuthentication ( userId );
        SecurityContextHolder.getContext ().setAuthentication ( authentication );
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus ( status.value () );
        response.setContentType ( "application/json" );

        try {
            String json = new ObjectMapper ().writeValueAsString ( new ErrorResponse ( status.value (), "T001", msg ) );
            response.getWriter ().write ( json );
        } catch (Exception e) {
            log.error ( e.getMessage () );
        }
    }
}
