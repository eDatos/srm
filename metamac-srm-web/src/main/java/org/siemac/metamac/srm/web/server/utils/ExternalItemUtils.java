package org.siemac.metamac.srm.web.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.web.common.server.utils.DtoUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class ExternalItemUtils {

    private static Map<OrganisationSchemeTypeEnum, TypeExternalArtefactsEnum> organisationSchemeTypeMappings         = new HashMap<OrganisationSchemeTypeEnum, TypeExternalArtefactsEnum>();
    private static Map<TypeExternalArtefactsEnum, String>                     resourceTypeKindMappings               = new HashMap<TypeExternalArtefactsEnum, String>();
    private static Map<TypeExternalArtefactsEnum, String>                     resourceTypeExceptionParameterMappings = new HashMap<TypeExternalArtefactsEnum, String>();

    static {
        organisationSchemeTypeMappings.put(OrganisationSchemeTypeEnum.AGENCY_SCHEME, TypeExternalArtefactsEnum.AGENCY_SCHEME);
        organisationSchemeTypeMappings.put(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, TypeExternalArtefactsEnum.DATA_CONSUMER_SCHEME);
        organisationSchemeTypeMappings.put(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, TypeExternalArtefactsEnum.DATA_PROVIDER_SCHEME);
        organisationSchemeTypeMappings.put(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, TypeExternalArtefactsEnum.ORGANISATION_UNIT_SCHEME);

        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.CONCEPT_SCHEME, SrmRestConstants.KIND_CONCEPT_SCHEME);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.CODELIST, SrmRestConstants.KIND_CODELIST);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.DATASTRUCTURE, SrmRestConstants.KIND_DATA_STRUCTURE);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.ORGANISATION_UNIT_SCHEME, SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.DATA_CONSUMER_SCHEME, SrmRestConstants.KIND_DATA_CONSUMER_SCHEME);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.DATA_PROVIDER_SCHEME, SrmRestConstants.KIND_DATA_PROVIDER_SCHEME);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.AGENCY_SCHEME, SrmRestConstants.KIND_AGENCY_SCHEME);
        resourceTypeKindMappings.put(TypeExternalArtefactsEnum.CATEGORY_SCHEME, SrmRestConstants.KIND_CATEGORY_SCHEME);

        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.CONCEPT_SCHEME, ServiceExceptionParameters.CONCEPT_SCHEME);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.CODELIST, ServiceExceptionParameters.CODELIST);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.DATASTRUCTURE, ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.ORGANISATION_UNIT_SCHEME, ServiceExceptionParameters.ORGANISATION_SCHEME);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.AGENCY_SCHEME, ServiceExceptionParameters.ORGANISATION_SCHEME);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.DATA_CONSUMER_SCHEME, ServiceExceptionParameters.ORGANISATION_SCHEME);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.DATA_PROVIDER_SCHEME, ServiceExceptionParameters.ORGANISATION_SCHEME);
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.CATEGORY_SCHEME, ServiceExceptionParameters.CATEGORY_SCHEME);
    }

    public static TypeExternalArtefactsEnum getOrganisationSchemeType(OrganisationSchemeTypeEnum type) {
        return organisationSchemeTypeMappings.get(type);
    }

    public static ExternalItemDto getOperationAsExternalItemDto(ResourceInternal resource) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(resource.getId());
        externalItemDto.setCodeNested(resource.getNestedId());
        externalItemDto.setUri(resource.getSelfLink().getHref());
        externalItemDto.setUrn(resource.getUrn());
        externalItemDto.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        externalItemDto.setTitle(DtoUtils.getInternationalStringDtoFromInternationalString(resource.getName()));
        externalItemDto.setManagementAppUrl(resource.getManagementAppLink());
        return externalItemDto;
    }

    public static List<ExternalItemDto> getOperationsAsExternalItemDtos(List<ResourceInternal> resources) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>(resources.size());
        for (ResourceInternal resource : resources) {
            externalItemDtos.add(getOperationAsExternalItemDto(resource));
        }
        return externalItemDtos;
    }

    public static String getResourceKind(TypeExternalArtefactsEnum type) {
        return resourceTypeKindMappings.get(type);
    }

    public static String getResourceNameParameter(TypeExternalArtefactsEnum type) {
        return resourceTypeExceptionParameterMappings.get(type);
    }
}
