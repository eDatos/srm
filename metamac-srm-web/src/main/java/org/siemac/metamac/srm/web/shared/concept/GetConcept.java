package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConcept {

    @In(1)
    String                  urn;

    @Out(1)
    ConceptMetamacDto       conceptDto;

    @Out(2)
    List<ConceptMetamacDto> relatedConcepts;
    
    @Out(3)
    List<ConceptMetamacDto> relatedRoleConcepts;

}
