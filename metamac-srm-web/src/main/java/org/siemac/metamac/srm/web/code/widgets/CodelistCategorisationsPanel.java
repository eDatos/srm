package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistCategorisationsPanel extends CategorisationsPanel {

    public void updateVisibility(ProcStatusEnum procStatus) {
        super.setProcStatus(procStatus);
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCategorisation(procStatus)) {
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
                if (!CodesClientSecurityUtils.canDeleteCategorisation(procStatus, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
