package com.hemoglobank.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Criptografa a senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomSuccessHandler customSuccessHandler) throws Exception {
        http
            // CSRF ligado: os formulários Thymeleaf (th:action) enviam o token automaticamente.
            // Ficam de fora apenas o cadastro público (JSON, sem sessão) e o console do H2.
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/cadastro/**", "/h2-console/**"))
            // O console do H2 usa frames da mesma origem
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers("/", "/login", "/cadastro", "/error",
                        "/api/cadastro/**", "/api/estoque",
                        "/css/**", "/js/**", "/images/**").permitAll()

                // Rotas restritas por perfil
                .requestMatchers("/doador/**").hasRole("DOADOR")
                .requestMatchers("/medico/**", "/api/bolsas/**", "/api/doacoes/**").hasRole("MEDICO")
                .requestMatchers("/gestor/**", "/usuarios/**", "/h2-console/**").hasRole("GESTOR")
                // Consultas JDBC (schemas reporting.* e hr.*)
                .requestMatchers("/api/relatorios/**").hasAnyRole("GESTOR", "MEDICO")
                .requestMatchers("/api/funcionarios/**").hasRole("GESTOR")

                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")//usa email como parametro
                .successHandler(customSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
