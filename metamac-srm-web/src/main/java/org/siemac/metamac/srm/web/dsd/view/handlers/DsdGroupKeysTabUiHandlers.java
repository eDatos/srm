package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGroupKeysTabUiHandlers extends UiHandlers {

    void saveGroupKeys(DescriptorDto descriptorDto);
    void deleteGroupKeys(List<DescriptorDto> descriptorDtos);
}
