package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OrganisationSchemeCategorisationsPanel extends CategorisationsPanel {

    private OrganisationSchemeTypeEnum type;

    public void updateVisibility(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        super.setCategorisedArtefactProcStatus(organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
        this.type = organisationSchemeMetamacDto.getType();
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (OrganisationsClientSecurityUtils.canCreateCategorisationFromOrganisationScheme(categorisedArtefactProcStatus, type)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public boolean canAllCategorisationsBeDeleted(ListGridRecord[] records) {
        for (ListGridRecord record : records) {
            if (record instanceof CategorisationRecord) {
                CategorisationRecord categorisationRecord = (CategorisationRecord) record;
                if (!OrganisationsClientSecurityUtils.canDeleteCategorisationFromOrganisationScheme(categorisedArtefactProcStatus, type, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canCancelAllCategorisationsValidity(ListGridRecord[] records) {
        for (ListGridRecord record : records) {
            if (record instanceof CategorisationRecord) {
                CategorisationRecord categorisationRecord = (CategorisationRecord) record;
                if (!OrganisationsClientSecurityUtils.canCancelCategorisationValidityFromOrganisationScheme(categorisedArtefactProcStatus, type, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canExportAllCategorisations(ListGridRecord[] records) {
        for (ListGridRecord record : records) {
            if (record instanceof CategorisationRecord) {
                CategorisationRecord categorisationRecord = (CategorisationRecord) record;
                if (!OrganisationsClientSecurityUtils.canExportCategorisationFromOrganisationScheme(categorisedArtefactProcStatus, type, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
