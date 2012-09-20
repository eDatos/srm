package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class DsdClientSecurityUtils {

    public static boolean canCreateDsd() {
        return SharedDsdSecurityUtils.canCreateDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateDsd(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canUpdateDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canVersioningDsd() {
        return SharedDsdSecurityUtils.canVersioningDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdatePrimaryMeasure(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canUpdatePrimaryMeasure(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateDimensions(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateAttributes(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateGroupKeys(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteDsd(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canDeleteDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canImportDsd() {
        return SharedDsdSecurityUtils.canImportDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelDsdValidity() {
        return SharedDsdSecurityUtils.canCancelDsdValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceDsd() {
        return SharedDsdSecurityUtils.canAnnounceDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendDsdToProductionValidation() {
        return SharedDsdSecurityUtils.canSendDsdToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendDsdToDiffusionValidation() {
        return SharedDsdSecurityUtils.canSendDsdToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectDsdValidation(ItemSchemeMetamacProcStatusEnum procStatus) {
        return SharedDsdSecurityUtils.canRejectDsdValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishDsdInternally() {
        return SharedDsdSecurityUtils.canPublishDsdInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishDsdExternally() {
        return SharedDsdSecurityUtils.canPublishDsdExternally(MetamacSrmWeb.getCurrentUser());
    }

}
