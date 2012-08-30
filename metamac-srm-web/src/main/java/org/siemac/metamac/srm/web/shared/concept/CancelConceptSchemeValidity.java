package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class CancelConceptSchemeValidity {

    @In(1)
    List<String>                  urns;

    @Out(1)
    List<ConceptSchemeMetamacDto> conceptSchemeDtos;

}
