package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class OrganisationMainFormLayout extends InternationalMainFormLayout {

    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, OrganisationMetamacDto organisationMetamacDto) {
        setCanEdit(OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType()));
        setCanDelete(OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeMetamacDto, organisationMetamacDto.getHasBeenPublished()));
    }
}
