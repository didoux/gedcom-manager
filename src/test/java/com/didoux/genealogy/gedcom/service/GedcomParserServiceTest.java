package com.didoux.genealogy.gedcom.service;

import com.didoux.genealogy.gedcom.model.entity.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class GedcomParserServiceTest {

    private GedcomParserService gedcomParserService;

    @BeforeEach
    void setUp() {
        gedcomParserService = new GedcomParserService();
    }

    @Test
    @DisplayName("Should parse individual with basic information from GEDCOM string")
    void shouldParseIndividualFromGedcomString() {
        // Given
        String gedcomContent = """
            0 @I1@ INDI
            1 NAME John /Doe/
            2 GIVN John
            2 SURN Doe
            1 SEX M
            1 BIRT
            2 DATE 1 JAN 1950
            2 PLAC Boston, MA, USA
            """;

        // When
        Individual individual = gedcomParserService.parseIndividualFromString(gedcomContent);

        // Then
        assertThat(individual).isNotNull();
        assertThat(individual.getXrefId()).isEqualTo("@I1@");
        assertThat(individual.getGivenNames()).isEqualTo("John");
        assertThat(individual.getSurname()).isEqualTo("Doe");
        assertThat(individual.getFullName()).isEqualTo("John /Doe/");
        assertThat(individual.getGender()).isEqualTo(Individual.Gender.M);
        assertThat(individual.getBirthDate()).isEqualTo("1 JAN 1950");
        assertThat(individual.getBirthPlace()).isEqualTo("Boston, MA, USA");
    }
}