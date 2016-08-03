package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.web.client.representation.widgets.BaseFacetForm;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;

public class DsdFacetForm extends BaseFacetForm {

    public DsdFacetForm() {
        textType.setValueMap(CommonUtils.getDsdFacetValueTypeHashMap());
    }
}
