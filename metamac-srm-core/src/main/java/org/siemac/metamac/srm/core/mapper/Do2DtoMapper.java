package org.siemac.metamac.srm.core.mapper;

import org.dozer.DozerBeanMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.serviceapi.BaseService;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationship;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;

public interface Do2DtoMapper {

    public DozerBeanMapper getMapperCore(TypeDozerCopyMode typeDozerCopyMode);

    public <T extends ComponentDto> T componentToComponentDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Component component, BaseService baseService);

    public <T extends ComponentListDto> T componentListToComponentListDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList, BaseService baseService);

    public RelationshipDto attributeRelationshipToattributeRelationshipDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, AttributeRelationship attributeRelationship,
            BaseService baseService);

    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition);
    
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition, BaseService baseService);

    public RepresentationDto representationToRepresentationDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Representation representation, BaseService baseService);

    public FacetDto facetToFacetDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Facet facet, BaseService baseService);

    public ExternalItemDto externalItemToExternalItemDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, ExternalItem externalItem, BaseService baseService);

}
