package com.didoux.genealogy.gedcom.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "individuals")
@Data
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

    @ManyToMany(mappedBy = "children")
    private Set<Family> childOfFamilies = new HashSet<>();

    @ManyToMany(mappedBy = "spouses")
    private Set<Family> spouseOfFamilies = new HashSet<>();

    public enum Gender {
        M, F, U  // Male, Female, Unknown
    }
}
