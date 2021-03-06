package org.siemac.metamac.srm.web.dsd.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.util.shared.ArrayUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.shared.CommonSharedUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRelathionship;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.UsageStatus;

public class CommonUtils extends CommonSharedUtils {

    // USAGE STATUS

    public static LinkedHashMap<String, String> getUsageStatusHashMap() {
        LinkedHashMap<String, String> usageStatusHashMap = new LinkedHashMap<String, String>();
        usageStatusHashMap.put(new String(), new String());
        for (UsageStatus u : UsageStatus.values()) {
            usageStatusHashMap.put(u.toString(), getUsageStatusName(u));
        }
        return usageStatusHashMap;
    }

    public static String getUsageStatusName(UsageStatus usageStatus) {
        return usageStatus != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().usageStatus() + usageStatus.name()) : null;
    }

    // TYPE RELATIONSHIP

    public static LinkedHashMap<String, String> getTypeRelationshipHashMap() {
        LinkedHashMap<String, String> typeRelationShipHashMap = new LinkedHashMap<String, String>();
        typeRelationShipHashMap.put(new String(), new String());
        for (TypeRelathionship t : TypeRelathionship.values()) {
            typeRelationShipHashMap.put(t.toString(), getTypeRelationshipName(t));
        }
        return typeRelationShipHashMap;
    }

    public static String getTypeRelationshipName(TypeRelathionship typeRelationship) {
        return typeRelationship != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRelationship() + typeRelationship.name()) : null;
    }

    public static boolean isGroupRelationshipType(String typeRelationship) {
        return StringUtils.equals(TypeRelathionship.GROUP_RELATIONSHIP.name(), typeRelationship);
    }

    public static boolean isDimensionRelationshipType(String typeRelationship) {
        return StringUtils.equals(TypeRelathionship.DIMENSION_RELATIONSHIP.name(), typeRelationship);
    }

    // TYPE DIMENSION COMPONENT

    public static LinkedHashMap<String, String> getTypeDimensionComponentHashMap() {
        LinkedHashMap<String, String> typeDimensionComponentHashMap = new LinkedHashMap<String, String>();
        typeDimensionComponentHashMap.put(new String(), new String());
        for (TypeDimensionComponent t : TypeDimensionComponent.values()) {
            typeDimensionComponentHashMap.put(t.name(), getTypeDimensionComponentName(t));
        }
        // Add special dimension type: SPATIAL
        typeDimensionComponentHashMap.put(SpecialDimensionTypeEnum.SPATIAL.name(), getSpecialDimensionTypeName(SpecialDimensionTypeEnum.SPATIAL));
        return typeDimensionComponentHashMap;
    }

    public static String getTypeDimensionComponentName(TypeDimensionComponent typeDimensionComponentEnum) {
        return typeDimensionComponentEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeDimensionComponent() + typeDimensionComponentEnum.name()) : null;
    }

    public static String getSpecialDimensionTypeName(SpecialDimensionTypeEnum specialDimensionTypeEnum) {
        return specialDimensionTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().specialDimensionTypeEnum() + specialDimensionTypeEnum.name()) : null;
    }

    public static String getDimensionTypeName(DimensionComponentDto dimensionComponentDto) {
        if (dimensionComponentDto.getSpecialDimensionType() != null) {
            return getSpecialDimensionTypeName(dimensionComponentDto.getSpecialDimensionType());
        } else {
            return getTypeDimensionComponentName(dimensionComponentDto.getTypeDimensionComponent());
        }
    }

    public static TypeDimensionComponent getTypeDimensionComponent(String type) {
        if (SpecialDimensionTypeEnum.SPATIAL.name().equals(type)) {
            return TypeDimensionComponent.DIMENSION;
        } else {
            return TypeDimensionComponent.valueOf(type);
        }
    }

    public static SpecialDimensionTypeEnum getSpecialDimensionType(String type) {
        if (SpecialDimensionTypeEnum.SPATIAL.name().equals(type)) {
            return SpecialDimensionTypeEnum.SPATIAL;
        }
        return null;
    }

    public static String getDimensionTypeAsString(DimensionComponentDto dimensionComponentDto) {
        if (dimensionComponentDto.getSpecialDimensionType() != null) {
            return dimensionComponentDto.getSpecialDimensionType().name();
        } else {
            return dimensionComponentDto.getTypeDimensionComponent().name();
        }
    }

    // DATA ATTRIBUTE TYPE

    public static LinkedHashMap<String, String> getDataAttributeTypeHashMap() {
        LinkedHashMap<String, String> dataAttributeTypeHasMap = new LinkedHashMap<String, String>();
        dataAttributeTypeHasMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (SpecialAttributeTypeEnum type : SpecialAttributeTypeEnum.values()) {
            dataAttributeTypeHasMap.put(type.name(), getSpecialAttributeTypeName(type));
        }
        return dataAttributeTypeHasMap;
    }

    public static String getSpecialAttributeTypeName(SpecialAttributeTypeEnum specialAttributeTypeEnum) {
        return specialAttributeTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().specialAttributeTypeEnum() + specialAttributeTypeEnum.name()) : null;
    }

    public static SpecialAttributeTypeEnum getSpecialAttributeTypeEnum(String type) {
        if (!StringUtils.isBlank(type)) {
            try {
                return SpecialAttributeTypeEnum.valueOf(type);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    // FACET VALUE TYPE

    public static LinkedHashMap<String, String> getDsdFacetValueTypeHashMap() {
        LinkedHashMap<String, String> facetValueTypeHashMap = new LinkedHashMap<String, String>();
        facetValueTypeHashMap.put(new String(), new String());
        for (FacetValueTypeEnum f : FacetFormUtils.getDsdFacetValueTypeEnums()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
            facetValueTypeHashMap.put(f.toString(), value);
        }
        return facetValueTypeHashMap;
    }

    // DTO linkedHashMaps

    public static LinkedHashMap<String, String> getDimensionComponentDtoHashMap(List<DimensionComponentDto> dimensionComponentDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (DimensionComponentDto d : dimensionComponentDtos) {
            valueMap.put(d.getUrn(), d.getCode());
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDescriptorDtoHashMap(List<DescriptorDto> descriptorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (DescriptorDto descriptorDto : descriptorDtos) {
            valueMap.put(descriptorDto.getUrn(), descriptorDto.getCode());
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDescriptorDtoHashMapWithEmpty(List<DescriptorDto> descriptorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (DescriptorDto descriptorDto : descriptorDtos) {
            valueMap.put(descriptorDto.getUrn(), descriptorDto.getCode());
        }
        return valueMap;
    }

    // DSD VISUALISATION METADATA

    /**
     * Decimals between 0 and 6
     */
    public static LinkedHashMap<String, String> getDsdShowDecimalsHashMap() {
        LinkedHashMap<String, String> dsdShowDecimalsHashMap = new LinkedHashMap<String, String>();
        dsdShowDecimalsHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (int i = 0; i < 7; i++) {
            dsdShowDecimalsHashMap.put(String.valueOf(i), String.valueOf(i));
        }
        return dsdShowDecimalsHashMap;
    }

    public static DimensionComponentDto getMesureDimension(List<DimensionComponentDto> dimensionComponentDtos) {
        if (dimensionComponentDtos != null) {
            for (DimensionComponentDto dimension : dimensionComponentDtos) {
                if (TypeDimensionComponent.MEASUREDIMENSION.equals(dimension.getTypeDimensionComponent())) {
                    return dimension;
                }
            }
        }
        return null;
    }

    public static String getConceptSchemeUrnOfMeasureDimensionRepresentation(List<DimensionComponentDto> dimensionComponentDtos) {
        DimensionComponentDto measureDimension = getMesureDimension(dimensionComponentDtos);
        if (measureDimension != null && measureDimension.getLocalRepresentation() != null && measureDimension.getLocalRepresentation().getEnumeration() != null) {
            return measureDimension.getLocalRepresentation().getEnumeration().getUrn();
        }
        return null;
    }

    /**
     * Returns true if representationType = ENUMERATED.
     * 
     * @param representationType
     * @return
     */
    public static boolean isRepresentationTypeEnumerated(String representationType) {
        if (representationType != null && !representationType.isEmpty()) {
            return RepresentationTypeEnum.ENUMERATION.equals(RepresentationTypeEnum.valueOf(representationType)) ? true : false;
        }
        return false;
    }

    public static boolean isDimensionTypeMeasureDimension(String typeDimensionComponent) {
        return StringUtils.equals(TypeDimensionComponent.MEASUREDIMENSION.name(), typeDimensionComponent);
    }

    public static boolean isAttributeTypeMeasureAttribute(String typeAttributeComponent) {
        return StringUtils.equals(SpecialAttributeTypeEnum.MEASURE_EXTENDS.name(), typeAttributeComponent);
    }

    public static boolean isDimensionTypeTimeDimension(String typeDimensionComponent) {
        return StringUtils.equals(TypeDimensionComponent.TIMEDIMENSION.name(), typeDimensionComponent);
    }

    public static boolean isAttributeTypeTimeAttribute(String typeAttributeComponent) {
        return StringUtils.equals(SpecialAttributeTypeEnum.TIME_EXTENDS.name(), typeAttributeComponent);
    }

    public static boolean isDimensionTypeTimeDimension(TypeDimensionComponent typeDimensionComponent) {
        return TypeDimensionComponent.TIMEDIMENSION.equals(typeDimensionComponent);
    }

    public static boolean isDimensionTypeSpatialDimension(String specialDimensionType) {
        if (!StringUtils.isBlank(specialDimensionType)) {
            try {
                SpecialDimensionTypeEnum specialDimensionTypeEnum = SpecialDimensionTypeEnum.valueOf(specialDimensionType);
                return SpecialDimensionTypeEnum.SPATIAL.equals(specialDimensionTypeEnum);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns attribute components of attributesDescriptor
     * 
     * @param attributesDescriptor
     * @return
     */
    public static List<DataAttributeDto> getAttributeComponents(DescriptorDto attributesDescriptor) {
        List<DataAttributeDto> dataAttributeDtos = new ArrayList<DataAttributeDto>();
        for (ComponentDto componentDto : attributesDescriptor.getComponents()) {
            if (componentDto instanceof DataAttributeDto) {
                DataAttributeDto dataAttributeDto = (DataAttributeDto) componentDto;
                dataAttributeDtos.add(dataAttributeDto);
            }
        }
        return dataAttributeDtos;
    }

    public static String getStatisticalOperationCodeFromDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        return dataStructureDefinitionMetamacDto.getStatisticalOperation() != null ? dataStructureDefinitionMetamacDto.getStatisticalOperation().getCode() : null;
    }

    public static String getStatisticalOperationCodeFromDsd(DataStructureDefinitionMetamacBasicDto dataStructureDefinitionMetamacDto) {
        return dataStructureDefinitionMetamacDto.getStatisticalOperation() != null ? dataStructureDefinitionMetamacDto.getStatisticalOperation().getCode() : null;
    }

    /**
     * Returns a {@link DimensionComponentDto} if the URN is in the list of urns specified
     * 
     * @param dimensionComponentDtos
     * @param urns
     * @return
     */
    public static List<DimensionComponentDto> getDimensionComponentDtosWithSpecifiedUrns(List<DimensionComponentDto> dimensionComponentDtos, String[] urns) {
        List<DimensionComponentDto> selectedDimensions = new ArrayList<DimensionComponentDto>();
        for (String urn : urns) {
            if (!StringUtils.isBlank(urn)) {
                for (DimensionComponentDto dimension : dimensionComponentDtos) {
                    if (StringUtils.equals(urn, dimension.getUrn())) {
                        selectedDimensions.add(dimension);
                    }
                }
            }
        }
        return selectedDimensions;
    }

    /**
     * Returns a {@link DescriptorDto} if the URN is in the list of urns specified
     * 
     * @param descriptorDtos
     * @param urns
     * @return
     */
    public static List<DescriptorDto> getDescriptorDtosWithSpecifiedUrns(List<DescriptorDto> descriptorDtos, String[] urns) {
        List<DescriptorDto> selectedDescriptors = new ArrayList<DescriptorDto>();
        for (String urn : urns) {
            if (!StringUtils.isBlank(urn)) {
                DescriptorDto descriptorDto = getDescriptorDtoWithSpecifiedUrn(descriptorDtos, urn);
                if (descriptorDto != null) {
                    selectedDescriptors.add(descriptorDto);
                }
            }
        }
        return selectedDescriptors;
    }

    public static DescriptorDto getDescriptorDtoWithSpecifiedUrn(List<DescriptorDto> descriptorDtos, String urn) {
        for (DescriptorDto descriptor : descriptorDtos) {
            if (StringUtils.equals(urn, descriptor.getUrn())) {
                return descriptor;
            }
        }
        return null;
    }

    public static String getDescriptorListAsString(Set<DescriptorDto> descriptorDtos) {
        StringBuilder builder = new StringBuilder();
        if (descriptorDtos != null) {
            List<DescriptorDto> descriptorList = new ArrayList<DescriptorDto>(descriptorDtos);
            for (int i = 0; i < descriptorList.size(); i++) {
                builder.append(i != 0 ? ",  " : "");
                builder.append(descriptorList.get(i).getCode());
            }
        }
        return builder.toString();
    }

    public static String getDimensionComponentListAsString(Set<DimensionComponentDto> dimensionComponentDtos) {
        StringBuilder builder = new StringBuilder();
        if (dimensionComponentDtos != null) {
            List<DimensionComponentDto> dimensions = new ArrayList<DimensionComponentDto>(dimensionComponentDtos);
            for (int i = 0; i < dimensions.size(); i++) {
                builder.append(i != 0 ? ",  " : "");
                builder.append(dimensions.get(i).getCode());
            }
        }
        return builder.toString();
    }

    public static String[] getUrnsFromDescriptorDtos(Set<DescriptorDto> descriptorDtos) {
        if (descriptorDtos != null) {
            List<DescriptorDto> descriptorList = new ArrayList<DescriptorDto>(descriptorDtos);
            String[] urns = new String[descriptorList.size()];
            for (int i = 0; i < descriptorList.size(); i++) {
                urns[i] = descriptorList.get(i).getUrn();
            }
            return urns;
        }
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public static String[] getUrnsFromDimensionComponentDtos(Set<DimensionComponentDto> dimensionComponentDtos) {
        if (dimensionComponentDtos != null) {
            List<DimensionComponentDto> dimensions = new ArrayList<DimensionComponentDto>(dimensionComponentDtos);
            String[] urns = new String[dimensions.size()];
            for (int i = 0; i < dimensions.size(); i++) {
                urns[i] = dimensions.get(i).getUrn();
            }
            return urns;
        }
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    // METADATA VISIBILITY

    public static boolean isDimensionRoleVisible(TypeDimensionComponent typeDimensionComponent) {
        return !isDimensionTypeTimeDimension(typeDimensionComponent);
    }

    public static boolean isDimensionCodelistEnumeratedRepresentationVisible(String representationType, String dimensionType) {
        // Show Codelist if RepresentationTypeEnum = ENUMERATED (except in MeasureDimension)
        return isRepresentationTypeEnumerated(representationType) && !isDimensionTypeMeasureDimension(dimensionType);
    }

    public static boolean isDimensionConceptSchemeEnumeratedRepresentationVisible(String representationType, String dimensionType) {
        // Show ConceptScheme if RepresentationTypeEnum = ENUMERATED and TypeDimensionComponent == MEASUREDIMENSION
        return isRepresentationTypeEnumerated(representationType) && isDimensionTypeMeasureDimension(dimensionType);
    }

    public static boolean isAttributeCodelistEnumeratedRepresentationVisible(String representationType, String attributeType) {
        // Show Codelist if RepresentationTypeEnum = ENUMERATED (except in measure attributes)
        return isRepresentationTypeEnumerated(representationType) && !isAttributeTypeMeasureAttribute(attributeType);
    }

    public static boolean isAttributeConceptSchemeEnumeratedRepresentationVisible(String representationType, String attributeType) {
        // Show ConceptScheme if RepresentationTypeEnum = ENUMERATED and attribute type is measure
        return isRepresentationTypeEnumerated(representationType) && isAttributeTypeMeasureAttribute(attributeType);
    }
}
