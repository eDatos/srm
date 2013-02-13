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

        CodelistVersionMetamac codelistVersion = mockCodelist(agencyID, resourceID, version);

        // codes
        CodeMetamac code1 = mockCode("code1", codelistVersion, null);
        CodeMetamac code2 = mockCode("code2", codelistVersion, null);
        CodeMetamac code2A = mockCode("code2A", codelistVersion, code2);
        CodeMetamac code2B = mockCode("code2B", codelistVersion, code2);

        // codes hierarchy
        codelistVersion.addItem(code1);
        codelistVersion.addItemsFirstLevel(code1);
        codelistVersion.addItem(code2);
        codelistVersion.addItemsFirstLevel(code2);
        codelistVersion.addItem(code2A);
        codelistVersion.addItem(code2B);
        code2.addChildren(code2A);
        code2.addChildren(code2B);

        return codelistVersion;
    }
}