package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategorySchemeVersionList {

    @In(1)
    String                         urn;

    @Out(1)
    List<CategorySchemeMetamacDto> categorySchemeMetamacDtos;

}
