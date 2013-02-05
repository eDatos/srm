package org.siemac.metamac.srm.web.shared.criteria;

public class OrganisationSchemeWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private Boolean           isLastVersion;

    public OrganisationSchemeWebCriteria() {
    }

    public OrganisationSchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
