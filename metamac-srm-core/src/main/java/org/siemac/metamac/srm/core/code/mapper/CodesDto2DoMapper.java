package org.siemac.metamac.srm.core.code.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

public interface CodesDto2DoMapper {

    public CodelistVersionMetamac codelistDtoToDo(CodelistMetamacDto source) throws MetamacException;
    public CodeMetamac codeDtoToDo(CodeMetamacDto source) throws MetamacException;
}