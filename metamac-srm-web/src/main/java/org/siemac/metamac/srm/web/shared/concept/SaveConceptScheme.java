package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConceptScheme {

    @In(1)
    ConceptSchemeMetamacDto conceptSchemeDto;

    @Out(1)
    ConceptSchemeMetamacDto savedConceptSchemeDto;
}
