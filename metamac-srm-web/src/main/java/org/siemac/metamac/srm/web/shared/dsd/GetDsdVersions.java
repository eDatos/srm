package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdVersions {

    @In(1)
    String                                       urn;

    @Out(1)
    List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos;

}
