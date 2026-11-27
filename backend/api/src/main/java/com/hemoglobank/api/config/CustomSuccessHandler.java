package com.hemoglobank.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

//Classe que é responsavel para depois de uma tentativa bem sucedida para mandar cada um para seu devido endereço e com seu papel
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String destino;
        if (roles.contains("ROLE_DOADOR")) {
            destino = "/doador/painel";
        } else if (roles.contains("ROLE_MEDICO")) {
            destino = "/medico/painel";
        } else if (roles.contains("ROLE_GESTOR")) {
            destino = "/gestor/painel";
        } else {
            destino = "/";
        }
        response.sendRedirect(request.getContextPath() + destino);
    }
}
