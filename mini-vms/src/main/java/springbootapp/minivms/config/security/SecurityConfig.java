package springbootapp.minivms.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


// This class defines the application security rules
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomLoginSuccessHandler customLoginSuccessHandler) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/", "/index", "/login", "/register", "/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/buyer/**").hasRole("BUYER")
                        .requestMatchers("/supplier/**").hasRole("SUPPLIER")
                        .requestMatchers("/worker/**").hasRole("WORKER")
                        .anyRequest().authenticated()
        ).formLogin(
                form -> form.loginPage("/login")
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
        ).exceptionHandling(
                 ex -> ex.accessDeniedPage("/access-denied")
        ).logout(
                        logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
        );

        return http.build();
    }
}
