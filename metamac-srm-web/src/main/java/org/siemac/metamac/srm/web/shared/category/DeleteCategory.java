package org.siemac.metamac.srm.web.shared.category;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteCategory {

    @In(1)
    String urn;
}
