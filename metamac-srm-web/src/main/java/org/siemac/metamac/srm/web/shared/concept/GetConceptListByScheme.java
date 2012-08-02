package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptListByScheme {

    @In(1)
    String           conceptSchemeUrn;

    @Out(1)
    List<ConceptDto> conceptDto;

}
