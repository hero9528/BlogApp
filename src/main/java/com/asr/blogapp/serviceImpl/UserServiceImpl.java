package com.asr.blogapp.serviceImpl;


import com.asr.blogapp.dto.UserSignupDto;
import com.asr.blogapp.entity.MyUserEntity;
import com.asr.blogapp.entity.Role;
import com.asr.blogapp.repositry.RoleRepositry;
import com.asr.blogapp.repositry.UserRepositry;
import com.asr.blogapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositry  userRepositry;

    @Autowired
    private RoleRepositry roleRepositry;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserSignupDto saveUser(UserSignupDto userSignupDto) {

      MyUserEntity guestUser =  userRepositry.findByEmail(userSignupDto.getEmail());
      if (guestUser != null) {

          throw new RuntimeException("User already exists"+ userSignupDto.getEmail());
      }

        MyUserEntity guestUserExists = modelMapper.map(userSignupDto,MyUserEntity.class);

      guestUserExists.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));


      Role role =roleRepositry.findByName("ROLE_GUEST");

        List<Role>  roleList = Arrays.asList(role);

      guestUserExists.setRoles(roleList);

      MyUserEntity savedUser = userRepositry.save(guestUserExists);


        return modelMapper.map(savedUser,UserSignupDto.class);
    }
}
