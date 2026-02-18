
package com.onlinevoting.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "party")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Party extends AuditDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Party name is mandatory")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Logo URL is mandatory")
    @Column(name = "logo_url", nullable = false, length = 512)
    private String logoUrl;

    @NotBlank(message = "Party president is mandatory")
    @Column(name = "president_name", nullable = false, length = 255)
    private String presidentName;

    public Party orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}