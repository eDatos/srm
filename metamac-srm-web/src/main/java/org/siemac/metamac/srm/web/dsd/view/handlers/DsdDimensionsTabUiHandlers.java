package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdDimensionsTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveDimension(DimensionComponentDto dimensionComponentDto);
    void deleteDimensions(List<DimensionComponentDto> dimensionComponentDtos);

}
