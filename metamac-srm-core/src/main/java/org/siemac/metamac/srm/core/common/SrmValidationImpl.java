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

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;

@Component("srmValidation")
public class SrmValidationImpl implements SrmValidation {

    @Autowired
    private OrganisationsMetamacService organisationsService;

    @Autowired
    private SrmConfiguration            srmConfiguration;

    @Override
    public void checkMaintainer(ServiceContext ctx, MaintainableArtefact maintainableArtefact, Boolean artefactImported) throws MetamacException {
        if (SdmxSrmUtils.isAgencySchemeSdmx(maintainableArtefact.getUrn())) {
            // AgencyScheme root has not maintainer
            return;
        }

        if (maintainableArtefact.getMaintainer() == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED).withMessageParameters(ServiceExceptionParameters.MAINTAINER).build();
        }
        String maintainerUrn = maintainableArtefact.getMaintainer().getNameableArtefact().getUrn();

        // Maintainer is internally or externally published
        OrganisationSchemeVersionMetamac maintainerOrganisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(ctx, maintainerUrn);
        SrmValidationUtils.checkArtefactInternallyOrExternallyPublished(maintainerOrganisationSchemeVersion.getMaintainableArtefact().getUrn(),
                maintainerOrganisationSchemeVersion.getLifeCycleMetadata());

        // If artefact is not imported, maintainer must be the default one
        if (!BooleanUtils.isTrue(artefactImported)) {
            String maintainerUrnDefault = srmConfiguration.retrieveMaintainerUrnDefault(); // can not be empty
            if (!maintainerUrnDefault.equals(maintainerUrn)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT).withMessageParameters(maintainerUrn, maintainerUrnDefault).build();
            }
        }
    }

    @Override
    public void checkItemsStructureCanBeModified(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
        checkItemsStructureCanBeModified(ctx, itemSchemeVersion.getMaintainableArtefact().getIsImported());
    }

    @Override
    public void checkItemsStructureCanBeModified(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException {
        checkItemsStructureCanBeModified(ctx, structureVersion.getMaintainableArtefact().getIsImported());
    }

    private void checkItemsStructureCanBeModified(ServiceContext ctx, Boolean artefactImported) throws MetamacException {
        if (BooleanUtils.isTrue(artefactImported)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED).build();
        }
    }
}
