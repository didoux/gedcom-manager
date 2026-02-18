package com.didoux.genealogy.gedcom.service;

import com.didoux.genealogy.gedcom.model.entity.Individual;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Gedcom7ParserService {

    private static final Pattern XREF_PATTERN = Pattern.compile("^@([^@]+)@\\s+(.+)$");
    private static final Pattern TAG_VALUE_PATTERN = Pattern.compile("^(\\d+)\\s+([^\\s]+)(?:\\s+(.*))?$");

    public Individual parseIndividualFromString(String gedcomContent) {
        Individual individual = new Individual();
        individual.setLiving(true);  // Default to living unless we find DEAT tag

        try (BufferedReader reader = new BufferedReader(new StringReader(gedcomContent))) {
            String line;
            String currentContext = null;
            int currentLevel = -1;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Parse level, tag, and value
                ParsedLine parsed = parseLine(line);
                if (parsed == null) continue;

                // Check if this is an individual record
                if (parsed.xref != null && "INDI".equals(parsed.tag)) {
                    individual.setXrefId("@" + parsed.xref + "@");
                    currentContext = "INDI";
                    currentLevel = parsed.level;
                } else if (individual.getXrefId() != null) {
                    // We're inside an individual record
                    if (parsed.level == 1) {
                        // Top-level individual tags
                        currentContext = parsed.tag;
                        switch (parsed.tag) {
                            case "NAME":
                                individual.setFullName(parsed.value);
                                parseNameComponents(parsed.value, individual);
                                break;
                            case "SEX":
                                individual.setGender(parseGender(parsed.value));
                                break;
                            case "BIRT":
                                currentContext = "BIRT";
                                break;
                            case "DEAT":
                                currentContext = "DEAT";
                                individual.setLiving(false);
                                break;
                        }
                    } else if (parsed.level == 2 && currentContext != null) {
                        // Sub-level tags
                        switch (currentContext + "." + parsed.tag) {
                            case "NAME.GIVN":
                                individual.setGivenNames(parsed.value);
                                break;
                            case "NAME.SURN":
                                individual.setSurname(parsed.value);
                                break;
                            case "BIRT.DATE":
                                individual.setBirthDate(parsed.value);
                                break;
                            case "BIRT.PLAC":
                                individual.setBirthPlace(parsed.value);
                                break;
                            case "DEAT.DATE":
                                individual.setDeathDate(parsed.value);
                                break;
                            case "DEAT.PLAC":
                                individual.setDeathPlace(parsed.value);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GEDCOM content", e);
        }

        return individual;
    }

    private ParsedLine parseLine(String line) {
        // Try to match line with XREF (@ID@ TAG VALUE)
        Matcher xrefMatcher = Pattern.compile("^0\\s+@([^@]+)@\\s+(.+)$").matcher(line);
        if (xrefMatcher.matches()) {
            ParsedLine parsed = new ParsedLine();
            parsed.level = 0;
            parsed.xref = xrefMatcher.group(1);
            parsed.tag = xrefMatcher.group(2);
            return parsed;
        }

        // Try to match regular line (LEVEL TAG VALUE)
        Matcher tagMatcher = Pattern.compile("^(\\d+)\\s+([^\\s]+)(?:\\s+(.*))?$").matcher(line);
        if (tagMatcher.matches()) {
            ParsedLine parsed = new ParsedLine();
            parsed.level = Integer.parseInt(tagMatcher.group(1));
            parsed.tag = tagMatcher.group(2);
            parsed.value = tagMatcher.group(3);
            return parsed;
        }

        return null;
    }

    private void parseNameComponents(String fullName, Individual individual) {
        if (fullName == null) return;

        // GEDCOM format: Given /Surname/
        Pattern namePattern = Pattern.compile("^(.+?)\\s*/([^/]+)/?.*$");
        Matcher matcher = namePattern.matcher(fullName);

        if (matcher.matches()) {
            String given = matcher.group(1).trim();
            String surname = matcher.group(2).trim();

            if (!given.isEmpty()) {
                individual.setGivenNames(given);
            }
            if (!surname.isEmpty()) {
                individual.setSurname(surname);
            }
        }
    }

    private Individual.Gender parseGender(String value) {
        if (value == null) return Individual.Gender.U;
        switch (value.toUpperCase()) {
            case "M": return Individual.Gender.M;
            case "F": return Individual.Gender.F;
            case "X": return Individual.Gender.X;
            default: return Individual.Gender.U;
        }
    }

    // Helper class for parsed line data
    private static class ParsedLine {
        int level;
        String xref;
        String tag;
        String value;
    }
}