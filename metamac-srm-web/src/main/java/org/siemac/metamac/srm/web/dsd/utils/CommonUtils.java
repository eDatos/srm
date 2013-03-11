package org.siemac.metamac.srm.web.dsd.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;

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

public class CommonUtils {

    private static LinkedHashMap<String, String> usageStatusHashMap            = null;
    private static LinkedHashMap<String, String> typeRelationShipHashMap       = null;
    private static LinkedHashMap<String, String> typeDimensionComponentHashMap = null;
    private static LinkedHashMap<String, String> dataAttributeTypeHasMap       = null;
    private static LinkedHashMap<String, String> facetValueTypeHashMap         = null;
    private static LinkedHashMap<String, String> dsdShowDecimalsHashMap        = null;

    // USAGE STATUS

    public static LinkedHashMap<String, String> getUsageStatusHashMap() {
        if (usageStatusHashMap == null) {
            usageStatusHashMap = new LinkedHashMap<String, String>();
            usageStatusHashMap.put(new String(), new String());
            for (UsageStatus u : UsageStatus.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().usageStatus() + u.getName());
                usageStatusHashMap.put(u.toString(), value);
            }
        }
        return usageStatusHashMap;
    }

    // TYPE RELATIONSHIP

    public static LinkedHashMap<String, String> getTypeRelathionshipHashMap() {
        if (typeRelationShipHashMap == null) {
            typeRelationShipHashMap = new LinkedHashMap<String, String>();
            typeRelationShipHashMap.put(new String(), new String());
            for (TypeRelathionship t : TypeRelathionship.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRelationship() + t.getName());
                typeRelationShipHashMap.put(t.toString(), value);
            }
        }
        return typeRelationShipHashMap;
    }

    // TYPE DIMENSION COMPONENT

    public static LinkedHashMap<String, String> getTypeDimensionComponentHashMap() {
        if (typeDimensionComponentHashMap == null) {
            typeDimensionComponentHashMap = new LinkedHashMap<String, String>();
            typeDimensionComponentHashMap.put(new String(), new String());
            for (TypeDimensionComponent t : TypeDimensionComponent.values()) {
                typeDimensionComponentHashMap.put(t.name(), getTypeDimensionComponentName(t));
            }
            // Add special dimension type: SPATIAL
            typeDimensionComponentHashMap.put(SpecialDimensionTypeEnum.SPATIAL.name(), getSpecialDimensionTypeName(SpecialDimensionTypeEnum.SPATIAL));
        }
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
        if (dataAttributeTypeHasMap == null) {
            dataAttributeTypeHasMap = new LinkedHashMap<String, String>();
            dataAttributeTypeHasMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
            for (SpecialAttributeTypeEnum type : SpecialAttributeTypeEnum.values()) {
                dataAttributeTypeHasMap.put(type.name(), getSpecialAttributeTypeName(type));
            }
        }
        return dataAttributeTypeHasMap;
    }

    public static String getSpecialAttributeTypeName(SpecialAttributeTypeEnum specialAttributeTypeEnum) {
        return specialAttributeTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().specialAttributeTypeEnum() + specialAttributeTypeEnum.name()) : null;
    }

    // FACET VALUE TYPE

    public static LinkedHashMap<String, String> getDsdFacetValueTypeHashMap() {
        if (facetValueTypeHashMap == null) {
            facetValueTypeHashMap = new LinkedHashMap<String, String>();
            facetValueTypeHashMap.put(new String(), new String());
            for (FacetValueTypeEnum f : FacetFormUtils.getDsdFacetValueTypeEnums()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
                facetValueTypeHashMap.put(f.toString(), value);
            }
        }
        return facetValueTypeHashMap;
    }

    // DTO linkedHashMaps

    public static LinkedHashMap<String, String> getDimensionComponentDtoHashMap(List<DimensionComponentDto> dimensionComponentDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (DimensionComponentDto d : dimensionComponentDtos) {
            valueMap.put(d.getUrn(), d.getCode());
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDescriptorDtoHashMap(List<DescriptorDto> descriptorDtos) {
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
        if (dsdShowDecimalsHashMap == null) {
            dsdShowDecimalsHashMap = new LinkedHashMap<String, String>();
            dsdShowDecimalsHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
            for (int i = 0; i < 7; i++) {
                dsdShowDecimalsHashMap.put(String.valueOf(i), String.valueOf(i));
            }
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

    public static boolean isDimensionTypeTimeDimension(String typeDimensionComponent) {
        return StringUtils.equals(TypeDimensionComponent.TIMEDIMENSION.name(), typeDimensionComponent);
    }

    public static boolean isDimensionTypeTimeDimension(TypeDimensionComponent typeDimensionComponent) {
        return TypeDimensionComponent.TIMEDIMENSION.equals(typeDimensionComponent);
    }

    /**
     * Returns dimension components of dimensionsDescriptor
     * 
     * @param dimensionsDescriptor
     * @return
     */
    public static List<DimensionComponentDto> getDimensionComponents(DescriptorDto dimensionsDescriptor) {
        List<DimensionComponentDto> dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
        for (ComponentDto componentDto : dimensionsDescriptor.getComponents()) {
            if (componentDto instanceof DimensionComponentDto) {
                DimensionComponentDto dimensionComponentDto = (DimensionComponentDto) componentDto;
                dimensionComponentDtos.add(dimensionComponentDto);
            }
        }
        return dimensionComponentDtos;
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

    /**
     * @param list
     * @return
     */
    public static String getRoleListToString(List<ExternalItemDto> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(i != 0 ? ",  " : "");
            builder.append(list.get(i).getCode());
        }
        return builder.toString();
    }

    public static String getStatisticalOperationCodeFromDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
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

    // METADATA VISIBILITY

    public static boolean isDimensionRoleVisible(TypeDimensionComponent typeDimensionComponent) {
        return !isDimensionTypeTimeDimension(typeDimensionComponent);
    }

    public static boolean isDimensionCodelistEnumeratedRepresenationVisible(String representationType, String dimensionType) {
        // Show CodeList if RepresentationTypeEnum = ENUMERATED (except in MeasureDimension)
        return isRepresentationTypeEnumerated(representationType) && !isDimensionTypeMeasureDimension(dimensionType);
    }
}
