package org.siemac.metamac.srm.web.shared.criteria;

public class CategoryWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            categorySchemeUrn;
    private Boolean           isExternallyPublished;

    public CategoryWebCriteria() {
    }

    public CategoryWebCriteria(String criteria) {
        super(criteria);
    }

    public CategoryWebCriteria(String criteria, String categorySchemeUrn) {
        super(criteria);
        this.categorySchemeUrn = categorySchemeUrn;
    }

    public String getCategorySchemeUrn() {
        return categorySchemeUrn;
    }

    public void setCategorySchemeUrn(String categorySchemeUrn) {
        this.categorySchemeUrn = categorySchemeUrn;
    }

    public Boolean getIsExternallyPublished() {
        return isExternallyPublished;
    }

    public void setIsExternallyPublished(Boolean isExternallyPublished) {
        this.isExternallyPublished = isExternallyPublished;
    }
}
