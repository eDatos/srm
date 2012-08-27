package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import java.util.UUID;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;

public class ConceptsMetamacDoMocks extends ConceptsDoMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme() {

        ConceptSchemeVersionMetamac conceptSchemeVersion = new ConceptSchemeVersionMetamac();
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeVersion.setRelatedOperation(mockOperationExternalItem(UUID.randomUUID().toString()));

        mockConceptScheme(conceptSchemeVersion);
        return conceptSchemeVersion;
    }
    
    public static ConceptMetamac mockConcept() {

        ConceptMetamac concept = new ConceptMetamac();
        concept.setPluralName(mockInternationalString());
        concept.setAcronym(mockInternationalString());
        concept.setDescriptionSource(mockInternationalString());
        concept.setContext(mockInternationalString());
        concept.setDocMethod(mockInternationalString());
        concept.setSdmxRelatedArtefact(ConceptRoleEnum.DIMENSION);
        // TODO type
        concept.setDerivation(mockInternationalString());
        concept.setLegalActs(mockInternationalString());
        
        mockConcept(concept);
        return concept;
    }

    public static ExternalItem mockOperationExternalItem(String code) {
        return new ExternalItem(code, "http://" + code, "urn:" + code, TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
    }
}
