package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConceptScheme {

    @In(1)
    ConceptSchemeDto conceptSchemeDto;

    @Out(1)
    ConceptSchemeDto savedConceptSchemeDto;
}
