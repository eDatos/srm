package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class DsdClientSecurityUtils {

    // DSD

    public static boolean canCreateDsd() {
        // The operation is null because we only want to know if the create button can be shown (and we do not know the operation yet!)
        return SharedDsdSecurityUtils.canCreateDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), null);
    }

    public static boolean canUpdateDsd(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canVersioningDsd(String operationCode) {
        return SharedDsdSecurityUtils.canVersioningDsd(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canDeleteDsd(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canDeleteDsd(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canCancelDsdValidity(String operationCode) {
        return SharedDsdSecurityUtils.canEndDsdValidity(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canAnnounceDsd(String operationCode) {
        return SharedDsdSecurityUtils.canAnnounceDsd(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canSendDsdToProductionValidation(String operationCode) {
        return SharedDsdSecurityUtils.canSendDsdToProductionValidation(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canSendDsdToDiffusionValidation(String operationCode) {
        return SharedDsdSecurityUtils.canSendDsdToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canRejectDsdValidation(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canRejectDsdValidation(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canPublishDsdInternally(String operationCode) {
        return SharedDsdSecurityUtils.canPublishDsdInternally(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    public static boolean canPublishDsdExternally(String operationCode) {
        return SharedDsdSecurityUtils.canPublishDsdExternally(MetamacSrmWeb.getCurrentUser(), operationCode);
    }

    // PRIMARY MEASURE

    public static boolean canUpdatePrimaryMeasure(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdatePrimaryMeasure(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    // DIMENSIONS

    public static boolean canCreateDimension(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    public static boolean canUpdateDimension(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canDeleteDimension(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ATTRIBUTES

    public static boolean canUpdateAttributes(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    // GROUP KEYS

    public static boolean canUpdateGroupKeys(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    // CATEGORISATIONS

    public static boolean canModifyCategorisationForDataStructureDefinition(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }
}
