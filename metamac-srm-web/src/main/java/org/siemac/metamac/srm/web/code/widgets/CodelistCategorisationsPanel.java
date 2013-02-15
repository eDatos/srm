package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;

public class CodelistCategorisationsPanel extends CategorisationsPanel {

    public void updateVisibility(ProcStatusEnum procStatus) {
        super.setProcStatus(procStatus);
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CodesClientSecurityUtils.canModifyCategorisation(procStatus)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public void showDeleteCategorisationButton() {
        if (CodesClientSecurityUtils.canModifyCategorisation(procStatus)) {
            deleteCategorisationButton.show();
        }
    }
}
