package nl.inholland.codegeneration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import nl.inholland.codegeneration.security.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfiguration {
    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // no CSRF protection needed
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no sessions needed
        http.authorizeRequests()
                .antMatchers("/authenticate").permitAll() // allow some URLs for unauthenticated users
                .antMatchers("/h2-console/**/**").permitAll()
                .anyRequest().authenticated(); // disallow any other URL for unauthenticated users
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // add the filter

    }
}
