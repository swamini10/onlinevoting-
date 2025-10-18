package com.onlinevoting.dto;

public class StateDTO {

    private Long id;
    private String name;
    private Long countryId;

    public StateDTO(Long id, String name, Long countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCountryId() {
        return countryId;
    }
}
 
