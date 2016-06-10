package com.commonsemantics.olina.plugin.connector.pubmed

import com.commonsemantics.olina.plugin.connector.bibliographic.BibliographicSearchResults
import com.commonsemantics.olina.plugin.connector.bibliographic.IBibliographicObject
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
        for(EPubMedBibliographicSearchType bi: EPubMedBibliographicSearchType.list()) {
            if(bi.isIdentifier) connectorsManagerService.registerBibliographicObjectIdentifier(bi.name, bi.name);
        }
    }

    @Override
    JSONObject getBibliographicObjectByIdAsJson(String bibliographicIdentifierAcronym, String id) {
        return convertExternalPubmedArticle(getBibliographicObjectById(bibliographicIdentifierAcronym, id).getResult());
    }

    @Override
    BibliographicSearchResults getBibliographicObjectById(String bibliographicIdentifierAcronym, String id) {
        return getPubMedManager().getBibliographicObjectById(fetchIdentifierByAcronym(bibliographicIdentifierAcronym), id);
    }

    @Override
    BibliographicSearchResults getBibliographicObjects(String bibliographicIdentifierAcronym, List<String> ids) {
        return getPubMedManager().getBibliographicObjects(fetchIdentifierByAcronym(bibliographicIdentifierAcronym).name, ids);
    }

    JSONArray getBibliographicObjectsAsJson(String bibliographicIdentifierAcronym, List<String> ids) {
        JSONArray res = new JSONArray();
        List<IBibliographicObject> results = getBibliographicObjects(fetchIdentifierByAcronym(bibliographicIdentifierAcronym).name, ids).getResults()
        for(IBibliographicObject result: results) {
            res.add(convertExternalPubmedArticle(result));
        }
        return res;
    }

    @Override
    BibliographicSearchResults getBibliographicObjects(String typeQuery, List<String> queryTerms, Integer range, Integer offset) {
        return null
    }

    @Override
    BibliographicSearchResults getBibliographicObjects(String typeQuery, List<String> queryTerms, Integer pubStartMonth, Integer pubStartYear, Integer pubEndMonth, Integer pubEndYear, Integer range, Integer offset) {
        return null
    }

    private PubMedManager getPubMedManager() {
        log.info("proxy: " + grailsApplication.config.olina.server.proxy.host + "-" + grailsApplication.config.olina.server.proxy.port) ;
        return new PubMedManager(
                (grailsApplication.config.olina.server.proxy.host.isEmpty()?"":grailsApplication.config.olina.server.proxy.host),
                (grailsApplication.config.olina.server.proxy.port.isEmpty()?"":grailsApplication.config.olina.server.proxy.port));
    }

    private EPubMedBibliographicSearchType fetchIdentifierByAcronym(String bibliographicIdentifierAcronym) {
        EPubMedBibliographicSearchType identifier = EPubMedBibliographicSearchType.findByAcronym(bibliographicIdentifierAcronym);
        if (identifier == null) throw new RuntimeException("Identifier name not found: " + bibliographicIdentifierAcronym)
        return identifier;
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
