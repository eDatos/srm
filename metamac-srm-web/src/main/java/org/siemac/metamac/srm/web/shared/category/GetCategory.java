package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategory {

    @In(1)
    String                   urn;

    @Out(1)
    CategoryMetamacDto       categoryDto;

    @Out(2)
    CategorySchemeMetamacDto categorySchemeMetamacDto;
}
