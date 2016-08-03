package org.siemac.metamac.srm.core.common;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
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
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

@Component("srmValidation")
public class SrmValidationImpl implements SrmValidation {

    @Autowired
    private OrganisationsMetamacService organisationsService;

    @Autowired
    private SrmConfiguration            srmConfiguration;

    @Override
    public boolean isMaintainerSdmxRoot(ServiceContext ctx, Organisation maintainer) throws MetamacException {
        return maintainer == null;
    }

    @Override
    public boolean isMaintainerIsDefault(ServiceContext ctx, Organisation maintainer) throws MetamacException {
        return isMaintainerIsDefault(ctx, maintainer.getNameableArtefact().getUrn());
    }

    @Override
    public void checkMaintainerIsDefault(ServiceContext ctx, String maintainerUrn) throws MetamacException {
        if (!isMaintainerIsDefault(ctx, maintainerUrn)) {
            String maintainerUrnDefault = srmConfiguration.retrieveMaintainerUrnDefault();
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT).withMessageParameters(maintainerUrn, maintainerUrnDefault).build();
        }
    }

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
            checkMaintainerIsDefault(ctx, maintainerUrn);
        }
    }

    @Override
    public void checkItemsStructureCanBeModified(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException {
        checkItemsStructureCanBeModified(ctx, itemSchemeVersion.getMaintainableArtefact());
    }

    @Override
    public void checkItemsStructureCanBeModified(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException {
        checkItemsStructureCanBeModified(ctx, structureVersion.getMaintainableArtefact());
    }

    @Override
    public void checkStartValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        SrmValidationUtils.checkArtefactInternallyPublished(maintainableArtefact.getUrn(), lifeCycle);
        checkMaintainerIsDefault(ctx, maintainableArtefact.getMaintainer().getNameableArtefact().getUrn());
    }

    @Override
    public void checkEndValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        SrmValidationUtils.checkArtefactExternallyPublished(maintainableArtefact.getUrn(), lifeCycle);
        checkMaintainerIsDefault(ctx, maintainableArtefact.getMaintainer().getNameableArtefact().getUrn());
    }

    @Override
    public void checkModifyCategorisationValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact) throws MetamacException {
        checkMaintainerIsDefault(ctx, maintainableArtefact.getMaintainer().getNameableArtefact().getUrn());
        // any proc status
    }

    private boolean isMaintainerIsDefault(ServiceContext ctx, String maintainerUrn) throws MetamacException {
        String maintainerUrnDefault = srmConfiguration.retrieveMaintainerUrnDefault();
        return maintainerUrnDefault.equals(maintainerUrn);
    }

    private void checkItemsStructureCanBeModified(ServiceContext ctx, MaintainableArtefact maintainableArtefact) throws MetamacException {
        SrmValidationUtils.checkArtefactIsNotTemporal(maintainableArtefact);
        if (BooleanUtils.isTrue(maintainableArtefact.getIsImported())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED).build();
        }
    }
}
