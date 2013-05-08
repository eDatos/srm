package org.siemac.metamac.srm.core.common.service.utils.shared;

import static org.siemac.metamac.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CODELIST_FAMILY_PREFIX;
import static org.siemac.metamac.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_ELEMENT_PREFIX;
import static org.siemac.metamac.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_FAMILY_PREFIX;
import static org.siemac.metamac.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CODELIST_VARIABLE_PREFIX;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmUrnParserUtils;

public class SrmUrnParserUtils extends SdmxSrmUrnParserUtils {

    // CODES

    public static boolean isCodelistFamilyUrn(String urn) {
        if (StringUtils.isBlank(urn)) {
            throw new IllegalArgumentException("URN can not be null");
        }
        return StringUtils.equals(URN_SIEMAC_CLASS_CODELIST_FAMILY_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static boolean isVariableFamilyUrn(String urn) {
        if (StringUtils.isBlank(urn)) {
            throw new IllegalArgumentException("URN can not be null");
        }
        return StringUtils.equals(URN_SIEMAC_CLASS_CODELIST_VARIABLE_FAMILY_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static boolean isVariableUrn(String urn) {
        if (StringUtils.isBlank(urn)) {
            throw new IllegalArgumentException("URN can not be null");
        }
        return StringUtils.equals(URN_SIEMAC_CLASS_CODELIST_VARIABLE_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static boolean isVariableElementUrn(String urn) {
        if (StringUtils.isBlank(urn)) {
            throw new IllegalArgumentException("URN can not be null");
        }
        return StringUtils.equals(URN_SIEMAC_CLASS_CODELIST_VARIABLE_ELEMENT_PREFIX, UrnUtils.extractPrefix(urn));
    }
}
