package org.siemac.metamac.srm.web.shared.dsd;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
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
