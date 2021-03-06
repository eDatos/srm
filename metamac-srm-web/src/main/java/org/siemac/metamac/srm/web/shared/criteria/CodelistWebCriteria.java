package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;

public class CodelistWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            conceptUrn;
    private String            codelistFamilyUrn;
    private AccessTypeEnum    accessType;
    private String            isNotCodelistUrn;
    private String            variableUrn;
    private String            variableElementUrn;
    private String            variableFamilyUrn;
    private String            codelisUrnToReplaceCodelist;

    public CodelistWebCriteria() {
    }

    public CodelistWebCriteria(String criteria) {
        super(criteria);
    }

    public CodelistWebCriteria(String criteria, String conceptUrn) {
        super(criteria);
        this.conceptUrn = conceptUrn;
    }

    public String getConceptUrn() {
        return conceptUrn;
    }

    public void setConceptUrn(String conceptUrn) {
        this.conceptUrn = conceptUrn;
    }

    public String getCodelistFamilyUrn() {
        return codelistFamilyUrn;
    }

    public void setCodelistFamilyUrn(String codelistFamilyUrn) {
        this.codelistFamilyUrn = codelistFamilyUrn;
    }

    public AccessTypeEnum getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessTypeEnum accessType) {
        this.accessType = accessType;
    }

    public String getIsNotCodelistUrn() {
        return isNotCodelistUrn;
    }

    public void setIsNotCodelistUrn(String isNotCodelistUrn) {
        this.isNotCodelistUrn = isNotCodelistUrn;
    }

    public String getVariableUrn() {
        return variableUrn;
    }

    public void setVariableUrn(String variableUrn) {
        this.variableUrn = variableUrn;
    }

    public String getVariableElementUrn() {
        return variableElementUrn;
    }

    public void setVariableElementUrn(String variableElementUrn) {
        this.variableElementUrn = variableElementUrn;
    }

    public String getVariableFamilyUrn() {
        return variableFamilyUrn;
    }

    public void setVariableFamilyUrn(String variableFamilyUrn) {
        this.variableFamilyUrn = variableFamilyUrn;
    }

    public String getCodelisUrnToReplaceCodelist() {
        return codelisUrnToReplaceCodelist;
    }

    public void setCodelisUrnToReplaceCodelist(String codelisUrnToReplaceCodelist) {
        this.codelisUrnToReplaceCodelist = codelisUrnToReplaceCodelist;
    }
}
