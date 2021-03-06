package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void retrieveConceptsByScheme(String conceptSchemeUrn);

    void deleteConcept(ConceptMetamacDto conceptMetamacDto);

    void retrieveConceptSchemesWithConceptsThatCanBeRole(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsThatCanBeRole(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemesWithConceptsThatCanBeExtended(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsThatCanBeExtended(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptTypes();

    void retrieveCodelistsOrConceptSchemesForEnumeratedRepresentation(RelatedResourceTypeEnum relatedResourceTypeEnum, String variableUrn, int firstResult, int maxResults, String criteria,
            String conceptUrn, boolean isLastVersion);

    void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria, String variableFamilyUrn);
    void retrieveVariableFamilies(int firstResult, int maxResults, String criteria);

    // RELATED RESOURCE RELATIONS

    void retrieveCodelistsForQuantityUnitFilter(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria);
    void retrieveCodesForQuantityUnit(int firstResult, int maxResults, CodeWebCriteria codeWebCriteria);

    void retrieveCodelistsForQuantityBaseLocationFilter(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria);
    void retrieveCodesForQuantityBaseLocation(int firstResult, int maxResults, CodeWebCriteria codeWebCriteria);

    void retrieveConceptSchemesForQuantityDenominatorFilter(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsForQuantityDenominator(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemesForQuantityNumeratorFilter(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsForQuantityNumerator(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemesForQuantityBaseFilter(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsForQuantityBase(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);
}
