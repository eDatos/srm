package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.CategorisationsPanel;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;

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
        if (ConceptsClientSecurityUtils.canModifyCategorisation(procStatus, type, operationCode)) {
            newCategorisationButton.show();
        } else {
            newCategorisationButton.hide();
        }
    }

    @Override
    public void showDeleteCategorisationButton() {
        if (ConceptsClientSecurityUtils.canModifyCategorisation(procStatus, type, operationCode)) {
            deleteCategorisationButton.show();
        }
    }
}
