package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCodes {

    @In(1)
    int                  firstResult;

    @In(2)
    int                  maxResults;

    @In(3)
    CodeWebCriteria      criteria;

    @Out(1)
    List<CodeMetamacDto> codes;

    @Out(2)
    Integer              firstResultOut;

    @Out(3)
    Integer              totalResults;
}
