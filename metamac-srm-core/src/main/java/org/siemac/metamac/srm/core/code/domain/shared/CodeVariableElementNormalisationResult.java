package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class CodeVariableElementNormalisationResult implements Serializable {

    private static final long              serialVersionUID = 1L;

    private CodeMetamacVisualisationResult code;
    private VariableElementResult          variableElement;

    public CodeMetamacVisualisationResult getCode() {
        return code;
    }

    public void setCode(CodeMetamacVisualisationResult code) {
        this.code = code;
    }

    public VariableElementResult getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(VariableElementResult variableElement) {
        this.variableElement = variableElement;
    }

}