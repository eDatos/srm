package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.srm.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.srm.v1_0.domain.VariableFamily;

public interface CodesDo2SoapMapper {

    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source);
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> source, Integer pageSize) throws MetamacException;
}
