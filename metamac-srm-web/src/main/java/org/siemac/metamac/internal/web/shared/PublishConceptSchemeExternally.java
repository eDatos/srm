package org.siemac.metamac.internal.web.shared;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure=false)
public class PublishConceptSchemeExternally {

    @In(1)
    String conceptSchemeUuid;
    
    @Out(1)
    ConceptSchemeDto conceptSchemeDto;
}
