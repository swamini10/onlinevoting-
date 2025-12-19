package com.onlinevoting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Service;

import com.onlinevoting.dto.MenuDto;
import com.onlinevoting.model.Feature;
import com.onlinevoting.model.RoleFeatureMapping;
import com.onlinevoting.model.UserDetail;
import com.onlinevoting.repository.FeatureRepository;
import com.onlinevoting.repository.RoleFeatureMappingRepository;
import com.onlinevoting.util.UserContextHelper;

@Service
public class MenuServiceImpl implements MenuService {
    
    private final RoleFeatureMappingRepository roleFeatureMappingRepository;
    private final FeatureRepository featureRepository;
    private final UserDetailService userDetailService;
    private final UserContextHelper userContextHelper;

    public MenuServiceImpl(RoleFeatureMappingRepository roleFeatureMappingRepository, FeatureRepository featureRepository, 
                          UserDetailService userDetailService, UserContextHelper userContextHelper) {
        this.roleFeatureMappingRepository = roleFeatureMappingRepository;
        this.featureRepository = featureRepository;
        this.userDetailService = userDetailService;
        this.userContextHelper = userContextHelper;
    }

    @Override
    public List<MenuDto> getMenuItemsByRoleId(Long roleId) {
        List<RoleFeatureMapping> featureMappings = roleFeatureMappingRepository.findByRoleIdAndIsActive(roleId, true);
        
        List<Long> featureIds = featureMappings.stream().map(RoleFeatureMapping::getFeatureId).collect(Collectors.toList());

        List<Feature> features = featureRepository.findByIdInAndIsActive(featureIds, true);
        Map<Long, List<MenuDto>> menuIdAndSubmenuMap = new TreeMap<>();
        List<MenuDto> menuDtos = new ArrayList<>();
        Map<Long, String> menuIdAndNameMap = new HashMap<>();
        
        for(Feature feature : features) {

            if(menuIdAndSubmenuMap.containsKey(feature.getMenuId())){
                List<MenuDto> dtos= menuIdAndSubmenuMap.get(feature.getMenuId());
                dtos.add(convertToMenuDto(feature));
                continue;
            }else {
                List<MenuDto> subMenuList = new ArrayList<>();
                subMenuList.add(convertToMenuDto(feature));
                menuIdAndSubmenuMap.put(feature.getMenuId(), subMenuList);
            }
            menuIdAndNameMap.put(feature.getMenuId(), feature.getMenuName());

        }

        for(Map.Entry<Long, List<MenuDto>> entry : menuIdAndSubmenuMap.entrySet()) {
            Long menuId = entry.getKey();
            List<MenuDto> subMenus = entry.getValue();
            MenuDto mainMenuDto = new MenuDto(menuId.toString(), menuIdAndNameMap.get(menuId), null, null, subMenus);
            menuDtos.add(mainMenuDto);

        }
        return menuDtos;
    }

    @Override
    public List<MenuDto> getMenuItemsByUserId(String emailId) {
        List<MenuDto> menuDtos = new ArrayList<>();
       UserDetail userDetail = userDetailService.getUserByEmail(emailId);
        if(userDetail != null) {
            Long roleId = userDetail.getRole().getId();
            menuDtos =  this.getMenuItemsByRoleId(roleId);
        }
        return menuDtos;
    }

    @Override
    public List<MenuDto> getMenuItemsForCurrentUser() {
        String emailId = userContextHelper.getCurrentUserEmail();
        if (emailId == null || emailId.isEmpty()) {
            throw new IllegalStateException("No user context found. User must be authenticated.");
        }
        return getMenuItemsByUserId(emailId);
    }
    
    private MenuDto convertToMenuDto(Feature feature) {
        return new MenuDto(
            feature.getId().toString(),
            feature.getName(),
            feature.getIcon() != null ? feature.getIcon() : "",
            feature.getUrl() != null ? feature.getUrl() : ""
        );
    }
}


