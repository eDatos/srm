package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.domain.concept.dto.ConceptDto;

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
