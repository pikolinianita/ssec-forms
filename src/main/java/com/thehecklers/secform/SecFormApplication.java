package com.thehecklers.secform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SecFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecFormApplication.class, args);
    }

}

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder pwEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Bean
    UserDetailsService authentication() {
        UserDetails milanco = User.builder()
                .username("milanco")
                .password(pwEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails marija = User.builder()
                .username("marija")
                .password(pwEncoder.encode("StrongPassword"))
                .roles("USER", "ADMIN")
                .build();

        System.out.println("    Milanco's Password: " + milanco.getPassword());
        System.out.println("    Marija's Password: " + marija.getPassword());

        return new InMemoryUserDetailsManager(milanco, marija);

    }

    @Override    // authorization
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}

@RestController
class FormController {
    @GetMapping("/")
    String everyone() {
        return "Hello everyone!";
    }

    @GetMapping("/admin")
    String adminOnly() {
        return "<h1>Administrator's Page</h1>Greetings Admin!";
    }
}