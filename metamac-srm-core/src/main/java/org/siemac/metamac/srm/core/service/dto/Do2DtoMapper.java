package org.siemac.metamac.srm.core.service.dto;

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
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationship;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;

public interface Do2DtoMapper {

    // public <T extends ItemDto> T itemToItemDto(Item item, ServiceContext ctx, SdmxBaseService sdmxBaseService);

    // public <T extends ItemSchemeDto> T itemschemeToItemSchemeDto(ItemScheme itemScheme, ServiceContext ctx,
    // SdmxBaseService sdmxBaseService);

    public DozerBeanMapper getMapperCore(TypeDozerCopyMode typeDozerCopyMode);

    public <T extends ComponentDto> T componentToComponentDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Component component, SdmxBaseService sdmxBaseService);

    public <T extends ComponentListDto> T componentListToComponentListDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList, SdmxBaseService sdmxBaseService);

    public RelationshipDto attributeRelationshipToattributeRelationshipDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, AttributeRelationship attributeRelationship,
            SdmxBaseService sdmxBaseService);

    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition,
            SdmxBaseService sdmxBaseService);
    
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition, SdmxBaseService sdmxBaseService);

    public RepresentationDto representationToRepresentationDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Representation representation, SdmxBaseService sdmxBaseService);

    public FacetDto facetToFacetDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, Facet facet, SdmxBaseService sdmxBaseService);

    public ExternalItemDto externalItemToExternalItemDto(ServiceContext ctx, TypeDozerCopyMode typeDozerCopyMode, ExternalItem externalItem, SdmxBaseService sdmxBaseService);

}
