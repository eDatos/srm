package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import java.util.UUID;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public class ConceptsMetamacDoMocks extends ConceptsDoMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme(OrganisationMetamac maintainer) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(ConceptSchemeTypeEnum.OPERATION);
        target.setRelatedOperation(mockExternalItemOperation(UUID.randomUUID().toString()));
        mockConceptScheme(target, maintainer);
        return target;
    }

    public static ConceptSchemeVersionMetamac mockConceptSchemeFixedValues(String agencyID, String resourceID, String version) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(ConceptSchemeTypeEnum.OPERATION);
        target.setRelatedOperation(mockExternalItemOperation("operation-" + resourceID));
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
        target.setConceptType(conceptType);
        target.setDerivation(mockInternationalString("derivation"));
        target.setLegalActs(mockInternationalString("legalActs"));
        if (coreRepresentation != null) {
            mockConcept(target, RepresentationTypeEnum.ENUMERATION, coreRepresentation);
        } else {
            mockConcept(target, RepresentationTypeEnum.TEXT_FORMAT, null);
        }
        return target;
    }

    public static ConceptMetamac mockConceptSimpleFixedValues(String resourceID, ConceptSchemeVersionMetamac itemSchemeVersion) {
        ConceptMetamac target = new ConceptMetamac();
        mockConceptFixedValues(target, resourceID, itemSchemeVersion, null, RepresentationTypeEnum.ENUMERATION);
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
        target.setConceptType(mockConceptTypeFixedValues("conceptType1"));
        target.setVariable(CodesMetamacDoMocks.mockVariableFixedValues("variable1"));
        target.setQuantity(mockQuantityFixedValues(resourceID));
        mockConceptFixedValues(target, resourceID, itemSchemeVersion, parent, RepresentationTypeEnum.ENUMERATION);
        return target;
    }

    public static ExternalItem mockExternalItemOperation(String code) {
        ExternalItem target = new ExternalItem();
        target.setCode(code);
        target.setUri("/latest/operations/" + code);
        target.setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=" + code);
        target.setUrnInternal(null);
        target.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        target.setManagementAppUrl("/#operations;id=" + code);
        target.setTitle(null);
        return target;
    }

    public static ConceptType mockConceptTypeFixedValues(String identifier) {
        ConceptType target = new ConceptType();
        target.setIdentifier(identifier);
        target.setDescription(mockInternationalStringFixedValues("description", identifier));
        return target;
    }

    private static Quantity mockQuantityFixedValues(String resourceId) {
        Quantity target = new Quantity();
        target.setQuantityType(QuantityTypeEnum.CHANGE_RATE);
        target.setUnitCode(CodesMetamacDoMocks.mockCodeFixedValues("unitCode01", CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist01", "01.000"), null));
        target.setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        target.setSignificantDigits(Integer.valueOf(5));
        target.setDecimalPlaces(Integer.valueOf(2));
        target.setUnitMultiplier(Integer.valueOf(10));
        target.setMinimum(Integer.valueOf(1000));
        target.setMaximum(Integer.valueOf(2000));
        target.setNumerator(mockConceptSimpleFixedValues("numerator1", mockConceptSchemeFixedValues("agency01", "conceptScheme02", "01.000")));
        target.setDenominator(mockConceptSimpleFixedValues("denominator1", mockConceptSchemeFixedValues("agency01", "conceptScheme03", "01.000")));
        target.setIsPercentage(Boolean.FALSE);
        target.setPercentageOf(mockInternationalStringFixedValues("percentageOf", resourceId));
        target.setBaseQuantity(mockConceptSimpleFixedValues("baseQuantity1", mockConceptSchemeFixedValues("agency01", "conceptScheme04", "01.000")));
        // Note: following metadata are not in change_rate, but put it to test all metadata conversion. This entity never will be saved
        target.setBaseValue(Integer.valueOf(5));
        target.setBaseTime("2010");
        target.setBaseLocation(CodesMetamacDoMocks.mockCodeFixedValues("baseLocation01", CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist01", "01.000"), null));
        return target;
    }

}
