package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDescriptorListForDsd {

    @In(1)
    Long                idDsd;

    @In(2)
    List<DescriptorDto> descriptorDtos;
}
