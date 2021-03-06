package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCodelistOpennessVisualisationWindow extends EditCodelistVisualisationWindow {

    public EditCodelistOpennessVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an openness level can be edited when the openness level is not the all expanded one and the version is not temporal one
        return codelistVisualisationDto != null && codelistVisualisationDto.getId() != null ? !isAllExpandedOpennessVisualisation()
                && !UrnUtils.isTemporalUrn(codelistVisualisationDto.getCodelist().getUrn()) : true;
    }

    private boolean isAllExpandedOpennessVisualisation() {
        return SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE.equals(codelistVisualisationDto.getCode());
    }
}
