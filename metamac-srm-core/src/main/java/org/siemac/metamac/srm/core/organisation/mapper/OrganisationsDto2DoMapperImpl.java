package org.siemac.metamac.srm.core.organisation.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.exception.OrganisationMetamacNotFoundException;
import org.siemac.metamac.srm.core.organisation.exception.OrganisationSchemeVersionMetamacNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("organisationsDto2DoMapper")
public class OrganisationsDto2DoMapperImpl implements OrganisationsDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDto2DoMapper organisationDto2DoMapperSdmxSrm;

    @Autowired
    private OrganisationMetamacRepository                                                  organisationMetamacRepository;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository                                     organisationSchemeVersionMetamacRepository;

    protected OrganisationSchemeVersionMetamacRepository getOrganisationSchemeVersionMetamacRepository() {
        return organisationSchemeVersionMetamacRepository;
    }

    @Override
    public OrganisationSchemeVersionMetamac organisationSchemeMetamacDtoToDo(OrganisationSchemeMetamacDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        OrganisationSchemeVersionMetamac target = null;
        if (source.getId() == null) {
            target = new OrganisationSchemeVersionMetamac();
        } else {
            try {
                target = getOrganisationSchemeVersionMetamacRepository().findById(source.getId());
            } catch (OrganisationSchemeVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SEARCH_BY_ID_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.ORGANISATION_SCHEME, source.getId()).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Modifiable attributes
        // nothing

        // Sdmx metadata
        organisationDto2DoMapperSdmxSrm.organisationSchemeDtoToDo(source, target);

        return target;
    }

    @Override
    public OrganisationMetamac organisationMetamacDtoToDo(OrganisationMetamacDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        OrganisationMetamac target = null;
        if (source.getId() == null) {
            target = new OrganisationMetamac();
        } else {
            try {
                target = organisationMetamacRepository.findById(source.getId());
            } catch (OrganisationMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SEARCH_BY_ID_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.ORGANISATION, source.getId()).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Modifiable attributes
        // nothing

        // Sdmx metadata
        organisationDto2DoMapperSdmxSrm.organisationDtoToDo(source, target);

        return target;
    }

}