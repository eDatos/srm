package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CategorySchemeCategorisationsPanel extends CategorisationsPanel {

    public void updateVisibility(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        super.setCategorisedArtefactUrn(categorySchemeMetamacDto.getUrn());
        super.setCategorisedArtefactProcStatus(categorySchemeMetamacDto.getLifeCycle().getProcStatus());
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (CategoriesClientSecurityUtils.canCreateCategorisation(categorisedArtefactProcStatus)) {
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
                if (!CategoriesClientSecurityUtils.canDeleteCategorisation(categorisedArtefactProcStatus, categorisationRecord.getCategorisationDto())) {
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
                if (!CategoriesClientSecurityUtils.canCancelCategorisationValidity(categorisedArtefactProcStatus, categorisationRecord.getCategorisationDto())) {
                    return false;
                }
            }
        }
        return true;
    }
}
