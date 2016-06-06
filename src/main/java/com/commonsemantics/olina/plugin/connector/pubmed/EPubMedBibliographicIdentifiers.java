package com.commonsemantics.olina.plugin.connector.pubmed;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dr. Paolo Ciccarese http://paolociccarese.info
 */
public enum EPubMedBibliographicIdentifiers {

    DOI("DOI", "Document Object Identifier"),
    PII("PII", "Publisher Item Identifier"),
    PMID("PMID", "PubMed Identifier"),
    PMCID("PMCID", "PubMed Central Identifier"),
    NIHMSID("NIHMSID", "NIH Manuscript Submission");

    private final String acronym;
    private final String name;

    EPubMedBibliographicIdentifiers(String acronym, String name) {
        this.acronym = acronym;
        this.name = name;
    }

    public static EPubMedBibliographicIdentifiers findByAcronym(String acronym) {
        for (EPubMedBibliographicIdentifiers i : EPubMedBibliographicIdentifiers.values()) {
            if(i.acronym.equals(acronym)) {
                return i;
            }
        }
        return null;
    }

    public static List<EPubMedBibliographicIdentifiers> list() {
        return Arrays.asList(EPubMedBibliographicIdentifiers.values());
    }
}
