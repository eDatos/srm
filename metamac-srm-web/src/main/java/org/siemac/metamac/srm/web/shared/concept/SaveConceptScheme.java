package org.siemac.metamac.srm.web.shared.concept;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
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
