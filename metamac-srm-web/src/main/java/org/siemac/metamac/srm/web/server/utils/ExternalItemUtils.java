package org.siemac.metamac.srm.web.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.web.common.server.utils.DtoUtils;

public class ExternalItemUtils {

    public static ExternalItemDto getOperationAsExternalItemDto(Resource resource, String operationsApiEndpoint) {
        // Do not store REST API end point
        String uri = StringUtils.removeStart(resource.getSelfLink().getHref(), operationsApiEndpoint);

        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(resource.getId());
        externalItemDto.setUri(uri);
        externalItemDto.setUrn(resource.getUrn());
        externalItemDto.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        externalItemDto.setTitle(DtoUtils.getInternationalStringDtoFromInternationalString(resource.getTitle()));
        return externalItemDto;
    }

    public static List<ExternalItemDto> getOperationsAsExternalItemDtos(List<ResourceInternal> resources, String operationsApiEndpoint) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>(resources.size());
        for (Resource resource : resources) {
            externalItemDtos.add(getOperationAsExternalItemDto(resource, operationsApiEndpoint));
        }
        return externalItemDtos;
    }
}
