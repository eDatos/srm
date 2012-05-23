package org.siemac.metamac.internal.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdGroupKeysTabUiHandlers extends UiHandlers {

    void retrieveDsd(Long id);
    void saveGroupKeys(DescriptorDto descriptorDto);
    void deleteGroupKeys(List<DescriptorDto> descriptorDtos);

}
