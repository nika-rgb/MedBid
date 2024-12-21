package com.medbid.medbid.rest.v1.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medbid.medbid.rest.v1.response.ApiErrorDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private final JwtTokenUtilities accessTokenUtilities;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!Objects.isNull(authorizationHeader) &&
                    authorizationHeader.startsWith(BEARER_TOKEN_PREFIX) &&
                    Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {

                String jwtToken = authorizationHeader.substring(BEARER_TOKEN_PREFIX.length()).trim();

                if (!accessTokenUtilities.isTokenValid(jwtToken)) {
                    writeResponse(request, response, HttpStatus.UNAUTHORIZED);
                    return;
                }

                String username = accessTokenUtilities.getUsernameFromToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (isAccountActive(userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.warn("Provided token is expired or account is no longer active");
                    writeResponse(request, response, HttpStatus.UNAUTHORIZED);
                    return;
                }

            }

        } catch (UsernameNotFoundException ex) {
            log.error("Username isn't found, please ensure that account is created");
            writeResponse(request, response, HttpStatus.NOT_FOUND);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(writeUnauthorizedResponse(request));
    }

    private String writeUnauthorizedResponse(HttpServletRequest request) throws JsonProcessingException {
        ApiErrorDetails errorDetails = new ApiErrorDetails("Authorization failed", request.getRequestURI(), null, LocalDateTime.now());
        return objectMapper.writeValueAsString(errorDetails);
    }

    private boolean isAccountActive(UserDetails userDetails) {
        return userDetails.isAccountNonExpired()
                && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired()
                && userDetails.isEnabled();
    }
}
