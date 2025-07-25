package com.swyp.catsgotogedog.common.config;

import java.util.List;

import com.swyp.catsgotogedog.common.security.filter.JwtTokenFilter;
import com.swyp.catsgotogedog.common.security.filter.OAuth2AutoLoginFilter;
import com.swyp.catsgotogedog.common.security.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtTokenFilter jwtTokenFilter;
    private final CatsgotogedogAuthenticationEntryPoint catsgotogedogAuthenticationEntryPoint;

    @Value("${allowed.origins.url}")
    private String allowedOriginsUrl;

    @Value("${allowed.http.methods}")
    private String allowedHttpMethods;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/oauth2/**",
                            "/login**",
                            "/error",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/api/user/reissue"
                            // todo : 인증이 필요 없는 API에 대해 추가 작성 필요
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(eh -> eh.authenticationEntryPoint(catsgotogedogAuthenticationEntryPoint))
                .addFilterBefore(new OAuth2AutoLoginFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2LoginSuccessHandler))
                .addFilterBefore(jwtTokenFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(allowedOriginsUrl));
        configuration.setAllowedMethods(List.of(allowedHttpMethods));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
