package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptsByScheme(String conceptSchemeUrn);

    void deleteConcept(ConceptMetamacDto conceptMetamacDto);

    void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults);
    void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults);
    void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String concept, String conceptSchemeUrn);
    void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String concept, String conceptSchemeUrn);

    void retrieveConceptTypes();

    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn);

    void retrieveVariables(int firstResult, int maxResults, String criteria, String variableFamilyUrn);
    void retrieveVariableFamilies(int firstResult, int maxResults, String criteria);
}
