package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistCategorisationsPanel extends CategorisationsPanel {

    private Boolean versioningBackground;

    public void updateVisibility(CodelistMetamacDto codelistMetamacDto) {
        super.setCategorisedArtefactProcStatus(codelistMetamacDto.getLifeCycle().getProcStatus());
        this.versioningBackground = codelistMetamacDto.getIsTaskInBackground();
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCategorisation(categorisedArtefactProcStatus, versioningBackground)) {
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
                if (!CodesClientSecurityUtils.canDeleteCategorisation(categorisedArtefactProcStatus, versioningBackground, categorisationRecord.getCategorisationDto())) {
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
                if (!CodesClientSecurityUtils.canCancelCategorisationValidity(categorisedArtefactProcStatus, versioningBackground, categorisationRecord.getCategorisationDto())) {
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
                if (!CodesClientSecurityUtils.canExportCategorisation(categorisedArtefactProcStatus, versioningBackground, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
