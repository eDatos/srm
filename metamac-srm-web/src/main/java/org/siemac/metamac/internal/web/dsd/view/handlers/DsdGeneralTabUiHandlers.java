package org.siemac.metamac.internal.web.dsd.view.handlers;

import org.siemac.metamac.domain_dto.DataStructureDefinitionDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGeneralTabUiHandlers extends UiHandlers {

    void retrieveDsd(Long id);
    void saveDsd(DataStructureDefinitionDto dataStructureDefinitionDto);

}
