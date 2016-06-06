package com.commonsemantics.olina.plugin.connector.pubmed.dtd150101;

import com.commonsemantics.olina.plugin.connector.pubmed.EPubMedBibliographicIdentifiers;
import com.commonsemantics.olina.plugin.connector.pubmed.PubMedQueryTermBuilder;
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml.PubmedArticle;
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml.PubmedArticleSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dr. Paolo Ciccarese http://paolociccarese.info
 */
public class PubMedManager {

    // Supported query types
    public static final String QUERY_TYPE_TITLE = "title";
    //public static final String QUERY_TYPE_TITLE_AND_ABSTRACT = "titleAndAbstract";
    public static final String QUERY_TYPE_PUBMED_CENTRAL_ID = "pubmedCentralId";

    /**
     * This is used as a place holder for those requested items that don't have
     * a suitable identifier.
     */
    public static final String UNRECOGNIZED = "UNRECOGNIZED";

    private static final Log logger = LogFactory.getLog(PubMedManager.class);

    /**
     * The MesH publication types that we currently support. We are hard coding them currently
     */
    private static final String[] ALLOWABLE_PUBLICATION_TYPES = {"Journal Article","Comment","News","Newspaper Article","Letter"};
    /**
     * Creating the PubMed Search Agent as singleton
     */
    private PubMedSearchAgent pubmedSearchAgent = PubMedSearchAgent.getInstance();

    private static final int DEFAULT_MAX_NUMBER_SEARCH_RESULTS = 90;
    private int maxNumberSearchResults = DEFAULT_MAX_NUMBER_SEARCH_RESULTS;

    public PubMedManager(String proxyIp, String proxyPort) {
        pubmedSearchAgent.setProxyIp(proxyIp);
        pubmedSearchAgent.setProxyPort(proxyPort);
    }

    public int getMaxNumberSearchResults() {
        return maxNumberSearchResults;
    }

    public void setMaxNumberSearchResults(int maxNumberSearchResults) {
        this.maxNumberSearchResults = maxNumberSearchResults;
    }

    public PubmedArticleObject getArticleByPubMedId(String pubmedId){
        List<String> pubmedIds = new ArrayList<String>();
        pubmedIds.add(pubmedId);

        List<PubmedArticleObject> queryResults = this.getArticlesByPubMedIds(pubmedIds);
        return (queryResults.size() == 0)? null : queryResults.get(0);
    }

    public List<PubmedArticleObject> getArticlesByPubMedIds(List<String>pubmedIds) {
        try {
            PubmedArticleSet results = pubmedSearchAgent.fetchPubmedDocuments(pubmedIds);
            return this.convertToExternalPubmedArticles(results);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*
     * Search of PubMed articles records with pagination.
     *
     * (non-Javadoc)
     * @see org.mindinformatics.services.connector.pubmed.dataaccess.IPubmedArticleManager#searchPubmedArticles(java.lang.String, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    public  Map<Map<String,String>, List<PubmedArticleObject>> searchPubmedArticles(
            String typeQuery, List<String> queryTerms,
            Integer pubStartMonth, Integer pubStartYear,
            Integer pubEndMonth, Integer pubEndYear,
            Integer range, Integer offset) {

        PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
        // We are restricting the  publication types to a restricted list
        // This restriction might be reconsidered
        List<String> pubTypes = new ArrayList<String>();
        for(String theType : ALLOWABLE_PUBLICATION_TYPES){
            pubTypes.add(theType.replace(" ", "+"));
        }

        if(CollectionUtils.isNotEmpty(queryTerms)) {
            if(typeQuery.equals(QUERY_TYPE_TITLE)) {
                termBuilder.addJournalArticleTitleWords(queryTerms);
            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMID)) {
                termBuilder.addPubmedIds(queryTerms);
                this.maxNumberSearchResults = queryTerms.size();
            }else if(typeQuery.equals(EPubMedBibliographicIdentifiers.DOI)) {
                termBuilder.add(queryTerms);
            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMCID)) {
                termBuilder.add(queryTerms);
            } else {
                termBuilder.add(queryTerms);
            }
        }

        termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);

        // The Integer is the total number of results
        int offsetValidated = 0;
        int maxResultsValidated = 0;
        Map<Integer,PubmedArticleSet> results;
        if(range>0 && offset>=0) {
            offsetValidated = offset;
            maxResultsValidated = range;
            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), maxResultsValidated, offsetValidated);
        } else {
            // This is just returning the first group of results
            maxResultsValidated = this.getMaxNumberSearchResults();
            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);
        }

        Map<Map<String,String>, List<PubmedArticleObject>> mapToReturn = new HashMap<Map<String,String>, List<PubmedArticleObject>>();

        if (results == null){
            Map<String,String> stats = new HashMap<String,String>();
            stats.put("total", Integer.toString(0));
            stats.put("exception", "PubmedArticleManagerImpl.searchPubmedArticles().nullresults");

            mapToReturn.put(stats, new ArrayList<PubmedArticleObject>());
            return mapToReturn;
        }

        int totalResults = results.keySet().iterator().next();
        List<PubmedArticleObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());

        Map<String,String> stats = new HashMap<String,String>();
        stats.put("total", Integer.toString(totalResults));
        stats.put("range", Integer.toString(maxResultsValidated));
        stats.put("offset", Integer.toString(offsetValidated));

        mapToReturn.put(stats, convertedResults);
        return mapToReturn;
    }

    /**
     * Converts the set of article results into a list of suitable objects.
     * @param records	The list of records to convert
     * @return The list of objects
     */
    private List<PubmedArticleObject> convertToExternalPubmedArticles(PubmedArticleSet records) {
        List<PubmedArticleObject> articles = new ArrayList<>();
        for(Object currentArticle : records.getPubmedArticleOrPubmedBookArticle()){
            if (currentArticle == null){
                articles.add(null);
            } else {
                if(currentArticle instanceof PubmedArticle)
                    articles.add(new PubmedArticleObject((PubmedArticle)currentArticle));
            }
        }
        return articles;
    }

    /*
     * (non-Javadoc)
     * @see org.mindinformatics.services.connector.pubmed.dataaccess.IPubmedArticleManager#getPubmedArticles(java.lang.String, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    public List<PubmedArticleObject> getPubmedArticles(
            String typeQuery, List<String>titleAndAbstractWords,
            Integer pubStartMonth, Integer pubStartYear,
            Integer pubEndMonth, Integer pubEndYear) {

        PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
        //We are restricting the  publication types to include these
        List<String> pubTypes = new ArrayList<String>();
        for(String theType : ALLOWABLE_PUBLICATION_TYPES){
            pubTypes.add(theType.replace(" ", "+"));
        }

        // TODO To be considered
        // termBuilder.addPublicationTypes(pubTypes);
        if(CollectionUtils.isNotEmpty(titleAndAbstractWords)){
            if(typeQuery.equals(QUERY_TYPE_TITLE))
                termBuilder.addJournalArticleTitleWords(titleAndAbstractWords);
            else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMID)) {
                termBuilder.addPubmedIds(titleAndAbstractWords);
                this.maxNumberSearchResults = titleAndAbstractWords.size();
            }else if(typeQuery.equals(EPubMedBibliographicIdentifiers.DOI)) {
                termBuilder.add(titleAndAbstractWords);
            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMCID)) {
                termBuilder.add(titleAndAbstractWords);
            } else if(typeQuery.equals(QUERY_TYPE_PUBMED_CENTRAL_ID)) {
                termBuilder.add(titleAndAbstractWords);
            }
        }
        termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);

        PubmedArticleSet results = pubmedSearchAgent.fetch(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);
        if (results == null){
            return null;
        }
        List<PubmedArticleObject> convertedResults = convertToExternalPubmedArticles(results);
        return convertedResults;
    }


//	public List<ExternalPubmedArticle> getPubmedArticles(
//			String typeQuery, List<String>titleAndAbstractWords,
//			Integer pubStartMonth, Integer pubStartYear,
//			Integer pubEndMonth, Integer pubEndYear,
//			String maxResults, String offset){
//
//		PubmedQueryTermBuilder termBuilder = new PubmedQueryTermBuilder();
//		//We are restricting the  publication types to include these
//		List<String> pubTypes = new ArrayList<String>();
//		for(String theType : ALLOWABLE_PUBLICATION_TYPES){
//			pubTypes.add(theType.replace(" ", "+"));
//		}
//		//termBuilder.addPublicationTypes(pubTypes);
//		if(CollectionUtils.isNotEmpty(titleAndAbstractWords)){
//			if(typeQuery.equals("titleAndAbstract"))
//				termBuilder.addTitleAndAbstractSearchWords(titleAndAbstractWords);
//			else if(typeQuery.equals("pubmedIds")) {
//				termBuilder.addPubmedIds(titleAndAbstractWords);
//				this.maxNumberSearchResults = titleAndAbstractWords.size();
//			}else if(typeQuery.equals("dois")) {
//				termBuilder.add(titleAndAbstractWords);
//			} else if(typeQuery.equals("pubmedCentralIds"))
//				termBuilder.add(titleAndAbstractWords);
//		}
//		termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);
//
//		System.out.println(termBuilder.toString());
//
//		PubmedArticleSet results = pubmedSearchAgent.fetch(termBuilder.toString(), Integer.parseInt(maxResults), Integer.parseInt(offset));
//		if (results == null){
//			return null;
//		}
//		List<ExternalPubmedArticle> convertedResults = convertToExternalPubmedArticles(results);
//		/*
//		CollectionUtils.filter(convertedResults, new Predicate(){
//			public boolean evaluate(Object object) {
//				return ((ExternalPubmedArticle)object).getOntologyType() != null;
//
//			}});
//			*/
//		return convertedResults;
//	}




