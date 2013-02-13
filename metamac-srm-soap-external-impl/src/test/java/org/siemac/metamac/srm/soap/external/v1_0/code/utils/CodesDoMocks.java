package org.siemac.metamac.srm.soap.external.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        return CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
    }

    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {
        return CodesMetamacDoMocks.mockCodelistWithCodesFixedValues(agencyID, resourceID, version);
    }

    public static CodeMetamac mockCode(String resourceID, CodelistVersionMetamac codelist, CodeMetamac parent) {
        return CodesMetamacDoMocks.mockCodeFixedValues(resourceID, codelist, parent);
    }

    public static CodelistFamily mockCodelistFamily(String resourceID) {
        return CodesMetamacDoMocks.mockCodelistFamilyFixedValues(resourceID);
    }

    public static Variable mockVariable(String resourceID) {
        return CodesMetamacDoMocks.mockVariableFixedValues(resourceID);
    }

    public static VariableElement mockVariableElement(String resourceID) {
        return CodesMetamacDoMocks.mockVariableElementFixedValues(resourceID);
    }

    public static VariableFamily mockVariableFamily(String resourceID) {
        return CodesMetamacDoMocks.mockVariableFamilyFixedValues(resourceID);
    }

}