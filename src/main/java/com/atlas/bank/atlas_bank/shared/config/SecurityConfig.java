package com.atlas.bank.atlas_bank.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Environment environment) throws Exception {
        boolean devProfile = environment.acceptsProfiles(Profiles.of("dev"));

        http
                .authorizeHttpRequests(auth -> {
                    //Herramientas de desarrollo (Actuator, Swagger UI) solo públicas en el perfil dev
                    if (devProfile) {
                        auth.requestMatchers(
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll();
                    } else {
                        auth.requestMatchers("/actuator/health").permitAll();
                    }
                })
                .authorizeHttpRequests(auth -> auth
                        //Accounts
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts/{ID}").hasAnyRole("USER", "ADMIN")

                        //Transactions
                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions/transfer").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions/{id}/transactions").hasAnyRole("USER", "ADMIN")

                        //H2
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess==null || realmAccess.get("roles")==null) {
                return Collections.emptyList();
            }
            var roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}
