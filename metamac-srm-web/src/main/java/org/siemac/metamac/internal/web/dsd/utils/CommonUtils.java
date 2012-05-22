package org.siemac.metamac.internal.web.dsd.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.domain_dto.ComponentDto;
import org.siemac.metamac.domain_dto.DataAttributeDto;
import org.siemac.metamac.domain_dto.DescriptorDto;
import org.siemac.metamac.domain_dto.DimensionComponentDto;
import org.siemac.metamac.internal.web.dsd.enums.RepresentationTypeEnum;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;

public class CommonUtils {

    /**
     * Returns true if representationType = ENUMERATED.
     * 
     * @param representationType
     * @return
     */
    public static boolean isRepresentationTypeEnumerated(String representationType) {
        if (representationType != null && !representationType.isEmpty()) {
            return RepresentationTypeEnum.ENUMERATED.equals(RepresentationTypeEnum.valueOf(representationType)) ? true : false;
        }
        return false;
    }

    /**
     * Returns {@link ExternalItemBtDto}s from selected codeIds
     * 
     * @param concepts
     * @param codeIds
     * @return
     */
    public static List<ExternalItemBtDto> getConcepts(List<ExternalItemBtDto> concepts, String[] codeIds) {
        List<ExternalItemBtDto> selectedConcepts = new ArrayList<ExternalItemBtDto>();
        for (String codeId : codeIds) {
            ExternalItemBtDto concept = ExternalItemUtils.getExternalItemBtDtoFromCodeId(concepts, codeId);
            selectedConcepts.add(concept);
        }
        return selectedConcepts;
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
    public static String getRoleListToString(List<ExternalItemBtDto> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(i != 0 ? ",  " : "");
            builder.append(list.get(i).getCodeId());
        }
        return builder.toString();
    }

}
