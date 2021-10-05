package org.siemac.metamac.srm.web.shared;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.web.common.client.resources.GlobalResources;

public class CommonSharedUtils {

    private static final EnumMap<StreamMessageStatusEnum, ImageResource> ICON_STREAM_MESSAGE_STATUS = new EnumMap(StreamMessageStatusEnum.class);
    static {
        ICON_STREAM_MESSAGE_STATUS.put(StreamMessageStatusEnum.FAILED, GlobalResources.RESOURCE.errorSmart());
        ICON_STREAM_MESSAGE_STATUS.put(StreamMessageStatusEnum.PENDING, GlobalResources.RESOURCE.warn());
        ICON_STREAM_MESSAGE_STATUS.put(StreamMessageStatusEnum.SENT, GlobalResources.RESOURCE.success());
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

    public static FormItemIcon getPublicationStreamStatusIcon(StreamMessageStatusEnum status) {
        if (status == null) {
            return null;
        }
        FormItemIcon icon = new FormItemIcon();
        icon.setSrc(ICON_STREAM_MESSAGE_STATUS.get(status).getURL());

        return icon;
    }

}
