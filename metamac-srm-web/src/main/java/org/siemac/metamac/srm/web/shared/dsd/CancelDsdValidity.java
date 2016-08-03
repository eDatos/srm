package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CancelDsdValidity {

    @In(1)
    List<String>                            urns;

    @Out(1)
    List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos;

}
