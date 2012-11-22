package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;

public class MockitoExpected extends MetamacRestAsserts {

    public static PagingParameter buildExpectedPagingParameter(String offset, String limit) {
        Integer startRow = null;
        if (offset == null) {
            startRow = Integer.valueOf(0);
        } else {
            startRow = Integer.valueOf(offset);
        }
        Integer maximumResultSize = null;
        if (limit == null) {
            maximumResultSize = Integer.valueOf(25);
        } else {
            maximumResultSize = Integer.valueOf(limit);
        }
        if (maximumResultSize > Integer.valueOf(1000)) {
            maximumResultSize = Integer.valueOf(1000);
        }
        int endRow = startRow + maximumResultSize;
        return PagingParameter.rowAccess(startRow, endRow, false);
    }
}