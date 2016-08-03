package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;

public class VariableElementWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            variableUrn;
    private String            codelistUrn;
    private VariableTypeEnum  variableType;
    private Boolean           isHasShape;
    private String            granularityCodeUrn;
    private Date              validFromDate;
    private Date              validToDate;

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

    public Date getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    public VariableTypeEnum getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeEnum variableType) {
        this.variableType = variableType;
    }

    public Boolean getIsHasShape() {
        return isHasShape;
    }

    public void setIsHasShape(Boolean isHasShape) {
        this.isHasShape = isHasShape;
    }

    public String getGranularityCodeUrn() {
        return granularityCodeUrn;
    }

    public void setGranularityCodeUrn(String granularityCodeUrn) {
        this.granularityCodeUrn = granularityCodeUrn;
    }
}
