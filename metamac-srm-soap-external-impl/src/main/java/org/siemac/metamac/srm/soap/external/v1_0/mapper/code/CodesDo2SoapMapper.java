package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;

public interface CodesDo2SoapMapper {

    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source);
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> source, Integer limit) throws MetamacException;

    public Variable toVariable(org.siemac.metamac.srm.core.code.domain.Variable source);
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> source, Integer limit) throws MetamacException;

    public CodelistFamily toCodelistFamily(org.siemac.metamac.srm.core.code.domain.CodelistFamily source);
    public CodelistFamilies toCodelistFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> result, Integer limit);

    public Codelists toCodelists(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac> result, Integer limit);
}
