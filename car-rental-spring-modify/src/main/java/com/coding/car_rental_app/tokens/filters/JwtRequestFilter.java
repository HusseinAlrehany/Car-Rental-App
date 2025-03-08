package com.coding.car_rental_app.tokens.filters;

import com.coding.car_rental_app.tokens.jwtservice.AppUserDetailsService;
import com.coding.car_rental_app.tokens.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String jwtToken;
        final String userEmail;
        //logging the request URL
        System.out.println("Request URL-> " + request.getRequestURI());

        // Extracting JWT from Cookie instead of Authorization header
        jwtToken = extractJwtFromCookie(request);

        if (jwtToken == null || jwtToken.isBlank()) {
            System.out.println("NO JWT TOKEN FOUND IN THE REQUEST");
            filterChain.doFilter(request, response);
            return;
        }

        //the extracted jwt from cookie
        System.out.println("EXTRACTED JWT TOKEN -> " + jwtToken);

        userEmail = jwtUtils.extractUsername(jwtToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(userEmail);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            } else {
                System.out.println("JWT token is INVALID OR EXPIRED");
            }
        }

        filterChain.doFilter(request, response);
    }


     // Extracts the JWT token from the HttpOnly cookie
    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Received Cookie " + cookie.getName() + " " + cookie.getValue());
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        } else {
            System.out.println("No Cookies received from client");
        }
        return null;
    }
}


