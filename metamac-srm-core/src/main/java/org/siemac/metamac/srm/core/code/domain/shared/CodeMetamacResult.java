package org.siemac.metamac.srm.core.code.domain.shared;

import java.util.HashMap;
import java.util.Map;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CodeMetamacResult extends ItemResult {

    private final Map<String, String> shortName = new HashMap<String, String>();

    public Map<String, String> getShortName() {
        return shortName;
    }
}
