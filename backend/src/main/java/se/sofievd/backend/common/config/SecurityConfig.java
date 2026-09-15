package se.sofievd.backend.common.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<JwtDecoder> jwtDecoder
    ) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        if (jwtDecoder.getIfAvailable() == null) {
            http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
            return http.build();
        }

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/preferences/**").authenticated()
                .anyRequest().permitAll()
        );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> { }));
        return http.build();
    }

    @Bean
    @ConditionalOnExpression("'${CLERK_JWT_ISSUER_URI:${CLERK_ISSUER_URL:}}' != ''")
    JwtDecoder jwtDecoder(
            @Value("${CLERK_JWT_ISSUER_URI:${CLERK_ISSUER_URL:}}") String issuerUrl
    ) {
        return JwtDecoders.fromIssuerLocation(issuerUrl);
    }
}