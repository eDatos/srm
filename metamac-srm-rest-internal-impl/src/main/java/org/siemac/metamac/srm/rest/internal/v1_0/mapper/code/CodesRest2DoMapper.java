package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

public interface CodesRest2DoMapper {

    public RestCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistCriteriaMapper();
    public RestCriteria2SculptorCriteria<CodeMetamac> getCodeCriteriaMapper();
    public RestCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper();
    public RestCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper();
    public RestCriteria2SculptorCriteria<VariableElement> getVariableElementCriteriaMapper();
    public RestCriteria2SculptorCriteria<CodelistFamily> getCodelistFamilyCriteriaMapper();
}
