package com.onlinevoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "states")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private Boolean isActive;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public Country geCountry() {
        return country;
    }
}
