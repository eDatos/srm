package org.siemac.metamac.srm.web.shared.criteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationWebCriteria extends ItemWebCriteria {

    private static final long          serialVersionUID = 1L;

    private OrganisationSchemeTypeEnum organisationSchemeType;

    public OrganisationWebCriteria() {
    }

    public OrganisationWebCriteria(String criteria) {
        super(criteria);
    }

    public OrganisationSchemeTypeEnum getOrganisationSchemeType() {
        return organisationSchemeType;
    }

    public void setOrganisationSchemeType(OrganisationSchemeTypeEnum organisationSchemeType) {
        this.organisationSchemeType = organisationSchemeType;
    }
}
