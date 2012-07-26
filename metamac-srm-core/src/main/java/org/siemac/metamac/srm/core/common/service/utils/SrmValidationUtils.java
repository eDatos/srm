package org.siemac.metamac.srm.core.common.service.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;

public class SrmValidationUtils {

    public static boolean isInternationalStringEmpty(InternationalString internationalString) throws MetamacException {
        if (internationalString == null) {
            return true;
        }
        if (internationalString.getTexts().size() == 0) {
            return true;
        }
        for (LocalisedString localisedString : internationalString.getTexts()) {
            if (StringUtils.isEmpty(localisedString.getLabel()) || StringUtils.isEmpty(localisedString.getLocale())) {
                return true;
            }
        }
        return false;
    }

}
