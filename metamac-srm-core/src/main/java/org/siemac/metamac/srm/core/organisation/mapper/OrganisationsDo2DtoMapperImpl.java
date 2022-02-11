package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

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
        target.setStreamMessageStatus(source.getStreamMessageStatus());
        do2DtoMapperSdmxSrm.organisationSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto organisationSchemeMetamacDoToRelatedResourceDto(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.organisationSchemeDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public OrganisationSchemeMetamacBasicDto organisationSchemeMetamacDoToBasicDto(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationSchemeMetamacBasicDto target = new OrganisationSchemeMetamacBasicDto();
        target.setType(source.getOrganisationSchemeType());
        target.setStreamMessageStatus(source.getStreamMessageStatus());
        itemSchemeVersionDoToItemSchemeBasicDto(source, source.getLifeCycleMetadata(), target);
        return target;
    }

    @Override
    public List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDoListToDtoList(List<OrganisationSchemeVersionMetamac> sources) {
        List<OrganisationSchemeMetamacBasicDto> target = new ArrayList<OrganisationSchemeMetamacBasicDto>(sources.size());
        for (OrganisationSchemeVersionMetamac source : sources) {
            target.add(organisationSchemeMetamacDoToBasicDto(source));
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
        target.setSpecialOrganisationHasBeenPublished(source.getSpecialOrganisationHasBeenPublished());
        return target;
    }

    @Override
    public OrganisationMetamacBasicDto organisationMetamacDoToBasicDto(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationMetamacBasicDto target = new OrganisationMetamacBasicDto();
        target.setType(source.getOrganisationType());
        target.setSpecialOrganisationHasBeenPublished(source.getSpecialOrganisationHasBeenPublished());
        itemDoToItemBasicDto(source, target);
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
    public ContactDto contactDoToDto(Contact source) {
        if (source == null) {
            return null;
        }
        ContactDto target = do2DtoMapperSdmxSrm.contactDoToDto(source);
        return target;
    }
}
