package com.onlinevoting.service;

import java.util.List;

import com.onlinevoting.dto.MenuDto;

public interface MenuService {
    List<MenuDto> getMenuItemsByRoleId(Long roleId);
    List<MenuDto> getMenuItemsByUserId(String emailId);
    List<MenuDto> getMenuItemsForCurrentUser();
}
