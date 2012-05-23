package org.siemac.metamac.srm.web.shared;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemeByIdLogic {

    @In(1)
    String           idLogic;

    @Out(1)
    ConceptSchemeDto conceptSchemeDto;

}
