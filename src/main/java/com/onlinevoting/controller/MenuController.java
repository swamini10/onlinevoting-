package com.onlinevoting.controller;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.MenuDto;
import com.onlinevoting.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MenuController {
    
    @Autowired
    private MenuService menuService;

    @GetMapping(path="/v1/menu/user", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getMenuForCurrentUser() {
        List<MenuDto> roleMenuItems = menuService.getMenuItemsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse(true, roleMenuItems, null));
    }
}
