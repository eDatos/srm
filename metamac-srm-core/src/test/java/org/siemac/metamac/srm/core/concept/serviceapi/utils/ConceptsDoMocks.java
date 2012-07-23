package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;

public class ConceptsDoMocks {

    // TODO mocks

    public static ConceptSchemeVersion createConceptScheme() {
        ConceptSchemeVersion conceptSchemeVersion = new ConceptSchemeVersion();
        BaseDoMocks.fillItemSchemeToCreate(conceptSchemeVersion);
        return conceptSchemeVersion;
    }
}
