package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public interface CodesDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists toCodelists(PagedResult<CodelistVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist toCodelist(CodelistVersionMetamac source) throws MetamacException;
    public void toCodelist(CodelistVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist target);

    public Codes toCodes(PagedResult<CodeMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Code toCode(CodeMetamac source);
    public void toCode(com.arte.statistic.sdmx.srm.core.code.domain.Code source, CodeType target);
    public void toCode(ItemResult source, ItemSchemeVersion itemSchemeVersion, CodeType target);
    public ResourceInternal toResource(CodeMetamac source);

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies toVariableFamilies(PagedResult<VariableFamily> sources, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily toVariableFamily(VariableFamily source) throws MetamacException;

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables toVariablesByFamily(String variableFamilyID, PagedResult<Variable> sources, String query, String orderBy,
            Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables toVariables(PagedResult<Variable> sources, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable toVariable(Variable source) throws MetamacException;
    public ResourceInternal toResource(Variable source);

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies toCodelistFamilies(PagedResult<CodelistFamily> sources, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily toCodelistFamily(CodelistFamily source) throws MetamacException;

}
