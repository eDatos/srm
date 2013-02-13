package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        return CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
    }

    public static CodeMetamac mockCode(String resourceID, CodelistVersionMetamac codelist, CodeMetamac parent) {
        return CodesMetamacDoMocks.mockCodeFixedValues(resourceID, codelist, parent);
    }

    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {
        return CodesMetamacDoMocks.mockCodelistWithCodesFixedValues(agencyID, resourceID, version);
    }
}