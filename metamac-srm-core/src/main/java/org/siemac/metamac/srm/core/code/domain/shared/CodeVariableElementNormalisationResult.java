package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class CodeVariableElementNormalisationResult implements Serializable {

    private static final long              serialVersionUID = 1L;

    private CodeMetamacVisualisationResult code;
    private VariableElementVisualisationResult          variableElementProposed;

    public CodeMetamacVisualisationResult getCode() {
        return code;
    }

    public void setCode(CodeMetamacVisualisationResult code) {
        this.code = code;
    }

    public VariableElementVisualisationResult getVariableElementProposed() {
        return variableElementProposed;
    }

    public void setVariableElementProposed(VariableElementVisualisationResult variableElementProposed) {
        this.variableElementProposed = variableElementProposed;
    }

}