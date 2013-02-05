package org.siemac.metamac.srm.web.shared.criteria;

public class DsdWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private Boolean           isLastVersion;

    public DsdWebCriteria() {
    }

    public DsdWebCriteria(String criteria) {
        super(criteria);
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
