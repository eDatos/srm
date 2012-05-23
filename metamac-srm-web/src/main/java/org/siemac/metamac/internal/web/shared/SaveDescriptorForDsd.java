package org.siemac.metamac.internal.web.shared;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveDescriptorForDsd {

    @In(1)
    Long          idDsd;

    @In(2)
    DescriptorDto descriptorDto;

    @Out(1)
    DescriptorDto descriptorDtoSaved;
}
