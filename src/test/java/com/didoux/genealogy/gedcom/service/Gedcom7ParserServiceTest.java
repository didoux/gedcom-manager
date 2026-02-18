package com.didoux.genealogy.gedcom.service;

import com.didoux.genealogy.gedcom.model.entity.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class Gedcom7ParserServiceTest {

    private Gedcom7ParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new Gedcom7ParserService();
    }

    @Test
    @DisplayName("Should parse GEDCOM 7.0 header")
    void shouldParseGedcom7Header() {
        // Given - GEDCOM 7.0 format header
        String gedcomContent = """
            HEAD
            GEDC
            GEDC.VERS 7.0
            SCHMA
            SCHMA.TAG _MYEXT https://example.com/myext
            """;

        // When
        var gedcomFile = parserService.parseHeader(gedcomContent);

        // Then
        assertThat(gedcomFile).isNotNull();
        assertThat(gedcomFile.getVersion()).isEqualTo("7.0");
        assertThat(gedcomFile.hasExtensions()).isTrue();
    }

    @Test
    @DisplayName("Should parse GEDCOM 7.0 individual with personal names")
    void shouldParseGedcom7Individual() {
        // Given - GEDCOM 7.0 individual format
        String gedcomContent = """
            HEAD
            GEDC
            GEDC.VERS 7.0
            
            @I1@ INDI
            PERS.NAME
            PERS.NAME.GIVN John Robert
            PERS.NAME.SURN Doe
            PERS.NAME.TYPE BIRTH
            SEX M
            BIRT
            BIRT.DATE 1950-01-01
            BIRT.PLAC Boston, MA, USA
            DEAT
            DEAT.DATE 2020-03-15
            DEAT.PLAC Chicago, IL, USA
            
            TRLR
            """;

        // When
        Individual individual = parserService.parseIndividualFromString(gedcomContent);

        // Then
        assertThat(individual).isNotNull();
        assertThat(individual.getXrefId()).isEqualTo("@I1@");
        assertThat(individual.getGivenNames()).isEqualTo("John Robert");
        assertThat(individual.getSurname()).isEqualTo("Doe");
        assertThat(individual.getGender()).isEqualTo(Individual.Gender.M);
        assertThat(individual.getBirthDate()).isEqualTo("1950-01-01");
        assertThat(individual.getBirthPlace()).isEqualTo("Boston, MA, USA");
        assertThat(individual.getDeathDate()).isEqualTo("2020-03-15");
        assertThat(individual.isLiving()).isFalse();
    }

    @Test
    @DisplayName("Should handle GEDCOM 7.0 date formats")
    void shouldHandleGedcom7DateFormats() {
        // Given - Various GEDCOM 7.0 date formats
        String gedcomContent = """
            @I1@ INDI
            BIRT
            BIRT.DATE 1950-01-01
            
            @I2@ INDI
            BIRT
            BIRT.DATE.TIME 1975-06-10T14:30:00Z
            
            @I3@ INDI
            BIRT
            BIRT.DATE.PHRASE About 1900
            BIRT.DATE.PERIOD FROM 1899 TO 1901
            """;

        // When
        var individuals = parserService.parseMultipleIndividuals(gedcomContent);

        // Then
        assertThat(individuals).hasSize(3);
        assertThat(individuals.get(0).getBirthDate()).isEqualTo("1950-01-01");
        assertThat(individuals.get(1).getBirthDateTime()).isEqualTo("1975-06-10T14:30:00Z");
        assertThat(individuals.get(2).getBirthDatePhrase()).isEqualTo("About 1900");
    }
}