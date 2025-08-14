package com.onlinevoting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {

    @GetMapping("/v1/data")
    public String getData(){
        return "sss";
    }
}
