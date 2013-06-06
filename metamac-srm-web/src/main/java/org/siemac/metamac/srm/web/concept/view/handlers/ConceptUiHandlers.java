package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptsByScheme(String conceptSchemeUrn);

    void deleteConcept(ConceptMetamacDto conceptMetamacDto);

    void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults);
    void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults);
    void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, String concept, String conceptSchemeUrn);
    void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, String concept, String conceptSchemeUrn);

    void retrieveConceptTypes();

    void retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(ConceptRoleEnum conceptRoleEnum, String variableUrn, int firstResult, int maxResults, String criteria, String conceptUrn);

    void retrieveVariables(int firstResult, int maxResults, String criteria, String variableFamilyUrn);
    void retrieveVariableFamilies(int firstResult, int maxResults, String criteria);
    
    //Relations
    void retrieveCodesForQuantityUnit(int firstResult, int maxResults, String codeCriteria, String schemeUrnAsFilter);
    void retrieveCodeListsForQuantityUnitFilter(int firstResult, int maxResults, String filterListCriteria);
    void retrieveConceptsForQuantityDenominator(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter);
    void retrieveConceptSchemesForQuantityDenominatorFilter(int firstResult, int maxResults, String filterListCriteria);
    void retrieveConceptsForQuantityNumerator(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter);
    void retrieveConceptSchemesForQuantityNumeratorFilter(int firstResult, int maxResults, String filterListCriteria);
    void retrieveConceptsForQuantityBase(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter);
    void retrieveConceptSchemesForQuantityBaseFilter(int firstResult, int maxResults, String filterListCriteria);
}
