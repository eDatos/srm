package org.siemac.metamac.srm.web.shared.category;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategorisation {

    @In(1)
    String            urn;

    @Out(1)
    CategorisationDto categorisationDto;
}
