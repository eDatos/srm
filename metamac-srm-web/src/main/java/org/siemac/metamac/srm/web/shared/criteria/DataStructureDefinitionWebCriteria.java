package org.siemac.metamac.srm.web.shared.criteria;

public class DataStructureDefinitionWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            statisticalOperationUrn;
    private String            dimensionConceptUrn;
    private String            attributeConceptUrn;

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

    public String getDimensionConceptUrn() {
        return dimensionConceptUrn;
    }

    public String getAttributeConceptUrn() {
        return attributeConceptUrn;
    }

    public void setDimensionConceptUrn(String dimensionConceptUrn) {
        this.dimensionConceptUrn = dimensionConceptUrn;
    }

    public void setAttributeConceptUrn(String attributeConceptUrn) {
        this.attributeConceptUrn = attributeConceptUrn;
    }
}
