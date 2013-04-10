package org.siemac.metamac.srm.web.shared.criteria;

public class OrganisationContactWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            organisationUrn;
    private String            name;
    private String            organisationUnit;
    private String            responsibility;

    public OrganisationContactWebCriteria() {
    }

    public OrganisationContactWebCriteria(String criteria) {
        super(criteria);
    }

    public String getOrganisationUrn() {
        return organisationUrn;
    }

    public String getName() {
        return name;
    }

    public String getOrganisationUnit() {
        return organisationUnit;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setOrganisationUrn(String organisationUrn) {
        this.organisationUrn = organisationUrn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganisationUnit(String organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }
}
