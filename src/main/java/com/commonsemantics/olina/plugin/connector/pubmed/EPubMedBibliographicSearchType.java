package com.commonsemantics.olina.plugin.connector.pubmed;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dr. Paolo Ciccarese http://paolociccarese.info
 */
public enum EPubMedBibliographicSearchType {

    DOI("DOI", "Document Object Identifier", true),
    PII("PII", "Publisher Item Identifier", true),
    PMID("PMID", "PubMed Identifier", true),
    PMCID("PMCID", "PubMed Central Identifier", true),
    NIHMSID("NIHMSID", "NIH Manuscript Submission", true),
    TITLE("title", "Title", false),
    TITLE_AND_ABSTRACT("title-and-abstract", "Title and Abstract", false);

    public final String name;
    public final String description;
    public final boolean isIdentifier;

    EPubMedBibliographicSearchType(String name, String description, boolean isIdentifier) {
        this.name = name;
        this.description = description;
        this.isIdentifier = isIdentifier;
    }

    public static EPubMedBibliographicSearchType findByName(String name) {
        for (EPubMedBibliographicSearchType i : EPubMedBibliographicSearchType.values()) {
            if(i.name.equals(name)) {
                return i;
            }
        }
        return null;
    }

    public static List<EPubMedBibliographicSearchType> list() {
        return Arrays.asList(EPubMedBibliographicSearchType.values());
    }
}
