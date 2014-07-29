package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CopyCategoryScheme {

    @In(1)
    String                   categorySchemeUrn;

    @In(2)
    String                   code;

    @Out(1)
    CategorySchemeMetamacDto categorySchemeMetamacDto;
}
