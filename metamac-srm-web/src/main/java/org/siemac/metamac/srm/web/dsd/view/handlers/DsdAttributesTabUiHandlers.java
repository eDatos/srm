package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataAttributeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdAttributesTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveAttribute(DataAttributeDto dataAttributeDto);
    void deleteAttributes(List<DataAttributeDto> dataAttributeDtos);

}
