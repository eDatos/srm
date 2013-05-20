package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class ConceptMainFormLayout extends InternationalMainFormLayout {

    public void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        setCanEdit(ConceptsClientSecurityUtils.canUpdateConcept(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto)));
        setCanDelete(ConceptsClientSecurityUtils.canDeleteConcept(conceptSchemeMetamacDto));
    }
}
