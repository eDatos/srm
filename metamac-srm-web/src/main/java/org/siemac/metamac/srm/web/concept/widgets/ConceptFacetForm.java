package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.client.representation.widgets.BaseFacetForm;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;

public class ConceptFacetForm extends BaseFacetForm {

    public ConceptFacetForm() {
        textType.setValueMap(CommonUtils.getConceptFacetValueTypeHashMap());
    }

}
