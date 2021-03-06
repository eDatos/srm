package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetVariables {

    @In(1)
    int                    firstResult;

    @In(2)
    int                    maxResults;

    @In(3)
    VariableWebCriteria    criteria;

    @In(4)
    String                 variableFamilyUrn;

    @Out(1)
    List<VariableBasicDto> variables;

    @Out(2)
    Integer                firstResultOut;

    @Out(3)
    Integer                totalResults;
}
