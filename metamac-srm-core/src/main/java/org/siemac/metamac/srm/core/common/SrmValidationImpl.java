package org.siemac.metamac.srm.core.common;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

@Component("srmValidation")
public class SrmValidationImpl implements SrmValidation {

    @Autowired
    private OrganisationsMetamacService organisationsService;

    @Autowired
    private SrmConfiguration            srmConfiguration;

    @Override
    public void checkMaintainer(ServiceContext ctx, MaintainableArtefact maintainableArtefact, Boolean artefactImported) throws MetamacException {
        if (maintainableArtefact.getMaintainer() == null) {
            // maintainer is required. Note: AgencyScheme root has not maintainer, but it must be inserted and modified directly in database
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED).withMessageParameters(ServiceExceptionParameters.MAINTAINER).build();
        }
        String maintainerUrn = maintainableArtefact.getMaintainer().getNameableArtefact().getUrn();

        // Maintainer is internally or externally published
        OrganisationSchemeVersionMetamac maintainerOrganisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(ctx, maintainerUrn);
        SrmValidationUtils.checkArtefactInternallyOrExternallyPublished(maintainerOrganisationSchemeVersion.getMaintainableArtefact().getUrn(),
                maintainerOrganisationSchemeVersion.getLifeCycleMetadata());

        // If artefact is not imported, maintainer must be default
        if (!BooleanUtils.isTrue(artefactImported)) {
            String maintainerUrnDefault = srmConfiguration.retrieveMaintainerUrnDefault(); // can not be empty
            if (!maintainerUrnDefault.equals(maintainerUrn)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT).withMessageParameters(maintainerUrn, maintainerUrnDefault).build();
            }
        }
    }
}
