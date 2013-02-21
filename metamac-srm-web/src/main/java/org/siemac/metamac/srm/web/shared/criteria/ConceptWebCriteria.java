package org.siemac.metamac.srm.web.shared.criteria;

public class ConceptWebCriteria extends ItemWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;
    private String            acronym;
    private String            descriptionSource;

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

    public String getAcronym() {
        return acronym;
    }

    public String getDescriptionSource() {
        return descriptionSource;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public void setDescriptionSource(String descriptionSource) {
        this.descriptionSource = descriptionSource;
    }
}
