package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;

public class DsdCategorisationsPanel extends CategorisationsPanel {

    private String operationCode;

    public void updateVisibility(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        super.setProcStatus(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus());
        this.operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (DsdClientSecurityUtils.canModifyCategorisationForDataStructureDefinition(procStatus, operationCode)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public void showDeleteCategorisationButton() {
        if (DsdClientSecurityUtils.canModifyCategorisationForDataStructureDefinition(procStatus, operationCode)) {
            deleteCategorisationButton.show();
        }
    }
}
