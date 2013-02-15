package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeCategorisationsPanel extends CategorisationsPanel {

    private OrganisationSchemeTypeEnum type;

    public void updateVisibility(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        super.setProcStatus(procStatus);
        this.type = organisationSchemeMetamacDto.getType();
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (OrganisationsClientSecurityUtils.canModifyCategorisationFromOrganisationScheme(procStatus, type)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public void showDeleteCategorisationButton() {
        if (OrganisationsClientSecurityUtils.canModifyCategorisationFromOrganisationScheme(procStatus, type)) {
            deleteCategorisationButton.show();
        }
    }
}
