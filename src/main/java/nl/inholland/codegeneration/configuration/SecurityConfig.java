package nl.inholland.codegeneration.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;

import nl.inholland.codegeneration.security.JwtFIlter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
        "/h2-console/**",
        "/authenticate/**"
    };
    private final JwtFIlter JwtFIlter;
    private final AuthenticationProvider AuthenticationProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(AuthenticationProvider)
                .addFilterBefore(JwtFIlter, UsernamePasswordAuthenticationFilter.class);
    
//        http.csrf().ignoringRequestMatchers("/h2-console/**");

        return http.build();
    }
  
//   @Bean
//     public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//         return http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(auth -> {
//                     auth.requestMatchers("/**").permitAll();
//                 })
//                 .httpBasic(withDefaults())
//                 .headers(header -> header.frameOptions().disable())
//                 .build();
//     }
  
     @Bean
     public WebSecurityCustomizer webSecurityCustomizer() {
         return (web) -> web.ignoring().requestMatchers("/h2-console/**");
     }
  
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("dev")
//                .password("dev123")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
    

    

 
}

    

   
