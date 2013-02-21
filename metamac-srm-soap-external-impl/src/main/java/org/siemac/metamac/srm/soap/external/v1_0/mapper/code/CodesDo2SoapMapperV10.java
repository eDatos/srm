package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public interface CodesDo2SoapMapperV10 {

    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source);
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> source, Integer limit);

    public Variable toVariable(org.siemac.metamac.srm.core.code.domain.Variable source);
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> source, Integer limit);

    public CodelistFamily toCodelistFamily(org.siemac.metamac.srm.core.code.domain.CodelistFamily source);
    public CodelistFamilies toCodelistFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> result, Integer limit);

    public Codelists toCodelists(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac> result, Integer limit);
    public Codelist toCodelist(CodelistVersionMetamac source);
    public void toCodelist(CodelistVersionMetamac source, Codelist target);
    public Code toCode(CodeMetamac source);
    public void toCode(CodeMetamac source, Code target);
    public void toCode(ItemResult source, ItemSchemeVersion itemSchemeVersion, Code target);
}
