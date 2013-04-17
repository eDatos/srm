package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
    void goToDsd(String urn);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria);

    // Life cycle

    void sendToProductionValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void sendToDiffusionValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void rejectValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void publishInternally(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, Boolean forceLatestFinal);
    void publishExternally(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void versioning(String urn, VersionTypeEnum versionType);
    void cancelValidity(String urn);
    void createTemporalVersion(String urn);
}
