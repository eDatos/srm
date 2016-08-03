package org.siemac.metamac.srm.web.shared.criteria;

public class CategoryWebCriteria extends ItemWebCriteria {

    private static final long serialVersionUID = 1L;

    private Boolean           isExternallyPublished;

    public CategoryWebCriteria() {
    }

    public CategoryWebCriteria(String criteria) {
        super(criteria);
    }

    public Boolean getIsExternallyPublished() {
        return isExternallyPublished;
    }

    public void setIsExternallyPublished(Boolean isExternallyPublished) {
        this.isExternallyPublished = isExternallyPublished;
    }
}
