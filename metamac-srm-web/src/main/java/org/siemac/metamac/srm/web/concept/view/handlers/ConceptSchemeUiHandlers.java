package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

public interface ConceptSchemeUiHandlers extends BaseConceptUiHandlers {

    // Schemes

    void retrieveConceptScheme(String conceptSchemeUrn);
    void retrieveConceptListByScheme(String conceptSchemeUrn);
    void retrieveConceptSchemeVersions(String conceptSchemeUrn);
    void saveConceptScheme(ConceptSchemeMetamacDto conceptScheme);
    void goToConceptScheme(String urn);
    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);
    void cancelValidity(String urn);

    // Life cycle

    void sendToProductionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void sendToDiffusionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void rejectValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void publishInternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void publishExternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void versioning(String urn, VersionTypeEnum versionType);
}
