package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.domain.srm.dto.ComponentDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdPrimaryMeasureTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void savePrimaryMeasure(ComponentDto componentDto);

}
