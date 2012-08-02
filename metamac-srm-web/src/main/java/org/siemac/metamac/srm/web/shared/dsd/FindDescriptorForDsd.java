package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class FindDescriptorForDsd {

    @In(1)
    Long                idDsd;

    @In(2)
    TypeComponentList   typeComponentList;

    @Out(1)
    List<DescriptorDto> descriptorDtos;

}
