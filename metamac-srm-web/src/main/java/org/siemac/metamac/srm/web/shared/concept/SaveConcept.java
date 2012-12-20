package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConcept {

    @In(1)
    ConceptMetamacDto        conceptToSave;

    @In(2)
    List<String>             rolesToSave;

    @In(3)
    List<String>             relatedConceptsToSave;

    @Out(1)
    ConceptMetamacDto        conceptDto;

    @Out(2)
    List<RelatedResourceDto> roles;

    @Out(3)
    List<ConceptMetamacDto>  relatedConcepts;
}
