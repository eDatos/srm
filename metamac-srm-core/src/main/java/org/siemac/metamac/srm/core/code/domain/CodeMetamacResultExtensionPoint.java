package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

public class CodeMetamacResultExtensionPoint {

    private final Map<String, String> shortName = new HashMap<String, String>();

    public Map<String, String> getShortName() {
        return shortName;
    }
}
