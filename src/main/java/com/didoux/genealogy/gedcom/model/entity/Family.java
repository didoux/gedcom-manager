package com.didoux.genealogy.gedcom.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "families")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family {

    @Id
    private String xrefId;  // @F1@ format from GEDCOM

    @ManyToMany
    @JoinTable(
            name = "family_children",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private Set<Individual> children = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "family_spouses",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "spouse_id")
    )
    private Set<Individual> spouses = new HashSet<>();

    // GEDCOM 7.0 marriage information
    private String marriageDate;
    private String marriagePlace;
    private String marriageType;

    private String divorceDate;
    private String divorcePlace;
}