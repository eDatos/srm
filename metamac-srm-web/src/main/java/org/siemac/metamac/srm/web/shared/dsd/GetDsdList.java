package org.siemac.metamac.srm.web.shared.dsd;

import java.util.ArrayList;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdList {

    @Out(1)
    ArrayList<DataStructureDefinitionDto> dsdDtos;

}
