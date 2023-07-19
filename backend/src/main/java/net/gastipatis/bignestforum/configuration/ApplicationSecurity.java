package net.gastipatis.bignestforum.configuration;

import lombok.AllArgsConstructor;
import net.gastipatis.bignestforum.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurity {

//    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationConfiguration authConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf().disable()

                .authorizeHttpRequests()

                .requestMatchers("/signIn").permitAll()
                .requestMatchers("/signUp").permitAll()
                .requestMatchers("/refresh").permitAll()

                .requestMatchers("/api-docs").permitAll()
                .requestMatchers("/api-docs/*").permitAll()
                .requestMatchers("/docs.html").permitAll()
                .requestMatchers("/swagger-ui/*").permitAll()

                .requestMatchers(HttpMethod.GET, "/category").permitAll()
                .requestMatchers(HttpMethod.GET, "/*/post").permitAll()
                .requestMatchers(HttpMethod.POST, "/*/post").permitAll()
                .requestMatchers(HttpMethod.GET, "/post").permitAll()
                .requestMatchers(HttpMethod.GET, "/post/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/forum").permitAll()
                .requestMatchers(HttpMethod.GET, "/forum/*").permitAll()

                .requestMatchers(HttpMethod.GET, "/*/thread").permitAll()
                .requestMatchers(HttpMethod.POST, "/*/thread").permitAll()
                .requestMatchers(HttpMethod.GET, "/thread").permitAll()

                .requestMatchers(HttpMethod.PUT, "/account/password").authenticated()
                .requestMatchers("/account").hasRole("ADMIN")
                .requestMatchers("/admin/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/*/post").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/*/thread").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/forum").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/forum").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/forum").hasRole("ADMIN")
                .anyRequest().hasRole("ADMIN")
//                .requestMatchers("/**").permitAll()

                .and()

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        var config = new CorsConfiguration();
//        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        var configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.;
//    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfig.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean(AuthenticationManagerBuilder auth) throws Exception {
//        return auth
//                .userDetailsService(userDetailsService)
//                .and()
//                .build();
//    }
}
