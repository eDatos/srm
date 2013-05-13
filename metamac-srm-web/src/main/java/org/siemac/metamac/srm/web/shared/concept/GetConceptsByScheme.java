package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptsByScheme {

    @In(1)
    String                                  conceptSchemeUrn;

    @In(2)
    String                                  locale;

    @Out(1)
    List<ConceptMetamacVisualisationResult> itemVisualisationResults;
}
