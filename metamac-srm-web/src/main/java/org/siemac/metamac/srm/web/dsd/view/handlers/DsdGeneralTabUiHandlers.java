package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);

    // Life cycle

    void sendToProductionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ItemSchemeMetamacProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);

}
