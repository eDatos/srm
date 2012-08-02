package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConcept {

    @In(1)
    String     urn;

    @Out(1)
    MetamacConceptDto conceptDto;

}
