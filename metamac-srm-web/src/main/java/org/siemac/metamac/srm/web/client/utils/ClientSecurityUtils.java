package org.siemac.metamac.srm.web.client.utils;

public class ClientSecurityUtils {

    public static boolean canCreateConceptScheme() {
        return true;
    }

    public static boolean canEditConceptScheme() {
        return true;
    }

    public static boolean canSendConceptSchemeToPendingPublication() {
        return true;
    }

    public static boolean canRejectConceptSchemeValidation() {
        return true;
    }

    public static boolean canPublishInternallyConceptScheme() {
        return true;
    }

    public static boolean canPublishExternallyConceptScheme() {
        return true;
    }

    public static boolean canVersioningConceptScheme() {
        return true;
    }
}
