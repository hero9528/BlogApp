package com.asr.blogapp.service;

import com.asr.blogapp.dto.UserSignupDto;

public interface UserService {

    UserSignupDto  saveUser(UserSignupDto userSignupDto);
}
