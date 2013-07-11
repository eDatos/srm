package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.base.utils.BaseDtoMocks;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public class ConceptsMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // -----------------------------------------------------------------------------------

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoGlossaryType(String codeMaintainer, String urnMaintainer) {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeDto.setMaintainer(OrganisationsMetamacDtoMocks.mockMaintainerDto(codeMaintainer, urnMaintainer));
        return conceptSchemeDto;
    }

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoOperationType(String codeMaintainer, String urnMaintainer) {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeDto.setRelatedOperation(BaseDtoMocks.mockExternalItemStatisticalOperationDto("operation1"));
        RelatedResourceDto maintainerDto = OrganisationsMetamacDtoMocks.mockMaintainerDto(codeMaintainer, urnMaintainer);
        maintainerDto.setUrnProvider(urnMaintainer);
        conceptSchemeDto.setMaintainer(maintainerDto);
        return conceptSchemeDto;
    }

    // -----------------------------------------------------------------------------------
    // CONCEPTS
    // -----------------------------------------------------------------------------------

    public static ConceptMetamacDto mockConceptDto(RepresentationTypeEnum typeRepresentationEnum) {
        ConceptMetamacDto conceptMetamacDto = new ConceptMetamacDto();
        ConceptsDtoMocks.mockConceptDto(conceptMetamacDto, typeRepresentationEnum);
        conceptMetamacDto.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        conceptMetamacDto.setConceptType(mockConceptTypeDto());
        conceptMetamacDto.setPluralName(BaseDtoMocks.mockInternationalStringDto("es", "pluralName"));
        conceptMetamacDto.setAcronym(BaseDtoMocks.mockInternationalStringDto("es", "acronym"));
        conceptMetamacDto.setDescriptionSource(BaseDtoMocks.mockInternationalStringDto("es", "descriptionSource"));
        conceptMetamacDto.setContext(BaseDtoMocks.mockInternationalStringDto("es", "context"));
        conceptMetamacDto.setDocMethod(BaseDtoMocks.mockInternationalStringDto("es", "docMethod"));
        conceptMetamacDto.setDerivation(BaseDtoMocks.mockInternationalStringDto("es", "derivation"));
        conceptMetamacDto.setLegalActs(BaseDtoMocks.mockInternationalStringDto("es", "legalActs"));
        return conceptMetamacDto;
    }

    public static RelatedResourceDto mockConceptRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setTitle(MetamacMocks.mockInternationalStringDto());
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CONCEPT);
        return relatedResourceDto;
    }

    public static QuantityDto mockQuantityDtoCommon(QuantityTypeEnum type, RelatedResourceDto unitCode, RelatedResourceDto numerator, RelatedResourceDto denominator, RelatedResourceDto baseQuantity,
            RelatedResourceDto baseLocation) {
        QuantityDto target = new QuantityDto();
        target.setQuantityType(type);
        if (QuantityUtils.isQuantityOrExtension(type)) {
            target.setUnitCode(unitCode);
            target.setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            target.setSignificantDigits(Integer.valueOf(5));
            target.setDecimalPlaces(Integer.valueOf(2));
            target.setUnitMultiplier(Integer.valueOf(10));
        }
        if (QuantityUtils.isAmountOrExtension(type)) {
            // nothing
        }
        if (QuantityUtils.isMagnitudeOrExtension(type)) {
            target.setMinimum(Integer.valueOf(1000));
            target.setMaximum(Integer.valueOf(2000));
        }
        if (QuantityUtils.isFractionOrExtension(type)) {
            target.setNumerator(numerator);
            target.setDenominator(denominator);
        }
        if (QuantityUtils.isRatioOrExtension(type)) {
            target.setIsPercentage(Boolean.FALSE);
            target.setPercentageOf(BaseDtoMocks.mockInternationalStringDto("es", "percentageOf"));
        }
        if (QuantityUtils.isRateOrExtension(type)) {
            // nothing
        }
        if (QuantityUtils.isIndexOrExtension(type)) {
            target.setBaseLocation(baseLocation);
        }
        if (QuantityUtils.isChangeRateOrExtension(type)) {
            target.setBaseQuantity(baseQuantity);
        }
        return target;
    }

    public static QuantityDto mockQuantityDtoTypeQuantity(RelatedResourceDto unitCode) {
        return mockQuantityDtoCommon(QuantityTypeEnum.QUANTITY, unitCode, null, null, null, null);
    }

    public static QuantityDto mockQuantityDtoTypeIndex(RelatedResourceDto unitCode, RelatedResourceDto numerator, RelatedResourceDto denominator, RelatedResourceDto baseLocation) {
        return mockQuantityDtoCommon(QuantityTypeEnum.INDEX, unitCode, numerator, denominator, null, baseLocation);
    }

    public static QuantityDto mockQuantityDtoTypeChangeRate(RelatedResourceDto unitCode, RelatedResourceDto numerator, RelatedResourceDto denominator, RelatedResourceDto baseQuantity) {
        return mockQuantityDtoCommon(QuantityTypeEnum.CHANGE_RATE, unitCode, numerator, denominator, baseQuantity, null);
    }

    // -----------------------------------------------------------------------------------
    // CONCEPT TYPES
    // -----------------------------------------------------------------------------------

    public static ConceptTypeDto mockConceptTypeDto() {
        ConceptTypeDto conceptTypeDto = new ConceptTypeDto();
        conceptTypeDto.setIdentifier("DERIVED");
        conceptTypeDto.setDescription(new InternationalStringDto());
        LocalisedStringDto en = new LocalisedStringDto();
        en.setLabel("Derived");
        en.setLocale("en");
        conceptTypeDto.getDescription().addText(en);
        LocalisedStringDto es = new LocalisedStringDto();
        en.setLabel("Derivado");
        en.setLocale("es");
        conceptTypeDto.getDescription().addText(es);
        return conceptTypeDto;
    }
}
