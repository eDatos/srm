package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistCategorisationsPanel extends CategorisationsPanel {

    private Boolean versioningBackground;

    public void updateVisibility(CodelistMetamacDto codelistMetamacDto) {
        super.setProcStatus(codelistMetamacDto.getLifeCycle().getProcStatus());
        this.versioningBackground = codelistMetamacDto.getVersioningBackground();
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCategorisation(procStatus, versioningBackground)) {
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
                if (!CodesClientSecurityUtils.canDeleteCategorisation(procStatus, versioningBackground, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
