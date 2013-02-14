package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class DsdClientSecurityUtils {

    public static boolean canCreateDsd() {
        // return SharedDsdSecurityUtils.canCreateDataStructureDefinition(MetamacSrmWeb.getCurrentUser());
        return true;
    }

    public static boolean canUpdateDsd(ProcStatusEnum procStatus) {
        // return SharedDsdSecurityUtils.canUpdateDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
        return true;
    }

    public static boolean canVersioningDsd() {
        // return SharedDsdSecurityUtils.canVersioningDsd(MetamacSrmWeb.getCurrentUser());
        return true;
    }

    public static boolean canUpdatePrimaryMeasure(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canUpdatePrimaryMeasure(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateDimensions(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateAttributes(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateGroupKeys(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteDsd(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canDeleteDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canImportDsd() {
        return true;
        // return SharedDsdSecurityUtils.canImportDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelDsdValidity() {
        return true;
        // return SharedDsdSecurityUtils.canEndDsdValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceDsd() {
        return true;
        // return SharedDsdSecurityUtils.canAnnounceDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendDsdToProductionValidation() {
        return true;
        // return SharedDsdSecurityUtils.canSendDsdToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendDsdToDiffusionValidation() {
        return true;
        // return SharedDsdSecurityUtils.canSendDsdToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectDsdValidation(ProcStatusEnum procStatus) {
        return true;
        // return SharedDsdSecurityUtils.canRejectDsdValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishDsdInternally() {
        return true;
        // return SharedDsdSecurityUtils.canPublishDsdInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishDsdExternally() {
        return true;
        // return SharedDsdSecurityUtils.canPublishDsdExternally(MetamacSrmWeb.getCurrentUser());
    }
}
