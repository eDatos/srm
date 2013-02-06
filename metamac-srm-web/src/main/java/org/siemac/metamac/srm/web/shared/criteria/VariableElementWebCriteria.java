package org.siemac.metamac.srm.web.shared.criteria;

public class VariableElementWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            variableUrn;

    public VariableElementWebCriteria() {
    }

    public VariableElementWebCriteria(String criteria) {
        super(criteria);
    }

    public String getVariableUrn() {
        return variableUrn;
    }

    public void setVariableUrn(String variableUrn) {
        this.variableUrn = variableUrn;
    }
}
