package org.siemac.metamac.srm.web.shared.criteria;

public class VariableElementWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            variableUrn;
    private String            codelistUrn;

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

    public String getCodelistUrn() {
        return codelistUrn;
    }

    public void setCodelistUrn(String codelistUrn) {
        this.codelistUrn = codelistUrn;
    }
}
