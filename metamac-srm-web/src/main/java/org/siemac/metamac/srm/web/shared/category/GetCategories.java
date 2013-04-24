package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategories {

    @In(1)
    int                           firstResult;

    @In(2)
    int                           maxResults;

    @In(3)
    CategoryWebCriteria           criteria;

    @Out(1)
    List<CategoryMetamacBasicDto> categoryMetamacDtos;

    @Out(2)
    Integer                       firstResultOut;

    @Out(3)
    Integer                       totalResults;
}
