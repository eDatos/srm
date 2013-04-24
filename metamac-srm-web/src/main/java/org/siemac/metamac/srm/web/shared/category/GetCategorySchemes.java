package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategorySchemes {

    @In(1)
    int                                 firstResult;

    @In(2)
    int                                 maxResults;

    @In(3)
    CategorySchemeWebCriteria           categorySchemeWebCriteria;

    @Out(1)
    List<CategorySchemeMetamacBasicDto> categorySchemeList;

    @Out(2)
    Integer                             firstResultOut;

    @Out(3)
    Integer                             totalResults;
}
