package org.siemac.metamac.srm.web.shared.concept;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendConceptSchemeStreamMessage {

    @In(1)
    String                  conceptSchemeUrn;

    @Out(1)
    ConceptSchemeMetamacDto conceptSchemeMetamacDto;

    @Out(2)
    MetamacWebException     notificationException;
}
