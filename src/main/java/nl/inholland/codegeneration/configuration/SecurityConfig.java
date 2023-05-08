package nl.inholland.codegeneration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


import nl.inholland.codegeneration.security.JwtFIlter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    private static final String[] AUTH_WHITELIST = {
        
        "/h2-console/**",
        "/authenticate/**"
        };
    private final JwtFIlter JwtFIlter;
    private final AuthenticationProvider AuthenticationProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(AuthenticationProvider)
                .addFilterBefore(JwtFIlter, UsernamePasswordAuthenticationFilter.class);
    
    
        return http.build();
    }

    
//change code to use filterchain isntead of this grose code

 
}
 