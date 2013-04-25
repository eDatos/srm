package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class CodeVariableElementNormalisationResult implements Serializable {

    private static final long              serialVersionUID = 1L;

    private CodeMetamacVisualisationResult code;
    private VariableElementResult          variableElementProposed;

    public CodeMetamacVisualisationResult getCode() {
        return code;
    }

    public void setCode(CodeMetamacVisualisationResult code) {
        this.code = code;
    }

    public VariableElementResult getVariableElementProposed() {
        return variableElementProposed;
    }

    public void setVariableElementProposed(VariableElementResult variableElementProposed) {
        this.variableElementProposed = variableElementProposed;
    }

}