package org.siemac.metamac.srm.web.concept.view.handlers;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptExtended(String conceptUrn);
    void retrieveConceptsByScheme(String conceptSchemeUrn);
    void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String concept);
    void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String concept);

    void retrieveConceptTypes();

    void retrieveCodeLists(String conceptUrn);
}
