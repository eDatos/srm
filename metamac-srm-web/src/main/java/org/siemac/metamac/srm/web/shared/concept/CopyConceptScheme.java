package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CopyConceptScheme {

    @In(1)
    String                  conceptSchemeUrn;

    @In(2)
    String                  code;

    @Out(1)
    ConceptSchemeMetamacDto conceptSchemeMetamacDto;
}
