package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);

}
