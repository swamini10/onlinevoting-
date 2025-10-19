package com.onlinevoting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinevoting.dto.MenuDto;
import com.onlinevoting.model.Feature;
import com.onlinevoting.model.RoleFeatureMapping;
import com.onlinevoting.repository.FeatureRepository;
import com.onlinevoting.repository.RoleFeatureMappingRepository;

@Service
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private RoleFeatureMappingRepository roleFeatureMappingRepository;
    
    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public List<MenuDto> getMenuItemsByRoleId(Long roleId) {
        List<RoleFeatureMapping> featureMappings = roleFeatureMappingRepository.findByRoleIdAndIsActive(roleId, true);
        
        List<Long> featureIds = featureMappings.stream().map(RoleFeatureMapping::getFeatureId).collect(Collectors.toList());

        
        List<Feature> features = featureRepository.findByIdInAndIsActive(featureIds, true);

        return features.stream()
                .map(this::convertToMenuDto)
                .collect(Collectors.toList());
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


