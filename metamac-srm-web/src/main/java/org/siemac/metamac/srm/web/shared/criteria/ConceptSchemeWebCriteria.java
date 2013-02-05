package org.siemac.metamac.srm.web.shared.criteria;

public class ConceptSchemeWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;
    private Boolean           isLastVersion;

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

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
