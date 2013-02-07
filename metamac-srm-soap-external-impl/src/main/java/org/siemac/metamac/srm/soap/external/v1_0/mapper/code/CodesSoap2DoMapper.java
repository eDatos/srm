package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.siemac.metamac.soap.criteria.mapper.SoapCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

public interface CodesSoap2DoMapper {

    public SoapCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper();
    public SoapCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper();
}
