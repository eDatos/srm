package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCodelists {

    @In(1)
    int                           firstResult;

    @In(2)
    int                           maxResults;

    @In(3)
    CodelistWebCriteria           criteria;

    @Out(1)
    List<CodelistMetamacBasicDto> codelists;

    @Out(2)
    Integer                       firstResultOut;

    @Out(3)
    Integer                       totalResults;
}
