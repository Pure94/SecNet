package com.larpologic.secretnetwork.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/badania";

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin-panel";
                break;
            }
            if (grantedAuthority.getAuthority().equals("ROLE_HADES")) {
                redirectUrl = "/";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}