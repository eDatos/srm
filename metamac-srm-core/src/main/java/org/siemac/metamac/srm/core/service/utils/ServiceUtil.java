package org.siemac.metamac.srm.core.service.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.common.constants.SrmConstants;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;

public class ServiceUtil {

    private static final NumberFormat formatterMajor                  = new DecimalFormat("0");
    private static final NumberFormat formatterMinor                  = new DecimalFormat("000");

    public static String generateVersionNumber(String actualVersionNumber, VersionTypeEnum versionType) throws MetamacException {

        if (actualVersionNumber == null) {
            return SrmConstants.VERSION_NUMBER_INITIAL;
        }

        String[] versionNumberSplited = actualVersionNumber.split("\\.");
        Integer versionNumberMajor = Integer.valueOf(versionNumberSplited[0]);
        Integer versionNumberMinor = Integer.valueOf(versionNumberSplited[1]);

        if (VersionTypeEnum.MAJOR.equals(versionType)) {
            versionNumberMajor++;
            versionNumberMinor = 0;
        } else if (VersionTypeEnum.MINOR.equals(versionType)) {
            versionNumberMinor++;
        } else {
            throw new MetamacException(MetamacCoreExceptionType.UNKNOWN, "Unsupported value for " + VersionTypeEnum.class.getCanonicalName() + ": " + versionType);
        }
        return (new StringBuilder()).append(formatterMajor.format(versionNumberMajor)).append(".").append(formatterMinor.format(versionNumberMinor)).toString();
    }
}