package org.siemac.metamac.srm.web.client.utils;

import java.util.List;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;

public class ErrorUtils extends CommonErrorUtils {

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        return getErrorMessages(MetamacSrmWeb.getCoreMessages(), caught, alternativeMessage);
    }
}