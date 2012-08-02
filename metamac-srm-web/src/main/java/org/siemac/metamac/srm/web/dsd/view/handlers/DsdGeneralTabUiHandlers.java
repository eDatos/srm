package org.siemac.metamac.srm.web.dsd.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto);

}
