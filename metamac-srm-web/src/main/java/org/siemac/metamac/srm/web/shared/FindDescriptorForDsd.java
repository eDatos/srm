package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;

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
