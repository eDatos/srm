package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemeVersions {

    @In(1)
    String                             urn;

    @Out(1)
    List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos;
}