//	/* (non-Javadoc)
//	 * @see org.mindinformatics.swan.pubmed.dataaccess.PubmedArticleDAO#getPubmedArticles(java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
//	 */
//	public List<ExternalPubmedArticle> getPubmedArticles(List<String>pubmedIdList,List<String>journalArticleTitleWords,List<String>titleAndAbstractWords,List<String>meshTerms,List<String>authorNames,Integer pubStartMonth,Integer pubStartYear,Integer pubEndMonth,Integer pubEndYear){
//		PubmedQueryTermBuilder termBuilder = new PubmedQueryTermBuilder();
//		//We are restricting the  publication types to include these
//		List<String> pubTypes = new ArrayList<String>();
//		for(String theType : ALLOWABLE_PUBLICATION_TYPES){
//			pubTypes.add(theType.replace(" ", "+"));
//		}
//		termBuilder.addPublicationTypes(pubTypes);
//
//		if (CollectionUtils.isNotEmpty(journalArticleTitleWords)){
//			termBuilder.addJournalArticleTitleWords(journalArticleTitleWords);
//		}
//		if(CollectionUtils.isNotEmpty(titleAndAbstractWords)){
//			termBuilder.addTitleAndAbstractSearchWords(titleAndAbstractWords);
//		}
//		if (CollectionUtils.isNotEmpty(meshTerms)){
//			termBuilder.addMeshTerms(meshTerms);
//		}
//		if(CollectionUtils.isNotEmpty(authorNames)){
//			termBuilder.addAuthors(authorNames);
//		}
//		if (CollectionUtils.isNotEmpty(pubmedIdList)){
//			termBuilder.addPubmedIds(pubmedIdList);
//		}
//		termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);
//
//		PubmedArticleSet results = pubmedSearchAgent.fetch(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);
//
//		if (results == null){
//			return null;
//		}
//		List<ExternalPubmedArticle> convertedResults = convertToExternalPubmedArticles(results);
//		/*
//		CollectionUtils.filter(convertedResults, new Predicate(){
//			public boolean evaluate(Object object) {
//				return ((ExternalPubmedArticle)object).getOntologyType() != null;
//
//			}});
//			*/
//		return convertedResults;
//	}
	/* (non-Javadoc)
	 * @see org.mindinformatics.swan.pubmed.dataaccess.PubmedArticleDAO#getPubmedArticles(java.util.List)
	 */

}
