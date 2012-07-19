package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDescriptorForDsd {

    @In(1)
    Long          idDsd;

    @In(2)
    DescriptorDto descriptorDto;

}
