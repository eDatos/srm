package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCodelistFamilies {

    @In(1)
    int                     firstResult;

    @In(2)
    int                     maxResults;

    @In(3)
    String                  criteria;

    @Out(1)
    List<CodelistFamilyDto> families;

    @Out(2)
    Integer                 firstResultOut;

    @Out(3)
    Integer                 totalResults;

}
