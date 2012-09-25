package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Component("organisationsDo2DtoMapper")
public class OrganisationsDo2DtoMapperImpl implements OrganisationsDo2DtoMapper {

    @Autowired
    @Qualifier("organisationsDo2DtoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2DtoMapper do2DtoMapper;

    @Override
    public OrganisationSchemeMetamacDto organisationSchemeMetamacDoToDto(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationSchemeMetamacDto target = new OrganisationSchemeMetamacDto();

        // TODO life cycle

        do2DtoMapper.organisationSchemeDoToDto(source);
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
