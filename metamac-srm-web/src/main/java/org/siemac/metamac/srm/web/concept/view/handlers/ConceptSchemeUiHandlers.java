package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeUiHandlers extends UiHandlers {

    // Schemes

    void retrieveConceptScheme(String conceptSchemeUrn);
    void retrieveConceptListByScheme(String conceptSchemeUrn);
    void retrieveConceptSchemeHistoryList(String conceptSchemeUrn);
    void saveConceptScheme(ConceptSchemeMetamacDto conceptScheme);
    void goToConceptScheme(String urn);
    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);
    void cancelValidity(String urn);

    // Concepts

    void createConcept(ConceptMetamacDto conceptDto);
    void deleteConcepts(List<Long> conceptIds);
    void goToConcept(String urn);

    // Life cycle

    void sendToProductionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);
}
