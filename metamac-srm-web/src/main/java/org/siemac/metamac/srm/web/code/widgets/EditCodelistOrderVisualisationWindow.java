package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCodelistOrderVisualisationWindow extends EditCodelistVisualisationWindow {

    public EditCodelistOrderVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an order can be edited only when the order is not the alphabetical one
        return codelistVisualisationDto != null ? !SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(codelistVisualisationDto.getCode()) : true;
    }
}
