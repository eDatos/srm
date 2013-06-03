package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CancelCategorySchemeValidity {

    @In(1)
    List<String>                   urns;

    @Out(1)
    List<CategorySchemeMetamacDto> categorySchemeMetamacDtos;
}
