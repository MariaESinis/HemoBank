package com.hemoglobank.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    // Bean do Codificador de Senha
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas liberadas para todos
                .requestMatchers("/", "/index", "/login", "/cadastro", "/api/cadastro/**", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/estoque").permitAll()
                
                // Rotas restritas por perfil
                .requestMatchers("/doador/**", "/api/agendamentos/**").hasAnyRole("DOADOR", "FUNCIONARIO", "ADMIN")
                // Adicionado "MEDICO" aqui para liberar o acesso ao painel do médico:
                .requestMatchers("/medico/**", "/api/exames/**", "/api/bolsas/**", "/api/doacoes/**").hasAnyRole("MEDICO", "FUNCIONARIO", "ADMIN")
                
                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email") // Informa ao Spring Security para usar o campo "email" no login
                .successHandler(customSuccessHandler) // Redireciona de forma inteligente baseado na role
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}