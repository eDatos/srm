package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface DsdGeneralTabUiHandlers extends BaseUiHandlers {

    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
    void deleteDsd(String urn);
    void goToDsd(String urn);
    void retrieveLatestDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto);
    void exportDsd(String urn, ExportDetailEnum detail, ExportReferencesEnum references);
    void copyDsd(String urn);
    void copyDsd(String urn, String code);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria);

    void retrieveDimensionsAndCandidateVisualisations(String dsdUrn);

    // Stream messages

    void reSendStreamMessageDsd(String urn);

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
