package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCategoryScheme {

    @In(1)
    CategorySchemeMetamacDto categorySchemeToSave;

    @Out(1)
    CategorySchemeMetamacDto categorySchemeSaved;

}
