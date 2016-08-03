package org.siemac.metamac.srm.web.shared.criteria;

public class CodeWebCriteria extends ItemWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            shortName;

    public CodeWebCriteria() {
    }

    public CodeWebCriteria(String criteria) {
        super(criteria);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
