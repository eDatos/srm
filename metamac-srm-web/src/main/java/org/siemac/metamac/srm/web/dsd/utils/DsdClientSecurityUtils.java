package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class DsdClientSecurityUtils {

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

    public static boolean canUpdatePrimaryMeasure(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdatePrimaryMeasure(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canUpdateDimensions(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canUpdateAttributes(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
    }

    public static boolean canUpdateGroupKeys(ProcStatusEnum procStatus, String operationCode) {
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), procStatus, operationCode);
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
}
