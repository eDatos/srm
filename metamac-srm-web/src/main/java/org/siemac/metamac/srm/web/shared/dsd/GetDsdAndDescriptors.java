package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdAndDescriptors {

    @In(1)
    String                     dsdUrn;

    @Out(1)
    DataStructureDefinitionDto dsd;

    @Out(2)
    DescriptorDto              primaryMeasure;

    @Out(3)
    DescriptorDto              dimensions;

    @Out(4)
    DescriptorDto              attributes;

    @Out(5)
    List<DescriptorDto>        groupKeys;

}
