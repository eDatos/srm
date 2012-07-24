package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.domain.concept.enums.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;

public class ConceptsDoMocks {

    public static ConceptSchemeVersion createConceptScheme() {
        ConceptSchemeVersion conceptSchemeVersion = new ConceptSchemeVersion();
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeVersion.setRelatedOperation(new ExternalItem("relatedOperation" + MetamacMocks.mockString(10), BaseDoMocks.URI_MOCK, "urn:" + "relatedOperation" + MetamacMocks.mockString(10), TypeExternalArtefactsEnum.CONCEPT_SCHEME));
        
        // item scheme
        BaseDoMocks.fillItemSchemeToCreate(conceptSchemeVersion);
        return conceptSchemeVersion;
    }
}
