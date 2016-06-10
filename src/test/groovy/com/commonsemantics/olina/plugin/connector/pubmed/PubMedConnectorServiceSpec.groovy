package com.commonsemantics.olina.plugin.connector.pubmed

import com.commonsemantics.olina.plugin.connector.bibliographic.BibliographicIdentifier
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Integration
@TestFor(PubMedConnectorService)
@Mock([BibliographicIdentifier])
class PubMedConnectorServiceSpec extends Specification {

    @Autowired
    PubMedConnectorService pubMedConnectorService

    def setup() {
    }

    def cleanup() {
    }

    void "test something 1 object pmid"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectById("PMID", "21624159");
        expect: true==true
    }

    void "test something 2 json pmid"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectByIdAsJson("PMID", "21624159");
        expect: true==true
    }

    void "test something 1 object pmcid"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectById("PMCID", "PMC3102893").getResults().toString();
        expect: true==true
    }

    void "test something 1 json pmcid"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectByIdAsJson("PMCID", "PMC3102893");
        expect: true==true
    }

    void "test something 1 object doi"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectById("DOI", "10.1186/2041-1480-2-S2-S4").getResults().toString();
        expect: true==true
    }

    void "test something 1 json doi"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getBibliographicObjectByIdAsJson("DOI", "10.1186/2041-1480-2-S2-S4");
        expect: true==true
    }

    void "test something 3 pmid"() {
        pubMedConnectorService.initialize();
        List<String> ids = new ArrayList<>();
        ids.add("21624159");
        ids.add("21624150");
        println pubMedConnectorService.getBibliographicObjects("PMID", ids);
        expect: true==true
    }

    void "test something 4 json pmid"() {
        pubMedConnectorService.initialize();
        List<String> ids = new ArrayList<>();
        ids.add("21624159");
        ids.add("21624150");
        println pubMedConnectorService.getBibliographicObjectsAsJson("PMID", ids);
        expect: true==true
    }

    void "test something 4 json pmc"() {
        pubMedConnectorService.initialize();
        List<String> ids = new ArrayList<>();
        ids.add("PMC3102893");
        ids.add("PMC3102903");
        println pubMedConnectorService.getBibliographicObjectsAsJson("PMCID", ids);
        expect: true==true
    }

    void "test something 4 json doi"() {
        pubMedConnectorService.initialize();
        List<String> ids = new ArrayList<>();
        ids.add("10.1186/2041-1480-2-S2-S4");
        ids.add("10.1186/1617-9625-9-S1-S4");
        println pubMedConnectorService.getBibliographicObjectsAsJson("DOI", ids);
        expect: true==true
    }

    void "test title json"() {
        pubMedConnectorService.initialize();
        List<String> ids = new ArrayList<>();
        ids.add("An open annotation ontology for science on web 3.0");
        println pubMedConnectorService.getBibliographicObjectsAsJson("title", ids);
        expect: true==true
    }
}


