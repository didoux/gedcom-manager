package com.didoux.genealogy.gedcom.repository;

import com.didoux.genealogy.gedcom.model.entity.Individual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class IndividualRepositoryTest {

    @Autowired
    private IndividualRepository repository;

    @Test
    void shouldSaveAndRetrieveIndividual() {
        // Given
        Individual individual = new Individual();
        individual.setXrefId("@I1@");
        individual.setFullName("John /Doe/");
        individual.setGivenNames("John");
        individual.setSurname("Doe");
        individual.setGender(Individual.Gender.M);

        // When
        Individual saved = repository.save(individual);
        Optional<Individual> retrieved = repository.findById("@I1@");

        // Then
        assertThat(saved.getXrefId()).isEqualTo("@I1@");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getFullName()).isEqualTo("John /Doe/");
    }

    @Test
    void shouldFindIndividualsBySurname() {
        // Given
        Individual john = createIndividual("@I1@", "John", "Doe");
        Individual jane = createIndividual("@I2@", "Jane", "Doe");
        Individual bob = createIndividual("@I3@", "Bob", "Smith");

        repository.save(john);
        repository.save(jane);
        repository.save(bob);

        // When
        var doesFound = repository.findBySurname("Doe");

        // Then
        assertThat(doesFound).hasSize(2);
        assertThat(doesFound)
            .extracting(Individual::getGivenNames)
            .containsExactlyInAnyOrder("John", "Jane");
    }

    private Individual createIndividual(String xref, String given, String surname) {
        Individual individual = new Individual();
        individual.setXrefId(xref);
        individual.setGivenNames(given);
        individual.setSurname(surname);
        individual.setFullName(given + " /" + surname + "/");
        individual.setGender(Individual.Gender.U);
        return individual;
    }
}