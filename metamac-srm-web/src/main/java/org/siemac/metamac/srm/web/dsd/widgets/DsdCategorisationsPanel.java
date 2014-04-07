package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdCategorisationsPanel extends CategorisationsPanel {

    private String operationCode;

    public void updateVisibility(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        super.setCategorisedArtefactProcStatus(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus());
        this.operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (DsdClientSecurityUtils.canCreateCategorisationForDataStructureDefinition(categorisedArtefactProcStatus, operationCode)) {
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
                if (!DsdClientSecurityUtils.canDeleteCategorisationForDataStructureDefinition(categorisedArtefactProcStatus, operationCode, categorisationRecord.getCategorisationDto())) {
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
                if (!DsdClientSecurityUtils.canCancelCategorisationValidityForDataStructureDefinition(categorisedArtefactProcStatus, operationCode, categorisationRecord.getCategorisationDto())) {
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
                if (!DsdClientSecurityUtils.canExportCategorisationForDataStructureDefinition(categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
