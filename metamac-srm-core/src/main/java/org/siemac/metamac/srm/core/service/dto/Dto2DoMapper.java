package org.siemac.metamac.srm.core.service.dto;

import org.dozer.MappingException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.vo.domain.ExternalItem;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;

public interface Dto2DoMapper {

    // public <T extends Item> T itemDtoToItem(ItemDto itemDto,
    // ServiceContext ctx, SdmxBaseService sdmxBaseService);

    // public <T extends ItemScheme> T itemschemeDtoToItemScheme(
    // ItemSchemeDto itemSchemeDto, ServiceContext ctx,
    // SdmxBaseService sdmxBaseService) throws MappingException,
    // OrganisationNotFoundException;

    public <T extends Component> T componentDtoToComponent(ComponentDto componentDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException;

    public <T extends ComponentList> T componentListDtoToComponentList(ComponentListDto componentListDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException;

    public DataStructureDefinition dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionDto dataStructureDefinitionDto, ServiceContext ctx, SdmxBaseService sdmxBaseService)
            throws MappingException, MetamacException;

    public Representation representationDtoToRepresentation(RepresentationDto representationDto, ServiceContext ctx, SdmxBaseService sdmxBaseService, Representation representationOlder)
            throws MappingException;

    public Facet facetDtoToFacet(FacetDto facetDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MappingException;

    public ExternalItem externalItemBtDtoToExternalItem(ExternalItemBtDto externalItemBtDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MappingException;

    public ExternalItemBt externalItemBtDtoToExternalItemBt(ExternalItemBtDto externalItemBtDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MappingException;

}
