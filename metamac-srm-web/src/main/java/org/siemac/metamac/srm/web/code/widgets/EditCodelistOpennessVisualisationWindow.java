package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCodelistOpennessVisualisationWindow extends EditCodelistVisualisationWindow {

    public EditCodelistOpennessVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an openness level can be edited only when the openness level is not the all expanded one
        return codelistVisualisationDto != null ? !SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE.equals(codelistVisualisationDto.getCode()) : true;
    }
}
