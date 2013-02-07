package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategoriesByScheme {

    @In(1)
    String                 schemeUrn;

    @Out(1)
    List<ItemHierarchyDto> categories;
}
