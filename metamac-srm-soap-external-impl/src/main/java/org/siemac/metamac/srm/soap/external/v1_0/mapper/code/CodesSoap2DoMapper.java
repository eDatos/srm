package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.SoapCriteria2SculptorCriteria;

public interface CodesSoap2DoMapper {

    public SoapCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper();
}
