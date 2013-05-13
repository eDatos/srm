package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

public class CodeMetamacResultExtensionPoint {

    private final Map<String, String> shortName = new HashMap<String, String>();
    private String                    variableElementCode;

    public Map<String, String> getShortName() {
        return shortName;
    }

    public void setVariableElementCode(String variableElement) {
        this.variableElementCode = variableElement;
    }

    public String getVariableElementCode() {
        return variableElementCode;
    }
}
