package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConcept {

    @In(1)
    String                       urn;

    @Out(1)
    ConceptMetamacDto            conceptDto;

    @Out(2)
    List<RelatedResourceDto>     roles;

    @Out(3)
    List<ConceptMetamacBasicDto> relatedConcepts;

    @Out(4)
    ConceptSchemeMetamacDto      conceptSchemeMetamacDto;
}
