package com.onlinevoting.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {

    @PostMapping("/v1/data")
    public String getData(){
        return "swamini";
    }
}
