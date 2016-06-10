package com.commonsemantics.olina.plugin.connector.pubmed.dtd150101;

import com.commonsemantics.olina.plugin.connector.bibliographic.BibliographicSearchResults;
import com.commonsemantics.olina.plugin.connector.bibliographic.IBibliographicObject;
import com.commonsemantics.olina.plugin.connector.pubmed.EPubMedBibliographicSearchType;
import com.commonsemantics.olina.plugin.connector.pubmed.PubMedQueryTermBuilder;
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml.PubmedArticle;
import com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml.PubmedArticleSet;
import org.apache.catalina.util.URLEncoder;
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

    public BibliographicSearchResults getBibliographicObjectById(EPubMedBibliographicSearchType bibliographicIdentifier, String id){
        List<String> ids = new ArrayList<>();
        ids.add(id);

        PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();

        if(bibliographicIdentifier == EPubMedBibliographicSearchType.PMID) {
            List<IBibliographicObject> queryResults = this.getArticlesByPubMedIds(ids);
            this.maxNumberSearchResults = ids.size();

            Map<String,String> details = new HashMap<String,String>();
            details.put("total", Integer.toString(queryResults.size()));
            details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
            details.put("offset", Integer.toString(0));

            BibliographicSearchResults res = new BibliographicSearchResults(details, queryResults);

            return res;
        } else if(bibliographicIdentifier == EPubMedBibliographicSearchType.PMID) {
            termBuilder.addPubmedIds(ids);
            this.maxNumberSearchResults = ids.size();
        }else if(bibliographicIdentifier == EPubMedBibliographicSearchType.DOI) {
            termBuilder.add(ids);
        } else if(bibliographicIdentifier == EPubMedBibliographicSearchType.PMCID) {
            termBuilder.add(ids);
        }
        Map<Integer,PubmedArticleSet> results;
        logger.error(termBuilder.toString());
        results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);

        int totalResults = results.keySet().iterator().next();
        List<IBibliographicObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());

        Map<String,String> details = new HashMap<String,String>();
        details.put("total", Integer.toString(totalResults));
        details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
        details.put("offset", Integer.toString(0));

        BibliographicSearchResults res = new BibliographicSearchResults(details, convertedResults);
        return res;
    }

    public IBibliographicObject getArticleByPubMedId(String pubmedId){
        return getBibliographicObjectById(EPubMedBibliographicSearchType.PMID, pubmedId).getResult();
    }

    public List<IBibliographicObject> getArticlesByPubMedIds(List<String>pubmedIds) {
        try {
            return this.convertToExternalPubmedArticles(pubmedSearchAgent.fetchPubmedDocuments(pubmedIds));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public  BibliographicSearchResults getBibliographicObjects(String typeQuery, List<String> queryTerms) {
        if(typeQuery.equals(EPubMedBibliographicSearchType.PMID.name)) {
            List<IBibliographicObject> queryResults = this.getArticlesByPubMedIds(queryTerms);
            this.maxNumberSearchResults = queryTerms.size();

            Map<String,String> details = new HashMap<String,String>();
            details.put("total", Integer.toString(queryResults.size()));
            details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
            details.put("offset", Integer.toString(0));

            BibliographicSearchResults res = new BibliographicSearchResults(details, queryResults);
            return res;
        } else if(typeQuery.equals(EPubMedBibliographicSearchType.PMCID.name)) {
            PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
            termBuilder.add(queryTerms);

            this.maxNumberSearchResults = queryTerms.size();
            Map<Integer,PubmedArticleSet> results;
            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);

            int totalResults = results.keySet().iterator().next();
            List<IBibliographicObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());

            Map<String,String> details = new HashMap<String,String>();
            details.put("total", Integer.toString(totalResults));
            details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
            details.put("offset", Integer.toString(0));

            BibliographicSearchResults res = new BibliographicSearchResults(details, convertedResults);
            return res;
        } else if(typeQuery.equals(EPubMedBibliographicSearchType.DOI.name)) {
            PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
            termBuilder.add(queryTerms);

            this.maxNumberSearchResults = queryTerms.size();
            Map<Integer,PubmedArticleSet> results;
            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);

            int totalResults = results.keySet().iterator().next();
            List<IBibliographicObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());

            Map<String,String> details = new HashMap<String,String>();
            details.put("total", Integer.toString(totalResults));
            details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
            details.put("offset", Integer.toString(0));

            BibliographicSearchResults res = new BibliographicSearchResults(details, convertedResults);
            return res;
        } else if(typeQuery.equals(EPubMedBibliographicSearchType.TITLE.name)) {
            logger.info("Querying title");
            PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
            termBuilder.addJournalArticleTitleWords(queryTerms);

            this.maxNumberSearchResults = 10;
            Map<Integer,PubmedArticleSet> results;
            logger.error(termBuilder.toString());
            results = pubmedSearchAgent.fetchWithStats((new URLEncoder()).encode(termBuilder.toString())  + "&field=title", this.getMaxNumberSearchResults(), 0);

            int totalResults = results.keySet().iterator().next();
            List<IBibliographicObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());

            Map<String,String> details = new HashMap<String,String>();
            details.put("total", Integer.toString(totalResults));
            details.put("range", Integer.toString(this.getMaxNumberSearchResults()));
            details.put("offset", Integer.toString(0));

            BibliographicSearchResults res = new BibliographicSearchResults(details, convertedResults);
            return res;
        }
        return null;
    }

    public  BibliographicSearchResults getBibliographicObjects(
            String typeQuery, List<String> queryTerms,
            Integer range, Integer offset) {
        return null;
    }

    public  BibliographicSearchResults getBibliographicObjects(
            String typeQuery, List<String> queryTerms,
            Integer pubStartMonth, Integer pubStartYear,
            Integer pubEndMonth, Integer pubEndYear,
            Integer range, Integer offset) {
        return null;
    }

