package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto);

}
