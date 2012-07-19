package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteAttributeListForDsd {

    @In(1)
    Long                   idDsd;

    @In(2)
    List<DataAttributeDto> dataAttributeDtos;

    @In(3)
    TypeComponentList      typeComponentList;

}
