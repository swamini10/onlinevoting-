package com.onlinevoting.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.onlinevoting.dto.BaseDTO;
import com.onlinevoting.model.UserRole;
import com.onlinevoting.repository.UserRoleRepository;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * return all user roles as BaseDTO list
     * @return
     */
    public List<BaseDTO> getAllUserRoles() {
       List<BaseDTO> baseDTOs =  new ArrayList<>();

       List<UserRole> roles = userRoleRepository.findAll();
        for(UserRole role : roles){
            baseDTOs.add(new BaseDTO(role.getId(), role.getName()));
        }
        return baseDTOs;
    }

}
