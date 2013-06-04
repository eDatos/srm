package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

public class ConceptSchemeWebCriteria extends VersionableResourceWebCriteria {

    private static final long     serialVersionUID = 1L;

    private String                dsdUrn;
    private String                conceptUrn;
    private String                statisticalOperationUrn;
    private ConceptSchemeTypeEnum conceptSchemeTypeEnum;

    public ConceptSchemeWebCriteria() {
    }

    public ConceptSchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public ConceptSchemeWebCriteria(String criteria, String dsdUrn) {
        super(criteria);
        this.dsdUrn = dsdUrn;
    }

    public String getDsdUrn() {
        return dsdUrn;
    }

    public void setDsdUrn(String dsdUrn) {
        this.dsdUrn = dsdUrn;
    }

    public String getStatisticalOperationUrn() {
        return statisticalOperationUrn;
    }

    public ConceptSchemeTypeEnum getConceptSchemeTypeEnum() {
        return conceptSchemeTypeEnum;
    }

    public void setStatisticalOperationUrn(String statisticalOperationUrn) {
        this.statisticalOperationUrn = statisticalOperationUrn;
    }

    public void setConceptSchemeTypeEnum(ConceptSchemeTypeEnum conceptSchemeTypeEnum) {
        this.conceptSchemeTypeEnum = conceptSchemeTypeEnum;
    }

    public String getConceptUrn() {
        return conceptUrn;
    }

    public void setConceptUrn(String conceptUrn) {
        this.conceptUrn = conceptUrn;
    }
}
