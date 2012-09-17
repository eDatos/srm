package org.siemac.metamac.srm.web.shared.dsd;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDescriptorForDsd {

    @In(1)
    String        dsdUrn;

    @In(2)
    DescriptorDto descriptorDto;

}
