package com.didoux.genealogy.gedcom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GedcomFile {
    private String version;
    private boolean hasExtensions;
    private String charset = "UTF-8";  // GEDCOM 7.0 default
    private String language;
    private String sourceSystem;
}