package org.siemac.metamac.srm.web.shared.category;

import java.util.Date;
import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class CancelCategorisationValidity {

    @In(1)
    List<String> urns;

    @In(2)
    Date         validTo;
}
