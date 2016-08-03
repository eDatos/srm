package org.siemac.metamac.srm.web.shared.concept;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteConcept {

    @In(1)
    String urn;
}
