package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Codes;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;

public interface CodesDo2RestMapperV10 {

    public org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelists toCodelists(PagedResult<CodelistVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist toCodelist(CodelistVersionMetamac source);
    public void toCodelist(CodelistVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist target);

    public Codes toCodes(PagedResult<CodeMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Code toCode(CodeMetamac source);
}
