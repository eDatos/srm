package org.siemac.metamac.srm.core.organisation.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("organisationsDto2DoMapper")
public class OrganisationsDto2DoMapperImpl extends BaseDto2DoMapperImpl implements OrganisationsDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDto2DoMapper organisationDto2DoMapperSdmxSrm;

    @Autowired
    private OrganisationMetamacRepository                                                  organisationMetamacRepository;

    @Autowired
    private OrganisationSchemeVersionMetamacRepository                                     organisationSchemeVersionMetamacRepository;

    @Override
    public OrganisationSchemeVersionMetamac organisationSchemeMetamacDtoToDo(OrganisationSchemeMetamacDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        OrganisationSchemeVersionMetamac target = null;
        if (source.getUrn() == null) {
            target = new OrganisationSchemeVersionMetamac();
        } else {
            target = organisationSchemeVersionMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        target.setStreamMessageStatus(source.getStreamMessageStatus());

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
        if (source.getUrn() == null) {
            target = new OrganisationMetamac();
        } else {
            target = organisationMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        // nothing

        // Sdmx metadata
        organisationDto2DoMapperSdmxSrm.organisationDtoToDo(source, target);

        return target;
    }

}