package org.siemac.metamac.srm.core.common.service.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;

public class SrmValidationUtils {

    public static boolean isInternationalStringEmpty(InternationalString internationalString) {
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

    public static void checkExternallyPublished(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(lifeCycle.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(ProcStatusEnum.EXTERNALLY_PUBLISHED);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
    }
}