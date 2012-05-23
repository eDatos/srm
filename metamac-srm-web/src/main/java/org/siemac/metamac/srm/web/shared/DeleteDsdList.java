package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDsdList {

    @In(1)
    List<DataStructureDefinitionDto> dataStructureDefinitionDtos;

}
