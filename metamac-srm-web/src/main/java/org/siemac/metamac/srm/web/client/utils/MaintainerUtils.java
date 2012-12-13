package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.web.common.client.enums.ApplicationOrganisationEnum;
import org.siemac.metamac.web.common.client.utils.ApplicationOrganisation;

public class MaintainerUtils {

    private static String DREM_URN  = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).DREM";
    private static String ISTAC_URN = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).ISTAC";
    private static String SREA_URN  = "urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).SREA";

    public static String getCurrentMaintainer() {
        if (ApplicationOrganisationEnum.DREM.equals(ApplicationOrganisation.getCurrentOrganisation())) {
            return DREM_URN;
        } else if (ApplicationOrganisationEnum.ISTAC.equals(ApplicationOrganisation.getCurrentOrganisation())) {
            return ISTAC_URN;
        } else if (ApplicationOrganisationEnum.SREA.equals(ApplicationOrganisation.getCurrentOrganisation())) {
            return SREA_URN;
        }
        return null;
    }
}
