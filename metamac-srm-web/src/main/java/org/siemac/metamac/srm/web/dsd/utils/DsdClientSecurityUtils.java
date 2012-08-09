package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class DsdClientSecurityUtils {

    // TODO Remove this attribute!!!
    private static ItemSchemeMetamacProcStatusEnum procStatus = ItemSchemeMetamacProcStatusEnum.DRAFT;

    public static boolean canCreateDsd() {
        return SharedDsdSecurityUtils.canCreateDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateDsd() {
        return SharedDsdSecurityUtils.canUpdateDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canVersioningDsd() {
        return SharedDsdSecurityUtils.canVersioningDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdatePrimaryMeasure() {
        return SharedDsdSecurityUtils.canUpdatePrimaryMeasure(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateDimensions() {
        return SharedDsdSecurityUtils.canUpdateDimensions(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateAttributes() {
        return SharedDsdSecurityUtils.canUpdateAttributes(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateGroupKeys() {
        return SharedDsdSecurityUtils.canUpdateGroupKeys(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteDsd() {
        return SharedDsdSecurityUtils.canDeleteDsd(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canImportDsd() {
        return SharedDsdSecurityUtils.canImportDsd(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateAnnotations() {
        return SharedDsdSecurityUtils.canUpdateAnnotations(MetamacSrmWeb.getCurrentUser(), procStatus);
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

    public static boolean canRejectDsdValidation() {
        return SharedDsdSecurityUtils.canRejectDsdValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishDsdInternally() {
        return SharedDsdSecurityUtils.canPublishDsdInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishDsdExternally() {
        return SharedDsdSecurityUtils.canPublishDsdExternally(MetamacSrmWeb.getCurrentUser());
    }

}
