package org.siemac.metamac.srm.rest.external.v1_0.mapper.code;

import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public interface CodesDo2RestMapperV10 {

    org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelists toCodelists(PagedResult<CodelistVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelist toCodelist(CodelistVersionMetamac source) throws MetamacException;
    Resource toResource(CodelistVersion source);

    Codes toCodes(PagedResult<CodeMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit, Set<String> fields);

    Codes toCodes(List<ItemResult> sources, CodelistVersionMetamac codelistVersion, Set<String> fields);
    Code toCode(CodeMetamac source);
    ItemResource toResource(CodeMetamac source);

    org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilies toVariableFamilies(PagedResult<VariableFamily> sources, String query, String orderBy, Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamily toVariableFamily(VariableFamily source) throws MetamacException;

    org.siemac.metamac.rest.structural_resources.v1_0.domain.Variables toVariablesByFamily(String variableFamilyID, PagedResult<Variable> sources, String query, String orderBy, Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.Variables toVariables(PagedResult<Variable> sources, String query, String orderBy, Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.Variable toVariable(Variable source) throws MetamacException;
    Resource toResource(Variable source);

    org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElements toVariableElements(PagedResult<VariableElement> sources, String variableID, String query, String orderBy,
            Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElements toVariableElements(List<VariableElementResult> sources, String variableID, String query);
    String toVariableElementsGeoJson(List<VariableElementResult> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate);
    VariableElementsGeoInfo toVariableElementsGeoXml(List<VariableElementResult> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate);
    String toVariableElementsGeoJson(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate);
    VariableElementsGeoInfo toVariableElementsGeoXml(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources, VariableElementResultSelection selection,
            DateTime globalLastUpdatedDate);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElement toVariableElement(org.siemac.metamac.srm.core.code.domain.VariableElement source);

    org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilies toCodelistFamilies(PagedResult<CodelistFamily> sources, String query, String orderBy, Integer limit);
    org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamily toCodelistFamily(CodelistFamily source) throws MetamacException;

}
