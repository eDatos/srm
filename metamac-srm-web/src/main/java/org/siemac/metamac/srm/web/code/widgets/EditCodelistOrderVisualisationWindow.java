package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCodelistOrderVisualisationWindow extends EditCodelistVisualisationWindow {

    public EditCodelistOrderVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an order can be edited when the order is not the alphabetical one and the version is not the temporal one
        return codelistVisualisationDto != null && codelistVisualisationDto.getId() != null ? !isAlphabeticalOrderVisualisation()
                && !UrnUtils.isTemporalUrn(codelistVisualisationDto.getCodelist().getUrn()) : true;
    }

    private boolean isAlphabeticalOrderVisualisation() {
        return SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(codelistVisualisationDto.getCode());
    }
}
