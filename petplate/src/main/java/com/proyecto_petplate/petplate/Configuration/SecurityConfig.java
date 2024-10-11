package com.proyecto_petplate.petplate.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF con lambda
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()  // Permitir acceso a cualquier otra ruta sin autenticación
        )
        .httpBasic(withDefaults());  // Habilitar la autenticación HTTP básica con configuración predeterminada

        return http.build();  // Construir la cadena de filtros de seguridad
    }
}
