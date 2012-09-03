package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

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

    void sendToProductionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);
}
