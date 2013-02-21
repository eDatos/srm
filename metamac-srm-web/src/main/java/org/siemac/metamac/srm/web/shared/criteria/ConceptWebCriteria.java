package org.siemac.metamac.srm.web.shared.criteria;

public class ConceptWebCriteria extends ItemWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;

    public ConceptWebCriteria() {
    }

    public ConceptWebCriteria(String criteria) {
        super(criteria);
    }

    public String getDsdUrn() {
        return dsdUrn;
    }

    public void setDsdUrn(String dsdUrn) {
        this.dsdUrn = dsdUrn;
    }
}
