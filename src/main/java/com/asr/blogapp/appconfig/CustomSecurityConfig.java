package com.asr.blogapp.appconfig;

import com.asr.blogapp.entity.MyUserEntity;
import com.asr.blogapp.repositry.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CustomSecurityConfig implements UserDetailsService {

    @Autowired
    private UserRepositry userRepositry;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MyUserEntity myUser = userRepositry.findByEmail(username);
        if (myUser == null) {
            throw new UsernameNotFoundException("User not found"); // ✅ MUST
        }


        List<SimpleGrantedAuthority> authorities =  myUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

       User user = new User(myUser.getEmail(), myUser.getPassword(), authorities);
        return user;
    }
}
