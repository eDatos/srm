package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptDto;

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
