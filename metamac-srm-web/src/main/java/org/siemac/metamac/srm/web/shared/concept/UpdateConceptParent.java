package org.siemac.metamac.srm.web.shared.concept;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateConceptParent {

    @In(1)
    String  conceptUrn;

    @In(2)
    String  newConceptParentUrn;

    @In(3)
    Integer newConceptIndex;
}
