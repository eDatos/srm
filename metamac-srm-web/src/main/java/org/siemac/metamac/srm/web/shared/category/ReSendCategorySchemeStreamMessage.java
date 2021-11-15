package org.siemac.metamac.srm.web.shared.category;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendCategorySchemeStreamMessage {

    @In(1)
    String                   categorySchemeUrn;

    @Out(1)
    CategorySchemeMetamacDto categorySchemeMetamacDto;

    @Out(2)
    MetamacWebException      notificationException;
}
