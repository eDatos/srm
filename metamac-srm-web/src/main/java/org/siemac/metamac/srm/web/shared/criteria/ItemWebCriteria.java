package org.siemac.metamac.srm.web.shared.criteria;

public class ItemWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            itemSchemeUrn;
    private Boolean           isLastVersion;

    public ItemWebCriteria() {
    }

    public ItemWebCriteria(String criteria) {
        super(criteria);
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }

    public String getItemSchemeUrn() {
        return itemSchemeUrn;
    }

    public void setItemSchemeUrn(String itemSchemeUrn) {
        this.itemSchemeUrn = itemSchemeUrn;
    }
}
