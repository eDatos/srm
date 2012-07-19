package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionConceptScheme {

    @In(1)
    Long             id;

    @Out(1)
    ConceptSchemeDto conceptSchemeDto;
}
