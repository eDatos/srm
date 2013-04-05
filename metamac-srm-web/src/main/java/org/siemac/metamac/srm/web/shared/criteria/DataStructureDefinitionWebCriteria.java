package org.siemac.metamac.srm.web.shared.criteria;

public class DataStructureDefinitionWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            statisticalOperationUrn;

    public DataStructureDefinitionWebCriteria() {
    }

    public DataStructureDefinitionWebCriteria(String criteria) {
        super(criteria);
    }

    public String getStatisticalOperationUrn() {
        return statisticalOperationUrn;
    }

    public void setStatisticalOperationUrn(String statisticalOperationUrn) {
        this.statisticalOperationUrn = statisticalOperationUrn;
    }
}