//    /*
//     * Search of PubMed articles records with pagination.
//     *
//     * (non-Javadoc)
//     * @see org.mindinformatics.services.connector.pubmed.dataaccess.IPubmedArticleManager#searchPubmedArticles(java.lang.String, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
//     */
//    public  Map<Map<String,String>, List<PubmedArticleObject>> searchPubmedArticles(
//            String typeQuery, List<String> queryTerms,
//            Integer pubStartMonth, Integer pubStartYear,
//            Integer pubEndMonth, Integer pubEndYear,
//            Integer range, Integer offset) {
//
//        PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
//        // We are restricting the  publication types to a restricted list
//        // This restriction might be reconsidered
//        List<String> pubTypes = new ArrayList<String>();
//        for(String theType : ALLOWABLE_PUBLICATION_TYPES){
//            pubTypes.add(theType.replace(" ", "+"));
//        }
//
//        if(CollectionUtils.isNotEmpty(queryTerms)) {
//            if(typeQuery.equals(QUERY_TYPE_TITLE)) {
//                termBuilder.addJournalArticleTitleWords(queryTerms);
//            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMID)) {
//                termBuilder.addPubmedIds(queryTerms);
//                this.maxNumberSearchResults = queryTerms.size();
//            }else if(typeQuery.equals(EPubMedBibliographicIdentifiers.DOI)) {
//                termBuilder.add(queryTerms);
//            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMCID)) {
//                termBuilder.add(queryTerms);
//            } else {
//                termBuilder.add(queryTerms);
//            }
//        }
//
//        termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);
//
//        // The Integer is the total number of results
//        int offsetValidated = 0;
//        int maxResultsValidated = 0;
//        Map<Integer,PubmedArticleSet> results;
//        if(range>0 && offset>=0) {
//            offsetValidated = offset;
//            maxResultsValidated = range;
//            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), maxResultsValidated, offsetValidated);
//        } else {
//            // This is just returning the first group of results
//            maxResultsValidated = this.getMaxNumberSearchResults();
//            results = pubmedSearchAgent.fetchWithStats(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);
//        }
//
//        Map<Map<String,String>, List<PubmedArticleObject>> mapToReturn = new HashMap<Map<String,String>, List<PubmedArticleObject>>();
//
//        if (results == null){
//            Map<String,String> stats = new HashMap<String,String>();
//            stats.put("total", Integer.toString(0));
//            stats.put("exception", "PubmedArticleManagerImpl.searchPubmedArticles().nullresults");
//
//            mapToReturn.put(stats, new ArrayList<PubmedArticleObject>());
//            return mapToReturn;
//        }
//
//        int totalResults = results.keySet().iterator().next();
//        List<PubmedArticleObject> convertedResults = convertToExternalPubmedArticles(results.values().iterator().next());
//
//        Map<String,String> stats = new HashMap<String,String>();
//        stats.put("total", Integer.toString(totalResults));
//        stats.put("range", Integer.toString(maxResultsValidated));
//        stats.put("offset", Integer.toString(offsetValidated));
//
//        mapToReturn.put(stats, convertedResults);
//        return mapToReturn;
//    }

    /**
     * Converts the set of article results into a list of suitable objects.
     * @param records	The list of records to convert
     * @return The list of objects
     */
    private List<IBibliographicObject> convertToExternalPubmedArticles(PubmedArticleSet records) {
        List<IBibliographicObject> articles = new ArrayList<>();
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

//    /*
//     * (non-Javadoc)
//     * @see org.mindinformatics.services.connector.pubmed.dataaccess.IPubmedArticleManager#getPubmedArticles(java.lang.String, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
//     */
//    public List<PubmedArticleObject> getPubmedArticles(
//            String typeQuery, List<String>titleAndAbstractWords,
//            Integer pubStartMonth, Integer pubStartYear,
//            Integer pubEndMonth, Integer pubEndYear) {
//
//        PubMedQueryTermBuilder termBuilder = new PubMedQueryTermBuilder();
//        //We are restricting the  publication types to include these
//        List<String> pubTypes = new ArrayList<String>();
//        for(String theType : ALLOWABLE_PUBLICATION_TYPES){
//            pubTypes.add(theType.replace(" ", "+"));
//        }
//
//        // TODO To be considered
//        // termBuilder.addPublicationTypes(pubTypes);
//        if(CollectionUtils.isNotEmpty(titleAndAbstractWords)){
//            if(typeQuery.equals(QUERY_TYPE_TITLE))
//                termBuilder.addJournalArticleTitleWords(titleAndAbstractWords);
//            else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMID)) {
//                termBuilder.addPubmedIds(titleAndAbstractWords);
//                this.maxNumberSearchResults = titleAndAbstractWords.size();
//            }else if(typeQuery.equals(EPubMedBibliographicIdentifiers.DOI)) {
//                termBuilder.add(titleAndAbstractWords);
//            } else if(typeQuery.equals(EPubMedBibliographicIdentifiers.PMCID)) {
//                termBuilder.add(titleAndAbstractWords);
//            } else if(typeQuery.equals(QUERY_TYPE_PUBMED_CENTRAL_ID)) {
//                termBuilder.add(titleAndAbstractWords);
//            }
//        }
//        termBuilder.setPublicationDateRange(pubStartMonth, pubStartYear, pubEndMonth, pubEndYear);
//
//        PubmedArticleSet results = pubmedSearchAgent.fetch(termBuilder.toString(), this.getMaxNumberSearchResults(), 0);
//        if (results == null){
//            return null;
//        }
//        List<PubmedArticleObject> convertedResults = convertToExternalPubmedArticles(results);
//        return convertedResults;
//    }


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
