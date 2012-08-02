package org.siemac.metamac.srm.web.shared.concept;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConcept {

    @In(1)
    ConceptDto conceptToSave;

    @Out(1)
    ConceptDto conceptDto;

}
