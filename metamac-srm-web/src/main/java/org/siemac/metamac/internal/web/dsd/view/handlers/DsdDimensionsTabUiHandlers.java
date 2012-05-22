package org.siemac.metamac.internal.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.domain_dto.DimensionComponentDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdDimensionsTabUiHandlers extends UiHandlers {

    void retrieveDsd(Long id);
    void saveDimension(DimensionComponentDto dimensionComponentDto);
    void deleteDimensions(List<DimensionComponentDto> dimensionComponentDtos);

}
