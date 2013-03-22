package org.siemac.metamac.srm.core.common.repository.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

public class SrmRepositoryUtils {

    private final static String BOOLEAN_TRUE_DATABASE_AS_1  = "1";
    private final static String BOOLEAN_FALSE_DATABASE_AS_0 = "0";

    public static String getString(Object source) {
        return source != null ? (String) source : null;
    }

    public static Long getLong(Object source) {
        if (source == null) {
            return null;
        } else if (source instanceof Long) {
            return (Long) source;
        } else {
            return Long.valueOf(source.toString());
        }
    }

    public static Integer getInteger(Object source) {
        if (source == null) {
            return null;
        } else if (source instanceof Integer) {
            return (Integer) source;
        } else {
            return Integer.valueOf(source.toString());
        }
    }

    public static Boolean getBoolean(Object source) throws MetamacException {
        if (source == null) {
            return null;
        } else if (source instanceof Boolean) {
            return (Boolean) source;
        } else if (BOOLEAN_TRUE_DATABASE_AS_1.equals(source.toString())) {
            return Boolean.TRUE;
        } else if (BOOLEAN_FALSE_DATABASE_AS_0.equals(source.toString())) {
            return Boolean.FALSE;
        } else {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNKNOWN).withMessageParameters("Boolean unrecognised: " + source).build();
        }
    }

    public static String booleanToBooleanDatabase(Boolean source) {
        return source ? BOOLEAN_TRUE_DATABASE_AS_1 : BOOLEAN_FALSE_DATABASE_AS_0;
    }

    /**
     * Returns true is internationalString exists and label not exists
     */
    public static Boolean withoutTranslation(Long internationalStringId, String label) {
        return internationalStringId != null && label == null;
    }

}