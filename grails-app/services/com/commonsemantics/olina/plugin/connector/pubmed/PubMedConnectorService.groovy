package com.commonsemantics.olina.plugin.connector.pubmed

import com.commonsemantics.olina.plugin.connector.bibliographic.BibliographicIdentifier
import com.commonsemantics.olina.plugin.connector.bibliographic.IBibliographyManagetConnector
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.PubMedManager
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.PubmedArticleObject
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml.PublicationType
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

/**
 * @author Dr. Paolo Ciccarese http://paolociccarese.info
 */
class PubMedConnectorService implements IBibliographyManagetConnector {

    def grailsApplication;
    def connectorsManagerService;

    /*
        The initialize is going to register the bibliographic identifiers
        that are understood/managed by this plugin
     */
    @Override
    void initialize() {
        // http://www.biosciencewriters.com/Digital-identifiers-of-scientific-literature-PMID-PMCID-NIHMS-DOI-and-how-to-use-them.aspx
        for(EPubMedBibliographicIdentifiers bi: EPubMedBibliographicIdentifiers.list()) {
            connectorsManagerService.registerBibliographicObjectIdentifier(bi.acronym, bi.name);
        }
    }

    @Override
    JSONObject getArticleById(String bibliographicIdentifierAcronym, String id) {
        log.info("proxy: " + grailsApplication.config.olina.server.proxy.host + "-" + grailsApplication.config.olina.server.proxy.port) ;
        BibliographicIdentifier bi = connectorsManagerService.getBibliographicIdentifier(bibliographicIdentifierAcronym);
        PubMedManager pa = new PubMedManager(
                (grailsApplication.config.olina.server.proxy.host.isEmpty()?"":grailsApplication.config.olina.server.proxy.host),
                (grailsApplication.config.olina.server.proxy.port.isEmpty()?"":grailsApplication.config.olina.server.proxy.port));

        PubmedArticleObject xpa = pa.getArticleByPubMedId(id);
        return convertExternalPubmedArticle(xpa);
    }

    /**
     * Converts a PubMed metadata entry into json
     * @param epa	The PubMed record
     * @return	The json representation of the PubMed record
     */
    private JSONObject convertExternalPubmedArticle(PubmedArticleObject epa) {

        try {
            JSONObject pubmedArticle = new JSONObject();
            pubmedArticle.put("id", epa.getId());
            pubmedArticle.put("url", "http://www.ncbi.nlm.nih.gov/pubmed/" + epa.getAuthoritativeId());
            pubmedArticle.put("title", epa.getTitle());
            pubmedArticle.put("source", "http://dbpedia.org/resource/PubMed");
            pubmedArticle.put("type", epa.getOntologyType());
            pubmedArticle.put("pmid", epa.getAuthoritativeId());
            pubmedArticle.put("pmcid", epa.getPMC());
            pubmedArticle.put("doi", epa.getDOI());
            pubmedArticle.put("publicationAuthors", epa.getAuthorNamesString());
            pubmedArticle.put("publicationInfo", epa.getJournalPublicationInfoString());
            pubmedArticle.put("publicationDate", epa.getPublicationDateString());
            pubmedArticle.put("journalName",epa.getJournalName());
            pubmedArticle.put("journalIssn", epa.getISSN());

            JSONArray pubmedArticleTypes = new JSONArray();
            List<PublicationType> types = epa.getPublicationTypes();
            for(PublicationType type: types) {
                if(type.getvalue().toString().trim().equals("Journal Article") || type.getvalue().toString().trim().equals("Letter")) {
                    pubmedArticleTypes.add("Journal Article");
                    //break;
                } else {
                    pubmedArticleTypes.add("Other type");
                }
            }
            pubmedArticle.put("types", pubmedArticleTypes);


            JSONArray authorNamesList = new JSONArray();
            List<PubmedArticleObject.ExternalAuthor> authors = epa.getHasAuthors();
            for(PubmedArticleObject.ExternalAuthor author: authors) {
                JSONObject personName = new JSONObject();
                personName.put("firstName", author.getFirstName());
                personName.put("lastName", author.getSurname());
                personName.put("fullName", author.getFullname());
                authorNamesList.add(personName);
            }


            pubmedArticle.put("authorNames", authorNamesList);
            return pubmedArticle;
        } catch (Exception e) {
            log.error "Error: ${e.message}", e
            e.printStackTrace();
            return null;
        }
    }
}
