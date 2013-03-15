package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeCategorisationsPanel extends CategorisationsPanel {

    private String                operationCode;
    private ConceptSchemeTypeEnum type;

    public void updateVisibility(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        super.setProcStatus(conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
        this.operationCode = CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        this.type = conceptSchemeMetamacDto.getType();
        updateNewButtonVisibility();
    }

    @Override
    public void updateNewButtonVisibility() {
        if (ConceptsClientSecurityUtils.canCreateCategorisation(procStatus, type, operationCode)) {
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
                if (!ConceptsClientSecurityUtils.canDeleteCategorisation(procStatus, type, operationCode, categorisationRecord.getMaintainer())) {
                    return false;
                }
            }
        }
        return true;
    }
}
