package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

public interface DsdGeneralTabUiHandlers extends CategorisationUiHandlers {

    void retrieveCompleteDsd(String urn);
    void retrieveDsdVersions(String dsdUrn);
    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
    void goToDsd(String urn);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria);

    // Life cycle

    void sendToProductionValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void sendToDiffusionValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void rejectValidation(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void publishInternally(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void publishExternally(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void versioning(String urn, VersionTypeEnum versionType);
    void cancelValidity(String urn);
}
