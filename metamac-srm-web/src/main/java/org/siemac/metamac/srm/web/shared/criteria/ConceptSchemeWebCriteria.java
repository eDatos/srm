package org.siemac.metamac.srm.web.shared.criteria;

public class ConceptSchemeWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;

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
}
