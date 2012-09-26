package org.siemac.metamac.srm.core.organisation.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.exception.OrganisationSchemeVersionMetamacNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class OrganisationsDto2DoMapperImpl implements OrganisationsDto2DoMapper {

    @Autowired
    @Qualifier("organisationsDto2DoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDto2DoMapper organisationDto2DoMapperSdmxSrm;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository                                     organisationSchemeVersionMetamacRepository;

    protected OrganisationSchemeVersionMetamacRepository getOrganisationSchemeVersionMetamacRepository() {
        return organisationSchemeVersionMetamacRepository;
    }

    @Override
    public OrganisationSchemeVersionMetamac organisationSchemeDtoToDo(OrganisationSchemeMetamacDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        OrganisationSchemeVersionMetamac target = null;
        if (source.getId() == null) {
            target = new OrganisationSchemeVersionMetamac(source.getType());
        } else {
            try {
                target = getOrganisationSchemeVersionMetamacRepository().findById(source.getId());
            } catch (OrganisationSchemeVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // No modifiable attributes

        organisationDto2DoMapperSdmxSrm.organisationSchemeDtoToDo(source, target);

        return target;
    }

}
