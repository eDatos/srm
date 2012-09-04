package org.siemac.metamac.srm.web.concept.view.handlers;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptListByScheme(String conceptSchemeUrn);

    void retrieveConceptTypes();

}
