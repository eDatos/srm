package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

@org.springframework.stereotype.Component("organisationsDo2DtoMapper")
public class OrganisationsDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements OrganisationsDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationSchemeMetamacDto target = new OrganisationSchemeMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));
        do2DtoMapperSdmxSrm.organisationSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources) {
        List<OrganisationSchemeMetamacDto> target = new ArrayList<OrganisationSchemeMetamacDto>(sources.size());
        for (OrganisationSchemeVersionMetamac source : sources) {
            target.add(organisationSchemeMetamacDoToDto(source));
        }
        return target;
    }

    @Override
    public OrganisationMetamacDto organisationMetamacDoToDto(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationMetamacDto target = new OrganisationMetamacDto();
        do2DtoMapperSdmxSrm.organisationDoToDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto organisationMetamacDoToRelatedResourceDto(OrganisationMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.organisationDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public List<ItemHierarchyDto> organisationMetamacDoListToItemHierarchyDtoList(List<OrganisationMetamac> sources) {
        List<ItemHierarchyDto> targets = new ArrayList<ItemHierarchyDto>(sources.size());
        for (OrganisationMetamac source : sources) {
            ItemHierarchyDto target = organisationMetamacDoToItemHierarchyDto(source);
            targets.add(target);
        }
        return targets;
    }

    private ItemHierarchyDto organisationMetamacDoToItemHierarchyDto(OrganisationMetamac organisationMetamac) {
        ItemHierarchyDto itemHierarchyDto = new ItemHierarchyDto();

        // Organisation
        OrganisationMetamacDto organisationMetamacDto = organisationMetamacDoToDto(organisationMetamac);
        itemHierarchyDto.setItem(organisationMetamacDto);

        // Children (only will be filled for OrganisationUnit type)
        for (Item item : organisationMetamac.getChildren()) {
            ItemHierarchyDto itemHierarchyChildrenDto = organisationMetamacDoToItemHierarchyDto((OrganisationMetamac) item);
            itemHierarchyDto.addChildren(itemHierarchyChildrenDto);
        }

        return itemHierarchyDto;
    }
}
