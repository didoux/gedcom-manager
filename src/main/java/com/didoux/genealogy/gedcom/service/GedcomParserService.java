//package com.didoux.genealogy.gedcom.service;
//
//import org.folg.gedcom.model.Gedcom;
//import org.folg.gedcom.model.Person;
//import org.folg.gedcom.parser.ModelParser;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class GedcomParserService {
//
//    public void parseAndImport(MultipartFile file) throws Exception {
//        ModelParser parser = new ModelParser();
//        Gedcom gedcom = parser.parseGedcom(file.getInputStream());
//
//        // Process individuals
//        for (Person person : gedcom.getPeople()) {
//            // Convert GEDCOM Person to your Individual entity
//            processIndividual(person);
//        }
//
//        // Process families
//        for (org.folg.gedcom.model.Family family : gedcom.getFamilies()) {
//            processFamily(family);
//        }
//    }
//
//    private void processIndividual(Person person) {
//        // Implementation to convert and save
//    }
//
//    private void processFamily(org.folg.gedcom.model.Family family) {
//        // Implementation to convert and save
//    }
//}