package org.siemac.metamac.internal.web.shared;

import java.util.ArrayList;

import org.siemac.metamac.domain_dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdList {

    @Out(1)
    ArrayList<DataStructureDefinitionDto> dsdDtos;

}
