package com.thehecklers.ssecforms;

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
public class SsecFormsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsecFormsApplication.class, args);
    }

}

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    private PasswordEncoder pwEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Bean
    UserDetailsService authentication() {
        UserDetails mark = User.builder()
                .username("mark")
                .password(pwEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails rob = User.builder()
                .username("rob")
                .password(pwEncoder.encode("password"))
                .roles("USER", "ADMIN")
                .build();

        System.out.println("Mark's password is: " + mark.getPassword());
        System.out.println("Rob's password is: " + rob.getPassword());

        return new InMemoryUserDetailsManager(mark, rob);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().mvcMatchers("/admin").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin();
    }
}

@RestController
class Stuff {
    @GetMapping("/")
    public String getStuff() {
        return "Here's some stuff!";
    }

    @GetMapping("/admin")
    public String getAdminInfo() { return "Super secret admin info: FOR ADMIN EYES ONLY!"; }
}

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//class User implements UserDetails {
//    private String id, username, password;
//    private boolean active;
//    private Collection<GrantedAuthority> roles;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return active;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return active;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return active;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return active;
//    }
//}