package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdAndDescriptors {

    @In(1)
    Long                       idDsd;

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
