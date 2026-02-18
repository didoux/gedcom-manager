package com.didoux.genealogy.gedcom.repository;

import com.didoux.genealogy.gedcom.model.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualRepository extends JpaRepository<Individual, String> {
    List<Individual> findBySurname(String surname);

}
