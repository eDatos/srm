package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.domain.srm.dto.ComponentDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdPrimaryMeasureTabUiHandlers extends UiHandlers {

    void retrieveDsd(Long id);
    void savePrimaryMeasure(ComponentDto componentDto);

}
