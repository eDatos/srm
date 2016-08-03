package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCategory {

    @In(1)
    CategoryMetamacDto categoryToSave;

    @Out(1)
    CategoryMetamacDto categorySaved;

}
