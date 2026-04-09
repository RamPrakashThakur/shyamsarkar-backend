package com.shyamsarkar.buildingmaterials.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/debug-auth")
    public Object debugAuth(Authentication authentication) {
        if (authentication == null) {
            return "NO AUTHENTICATION";
        }
        return authentication.getAuthorities();
    }
}
