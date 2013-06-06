package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

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

    public static boolean canCancelDsdValidity(DataStructureDefinitionMetamacBasicDto dataStructureDefinitionMetamacBasicDto) {
        return canCancelDsdValidity(dataStructureDefinitionMetamacBasicDto.getUrn(), CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacBasicDto),
                dataStructureDefinitionMetamacBasicDto.getMaintainer(), dataStructureDefinitionMetamacBasicDto.getVersionLogic());
    }

    public static boolean canCancelDsdValidity(String urn, String operationCode, RelatedResourceDto maintainer, String versionLogic) {
        return SharedDsdSecurityUtils.canEndDsdValidity(MetamacSrmWeb.getCurrentUser(), operationCode)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(urn, maintainer, versionLogic);
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

    public static boolean canCopyDsd(RelatedResourceDto maintainer) {
        // Only resources from other organisations can be copied
        return SharedDsdSecurityUtils.canCopyDataStructureDefinition(MetamacSrmWeb.getCurrentUser()) && !org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer);
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

        if (BooleanUtils.isTrue(categorisationDto.getFinalLogic())) {

            // if it is final, can NEVER be deleted
            return false;

        } else {

            if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {

                return SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);

            } else {

                // if it does not have the default maintainer, can NEVER be deleted
                return false;
            }
        }
    }

    public static boolean canCancelCategorisationValidityForDataStructureDefinition(ProcStatusEnum procStatus, String operationCode, CategorisationDto categorisationDto) {

        if (categorisationDto.getValidTo() != null) { // The validity has been canceled previously
            return false;
        }

        // Only categorisations of default maintainer can be canceled

        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {
            return SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
        } else {
            return false;
        }
    }
}
