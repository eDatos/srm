package org.siemac.metamac.srm.web.concept.view.handlers;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptExtended(String conceptUrn);
    void retrieveConceptsByScheme(String conceptSchemeUrn);

    void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults);
    void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults);
    void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String concept, String conceptSchemeUrn);
    void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String concept, String conceptSchemeUrn);

    void retrieveConceptTypes();

    void retrieveCodeLists(String conceptUrn);
}
