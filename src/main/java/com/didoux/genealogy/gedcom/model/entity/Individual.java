package com.didoux.genealogy.gedcom.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "individuals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Individual {
    @Id
    private String xrefId;  // GEDCOM reference like @I1@

    @Column(length = 500)
    private String fullName;

    private String givenNames;
    private String surname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;
    private String birthPlace;

    private LocalDate deathDate;
    private String deathPlace;

    @Builder.Default
    private Boolean living = true;

    @ManyToMany(mappedBy = "children")
    private Set<Family> childOfFamilies = new HashSet<>();

    @ManyToMany(mappedBy = "spouses")
    private Set<Family> spouseOfFamilies = new HashSet<>();

    public enum Gender {
        M, F, X, U  // Male, Female, Unknown
    }

    // Convenience method for isLiving() - Lombok generates getLiving()
    public boolean isLiving() {
        return living != null && living;
    }

    @PrePersist
    @PreUpdate
    private void setDefaults() {
        if (living == null) {
            living = true;
        }
        if (childOfFamilies == null) {
            childOfFamilies = new HashSet<>();
        }
        if (spouseOfFamilies == null) {
            spouseOfFamilies = new HashSet<>();
        }
    }
}
