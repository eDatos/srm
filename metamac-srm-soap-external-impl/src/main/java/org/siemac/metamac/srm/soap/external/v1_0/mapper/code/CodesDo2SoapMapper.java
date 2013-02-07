package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;

public interface CodesDo2SoapMapper {

    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source);
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> source, Integer pageSize) throws MetamacException;

    public Variable toVariable(org.siemac.metamac.srm.core.code.domain.Variable source);
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> source, Integer pageSize) throws MetamacException;
}
