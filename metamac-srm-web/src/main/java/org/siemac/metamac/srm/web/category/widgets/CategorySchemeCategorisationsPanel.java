package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

public class CategorySchemeCategorisationsPanel extends CategorisationsPanel {

    public void updateVisibility(ProcStatusEnum procStatus) {
        super.setProcStatus(procStatus);
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CategoriesClientSecurityUtils.canModifyCategorisation(procStatus)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public void showDeleteCategorisationButton() {
        if (CategoriesClientSecurityUtils.canModifyCategorisation(procStatus)) {
            deleteCategorisationButton.show();
        }
    }
}
