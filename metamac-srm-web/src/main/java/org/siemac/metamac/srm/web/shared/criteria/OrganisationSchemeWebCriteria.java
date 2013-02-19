package org.siemac.metamac.srm.web.shared.criteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeWebCriteria extends VersionableResourceWebCriteria {

    private static final long          serialVersionUID = 1L;

    private OrganisationSchemeTypeEnum type;

    public OrganisationSchemeWebCriteria() {
    }

    public OrganisationSchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public OrganisationSchemeTypeEnum getType() {
        return type;
    }

    public void setType(OrganisationSchemeTypeEnum type) {
        this.type = type;
    }
}
