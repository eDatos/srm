package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        return CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
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
        return CodesMetamacDoMocks.mockCodelistWithCodesFixedValues(agencyID, resourceID, version);
    }
}