package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;

public class VariableWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private VariableTypeEnum  variableType;

    public VariableWebCriteria() {
    }

    public VariableWebCriteria(String criteria) {
        super(criteria);
    }

    public VariableTypeEnum getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeEnum variableType) {
        this.variableType = variableType;
    }
}
