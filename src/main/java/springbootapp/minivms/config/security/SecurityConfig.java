package springbootapp.minivms.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
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
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
        );

        return http.build();
    }
}
