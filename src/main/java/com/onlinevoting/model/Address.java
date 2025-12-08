package com.onlinevoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
public class Address extends AuditDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City cityId; // 1-1

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State stateId; // 1-1

    @Column(name = "zip_code", nullable = false)
    private String zipCode; // 1-1

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country countryId; // 1-1

    public Address(String street, City cityId, State stateId, String zipCode, Country countryId) {
        super();
        this.street = street;
        this.cityId = cityId;
        this.stateId = stateId;
        this.zipCode = zipCode;
        this.countryId = countryId;
    }

    public Address() {
    }
}
