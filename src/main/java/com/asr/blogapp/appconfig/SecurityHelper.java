package com.asr.blogapp.appconfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static String currentLoggedInUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            return null;
        }
        return authentication.getName();

    }
}
