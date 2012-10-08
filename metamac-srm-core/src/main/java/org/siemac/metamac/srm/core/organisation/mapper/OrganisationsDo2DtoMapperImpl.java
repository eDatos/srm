package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("organisationsDo2DtoMapper")
public class OrganisationsDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements OrganisationsDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2DtoMapper do2DtoMapper;

    @Override
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationSchemeMetamacDto target = new OrganisationSchemeMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifecycleMetadata()));
        do2DtoMapper.organisationSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources) {
        List<OrganisationSchemeMetamacDto> target = new ArrayList<OrganisationSchemeMetamacDto>();
        for (OrganisationSchemeVersionMetamac source : sources) {
            target.add(organisationSchemeMetamacDoToDto(source));
        }
        return target;
    }

}
