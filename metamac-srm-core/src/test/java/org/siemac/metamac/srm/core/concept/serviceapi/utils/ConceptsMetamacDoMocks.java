package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import java.util.UUID;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public class ConceptsMetamacDoMocks extends ConceptsDoMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme(OrganisationMetamac maintainer) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(ConceptSchemeTypeEnum.OPERATION);
        target.setRelatedOperation(mockOperationExternalItem(UUID.randomUUID().toString()));
        mockConceptScheme(target, maintainer);
        return target;
    }

    public static ConceptSchemeVersionMetamac mockConceptSchemeFixedValues(String agencyID, String resourceID, String version) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(ConceptSchemeTypeEnum.OPERATION);
        target.setRelatedOperation(mockOperationExternalItem("operation-" + resourceID));
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        mockConceptSchemeFixedValues(target, agencyID, resourceID, version);
        return target;
    }

    public static ConceptMetamac mockConcept(ConceptType conceptType, CodelistVersion coreRepresentation) {
        ConceptMetamac target = new ConceptMetamac();
        target.setPluralName(mockInternationalString("pluralName"));
        target.setAcronym(mockInternationalString("acronym"));
        target.setDescriptionSource(mockInternationalString("descriptionSource"));
        target.setContext(mockInternationalString("context"));
        target.setDocMethod(mockInternationalString("docMethod"));
        target.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        target.setType(conceptType);
        target.setDerivation(mockInternationalString("derivation"));
        target.setLegalActs(mockInternationalString("legalActs"));
        mockConcept(target, RepresentationTypeEnum.ENUMERATION, coreRepresentation);
        return target;
    }

    public static ConceptMetamac mockConceptFixedValues(String resourceID, ConceptSchemeVersionMetamac itemSchemeVersion, ConceptMetamac parent) {
        ConceptMetamac target = new ConceptMetamac();
        target.setPluralName(mockInternationalStringFixedValues("pluralName", resourceID));
        target.setAcronym(mockInternationalStringFixedValues("acronym", resourceID));
        target.setDescriptionSource(mockInternationalStringFixedValues("descriptionSource", resourceID));
        target.setContext(mockInternationalStringFixedValues("context", resourceID));
        target.setDocMethod(mockInternationalStringFixedValues("docMethod", resourceID));
        target.setDerivation(mockInternationalStringFixedValues("derivation", resourceID));
        target.setLegalActs(mockInternationalStringFixedValues("legalActs", resourceID));
        target.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        target.setType(mockConceptTypeFixedValues("conceptType1"));
        target.setVariable(CodesMetamacDoMocks.mockVariableFixedValues("variable1"));
        mockConceptFixedValues(target, resourceID, itemSchemeVersion, parent, RepresentationTypeEnum.ENUMERATION);
        return target;
    }

    public static ExternalItem mockOperationExternalItem(String code) {
        ExternalItem target = new ExternalItem();
        target.setCode(code);
        target.setUri("http://" + code);
        target.setUrn("urn:" + code);
        target.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        target.setManagementAppUrl("managementAppUrl" + code);
        target.setTitle(null);
        return target;
    }

    public static ConceptType mockConceptTypeFixedValues(String identifier) {
        ConceptType target = new ConceptType();
        target.setIdentifier(identifier);
        target.setDescription(mockInternationalStringFixedValues("description", identifier));
        return target;
    }

}
