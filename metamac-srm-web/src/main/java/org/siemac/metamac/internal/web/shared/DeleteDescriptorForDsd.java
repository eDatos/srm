package org.siemac.metamac.internal.web.shared;

import org.siemac.metamac.domain_dto.DescriptorDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDescriptorForDsd {

    @In(1)
    Long          idDsd;

    @In(2)
    DescriptorDto descriptorDto;

}
