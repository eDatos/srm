package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConcept {

    @In(1)
    ConceptMetamacDto conceptToSave;

    @Out(1)
    ConceptMetamacDto conceptDto;

}
