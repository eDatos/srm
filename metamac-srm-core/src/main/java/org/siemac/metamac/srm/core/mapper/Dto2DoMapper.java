package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;

public interface Dto2DoMapper {

    // public <T extends Item> T itemDtoToItem(ItemDto itemDto,
    // ServiceContext ctx, BaseService baseService);

    // public <T extends ItemScheme> T itemschemeDtoToItemScheme(
    // ItemSchemeDto itemSchemeDto, ServiceContext ctx,
    // BaseService baseService) throws MappingException,
    // OrganisationNotFoundException;

    public <T extends Component> T componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException;

    public <T extends ComponentList> T componentListDtoToComponentList(ServiceContext ctx, ComponentListDto componentListDto) throws MetamacException;

    public DataStructureDefinition dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException;

//    public Representation representationDtoToRepresentation(RepresentationDto representationDto, ServiceContext ctx, BaseService baseService, Representation representationOlder) throws MetamacException;
//
//    public ExternalItem externalItemDtoToExternalItem(ServiceContext ctx, ExternalItemDto externalItemDto) throws MetamacException;

}
