package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;
import java.util.Set;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdAndDescriptors {

    @In(1)
    String                            dsdUrn;

    @In(2)
    Set<TypeComponentList>            descriptorsToRetrieve;

    @Out(1)
    DataStructureDefinitionMetamacDto dsd;

    @Out(2)
    DescriptorDto                     primaryMeasure;

    @Out(3)
    DescriptorDto                     dimensions;

    @Out(4)
    DescriptorDto                     attributes;

    @Out(5)
    List<DescriptorDto>               groupKeys;
}
