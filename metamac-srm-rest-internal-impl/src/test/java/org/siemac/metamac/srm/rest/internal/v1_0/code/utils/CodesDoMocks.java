package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
        target.setItemScheme(new ItemScheme());
        return target;
    }

    public static CodeMetamac mockCode(String resourceID, CodelistVersionMetamac codelist, CodeMetamac parent) {
        return CodesMetamacDoMocks.mockCodeFixedValues(resourceID, codelist, parent);
    }

    public static ItemResult mockCodeResult(String resourceID, ItemResult parent) {
        ItemResult itemResult = CodesMetamacDoMocks.mockCodeResultFixedValues(resourceID, parent);
        itemResult.setUriProvider(null);
        return itemResult;
    }

    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = mockCodelist(agencyID, resourceID, version);

        target.setReplacedByCodelist(mockCodelist(agencyID, "codelistReplacedBy", "01.000"));

        CodelistVersionMetamac codelistReplaceTo1 = mockCodelist(agencyID, "codelistReplaceTo1", "01.000");
        codelistReplaceTo1.getMaintainableArtefact().setFinalLogicClient(true);
        target.addReplaceToCodelist(codelistReplaceTo1);
        CodelistVersionMetamac codelistReplaceTo2 = mockCodelist(agencyID, "codelistReplaceTo2", "01.000");
        codelistReplaceTo2.getMaintainableArtefact().setFinalLogicClient(false);
        target.addReplaceToCodelist(codelistReplaceTo2);
        CodelistVersionMetamac codelistReplaceTo3 = mockCodelist(agencyID, "codelistReplaceTo3", "02.000");
        codelistReplaceTo3.getMaintainableArtefact().setFinalLogicClient(true);
        target.addReplaceToCodelist(codelistReplaceTo3);
        return target;
    }
}