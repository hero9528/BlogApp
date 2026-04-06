package com.asr.blogapp.appconfig;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class MySecurityConfig {



    @Bean
    public PasswordEncoder  passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/blogdashborad/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/dashborad/admin").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/dashborad/guest").hasAuthority("ROLE_GUEST")
                .requestMatchers("/admin/category/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers
                        ("/blogdashborad/allPost","/blogdashborad/addPost",
                                "/blogdashborad/savedPost","/blogdashborad/allPost/view/**",
                                "/blogdashborad/deletePost/**", "/blogdashborad/updatePost/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_GUEST")
                .requestMatchers("/blogdashborad/approvePost/**", "/blogdashborad/rejectPost/**"
                        , "/blogdashborad/admin/allcomment", "/blogdashborad/admin/delete/**"

                ).hasAuthority("ROLE_ADMIN")



                .requestMatchers("/" , "/category/**" , "/searching" , "/blogfullview/**" ,
                        "/auth/signup-user", "/feedback", "/postfiles/**"

                ).permitAll());



        http.formLogin(from ->from.loginPage("/auth/login-user")
                .loginProcessingUrl("/login").defaultSuccessUrl("/", true)
                .failureUrl("/auth/login-user?error=true")
                .permitAll());

        http.logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login-user?logout=true")  // ✅ clean URL
                        .permitAll());
       // http.formLogin(withDefaults());
       // http.httpBasic(withDefaults());
        return http.build();
    }
}
