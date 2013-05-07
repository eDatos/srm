package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

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

    public static boolean canCreateDsdTemporalVersion(String operationCode) {
        return SharedDsdSecurityUtils.canCreateDsdTemporalVersion(MetamacSrmWeb.getCurrentUser(), operationCode);
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

    public static boolean canCopyDsd() {
        return SharedDsdSecurityUtils.canCopyDataStructureDefinition(MetamacSrmWeb.getCurrentUser());
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
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    public static boolean canUpdateDimension(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode);
    }

    public static boolean canDeleteDimension(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ATTRIBUTES

    public static boolean canCreateAttribute(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    public static boolean canUpdateAttribute(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode);
    }

    public static boolean canDeleteAttribute(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // GROUP KEYS

    public static boolean canCreateGroupKeys(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    public static boolean canUpdateGroupKeys(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode);
    }

    public static boolean canDeleteGroupKeys(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CATEGORISATIONS

    public static boolean canCreateCategorisationForDataStructureDefinition(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canDeleteCategorisationForDataStructureDefinition(ProcStatusEnum procStatus, String operationCode, CategorisationDto categorisationDto) {
        // Maintainer is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer)
        return SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(categorisationDto);
    }
}
