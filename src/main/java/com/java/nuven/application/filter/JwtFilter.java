package com.java.nuven.application.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.nuven.application.response.ProblemDetail;
import com.java.nuven.application.security.details.UserDetailsImp;
import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.exception.ErrorCode;
import com.java.nuven.domain.repository.UserRepository;
import com.java.nuven.infra.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    
    private final UserRepository userRepository;

    public JwtFilter(UserRepository userRepository) {
        this.jwtUtils = new JwtUtils();
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (checkIfEndpointIsPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = recoveryToken(request);
        if (token == null) {
            makeError(response, ErrorCode.JWT_MISSING_ERROR);
            return;
        }

        String subject = null;
        try {
            subject = jwtUtils.getSubjectFromToken(token);
        } catch (Exception e) {
            makeError(response, ErrorCode.JWT_EXTRACT_ERROR);
            return;
        }

        User user = userRepository.findByUsername(subject).orElse(null);
        if (Objects.isNull(user)) {
            makeError(response, ErrorCode.JWT_USER_NOT_FOUND_EXTRACT);
            return;
        }
        UserDetailsImp userDetails = new UserDetailsImp(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void makeError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .title("Unauthorized")
                .detail(errorCode.toString())
                .build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(convertToJSON(problemDetail));
    }

    private String recoveryToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals("/api/v1/auth") ||
               requestURI.startsWith("/swagger-ui") ||
               requestURI.startsWith("/api-docs");
    }

    private String convertToJSON(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }
}
