package com.sparta.cmung_project.security;

import com.sparta.cmung_project.jwt.filter.JwtAuthFilter;
import com.sparta.cmung_project.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder (); }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {return (web) -> web.ignoring ().antMatchers ( "/h2-console/**" ); }

    //cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns( Arrays.asList("*") );
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*", "POST", "GET", "DELETE", "PUT", "PATCH", "OPTIONS"));  // 프론트에서 보내는 CRUD 허용
        configuration.setAllowedHeaders(Arrays.asList("*", "Access_Token")); // 프론트에서 보내는 모든 해더 허용
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader ( "*" );
        configuration.addExposedHeader("Access_Token"); // Axios 에서는 이런식으로 Access_token으로 지정해줘야 보임 *사용불가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CORS
        http.cors();
        // CSRF
        http.csrf ().disable ();
        http.sessionManagement ().sessionCreationPolicy ( SessionCreationPolicy.STATELESS );

        http.authorizeRequests ()
                .antMatchers ( "/auth/**" ).permitAll ()
                .antMatchers ( PERMIT_URL_ARRAY ).permitAll ()
                .anyRequest ().authenticated ()
                .and ().addFilterBefore ( new JwtAuthFilter ( jwtUtil ), UsernamePasswordAuthenticationFilter.class );

        return http.build ();
    }


}
