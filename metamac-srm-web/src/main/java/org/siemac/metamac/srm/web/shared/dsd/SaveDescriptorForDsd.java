package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveDescriptorForDsd {

    @In(1)
    String                            dsdUrn;

    @In(2)
    DescriptorDto                     descriptorDto;

    @Out(1)
    DescriptorDto                     descriptorDtoSaved;

    @Out(2)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;
}
