package com.commonsemantics.olina.plugin.connector.pubmed

import com.commonsemantics.olina.connector.bibliographic.BibliographicIdentifier
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

    void "test something"() {
        pubMedConnectorService.initialize();
        println pubMedConnectorService.getArticleById("PMID", "21624159");
        expect: true==true
    }
}
