package org.siemac.metamac.srm.web.dsd.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdPrimaryMeasureTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void savePrimaryMeasure(ComponentDto componentDto);

}
