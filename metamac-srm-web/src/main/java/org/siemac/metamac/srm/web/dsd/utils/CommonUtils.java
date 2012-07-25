package org.siemac.metamac.srm.web.dsd.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.domain.srm.enume.domain.TypeDimensionComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeRelathionship;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.domain.srm.enume.domain.UsageStatus;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class CommonUtils {

    /**
     * Returns true if representationType = ENUMERATED.
     * 
     * @param representationType
     * @return
     */
    public static boolean isRepresentationTypeEnumerated(String representationType) {
        if (representationType != null && !representationType.isEmpty()) {
            return TypeRepresentationEnum.ENUMERATED.equals(TypeRepresentationEnum.valueOf(representationType)) ? true : false;
        }
        return false;
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

    // ENUM linkedHashMaps

    public static LinkedHashMap<String, String> getUsageStatusHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (UsageStatus u : UsageStatus.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().usageStatus() + u.getName());
            valueMap.put(u.toString(), value);
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTypeRelathionshipHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TypeRelathionship t : TypeRelathionship.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRelationship() + t.getName());
            valueMap.put(t.toString(), value);
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTypeRepresentationEnumHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TypeRepresentationEnum r : TypeRepresentationEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRepresentationEnum() + r.getName());
            valueMap.put(r.toString(), value);
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTypeDimensionComponentHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TypeDimensionComponent t : TypeDimensionComponent.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeDimensionComponent() + t.getName());
            valueMap.put(t.toString(), value);
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getFacetValueTypeHashMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (FacetValueTypeEnum f : FacetFormUtils.getDsdFacetValueTypeEnums()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
            valueMap.put(f.toString(), value);
        }
        return valueMap;
    }

    // DTO linkedHashMaps

    public static LinkedHashMap<String, String> getDimensionComponentDtoHashMap(List<DimensionComponentDto> dimensionComponentDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (DimensionComponentDto d : dimensionComponentDtos) {
            valueMap.put(d.getId().toString(), d.getCode());
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDescriptorDtoHashMap(List<DescriptorDto> descriptorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (DescriptorDto descriptorDto : descriptorDtos) {
            valueMap.put(descriptorDto.getId().toString(), descriptorDto.getCode());
        }
        return valueMap;
    }

}
