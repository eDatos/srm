package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceimpl.utils.OrganisationsMetamacInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsDoCopyUtils.OrganisationCopyCallback;

/**
 * Implementation of OrganisationsMetamacService.
 */
@Service("organisationsMetamacService")
public class OrganisationsMetamacServiceImpl extends OrganisationsMetamacServiceImplBase {

    @Autowired
    private OrganisationsService        organisationsService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    @Qualifier("organisationSchemeLifeCycle")
    private LifeCycle                   organisationSchemeLifeCycle;

    @Autowired
    private SrmValidation               srmValidation;

    @Autowired
    private SrmConfiguration            srmConfiguration;

    @Autowired
    @Qualifier("organisationCopyCallbackMetamac")
    private OrganisationCopyCallback    organisationCopyCallback;

    public OrganisationsMetamacServiceImpl() {
    }

    @Override
    public OrganisationSchemeVersionMetamac createOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        // Fill and validate OrganisationScheme
        preCreateOrganisationScheme(ctx, organisationSchemeVersion);

        // Save organisationScheme
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;

        return (OrganisationSchemeVersionMetamac) organisationsService.createOrganisationScheme(ctx, organisationSchemeVersion, versionPattern);
    }

    @Override
    public OrganisationSchemeVersionMetamac preCreateOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkCreateOrganisationScheme(organisationSchemeVersion, null);
        checkOrganisationSchemeToCreateOrUpdate(ctx, organisationSchemeVersion);

        // Fill metadata
        organisationSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        organisationSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        organisationSchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return organisationSchemeVersion;
    }

    @Override
    public OrganisationSchemeVersionMetamac updateOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkUpdateOrganisationScheme(organisationSchemeVersion, null);
        checkOrganisationSchemeToCreateOrUpdate(ctx, organisationSchemeVersion);

        // Save organisationScheme
        return (OrganisationSchemeVersionMetamac) organisationsService.updateOrganisationScheme(ctx, organisationSchemeVersion);
    }

    @Override
    public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
    }

    @Override
    public List<OrganisationSchemeVersionMetamac> retrieveOrganisationSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {

        // Retrieve organisationSchemeVersions
        List<OrganisationSchemeVersion> organisationSchemeVersions = organisationsService.retrieveOrganisationSchemeVersions(ctx, urn);

        // Typecast to OrganisationSchemeVersionMetamac
        return organisationSchemeVersionsToOrganisationSchemeVersionsMetamac(organisationSchemeVersions);
    }

    @Override
    public PagedResult<OrganisationSchemeVersionMetamac> findOrganisationSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        PagedResult<OrganisationSchemeVersion> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultOrganisationSchemeVersionToMetamac(organisationSchemeVersionPagedResult);
    }

    @Override
    public OrganisationSchemeVersionMetamac sendOrganisationSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac sendOrganisationSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac rejectOrganisationSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac rejectOrganisationSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac publishInternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.publishInternally(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac publishExternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, urn);
        checkOrganisationSchemeCanBeModified(organisationSchemeVersion);

        // Delete
        organisationsService.deleteOrganisationScheme(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkVersioningOrganisationScheme(urnToCopy, versionType, null, null);
        checkOrganisationSchemeToVersioning(ctx, urnToCopy);

        // Versioning
        OrganisationSchemeVersionMetamac organisationSchemeNewVersion = (OrganisationSchemeVersionMetamac) organisationsService.versioningOrganisationScheme(ctx, urnToCopy, versionType,
                organisationCopyCallback);

        return organisationSchemeNewVersion;
    }

    @Override
    public OrganisationSchemeVersionMetamac endOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamacRepository().retrieveOrganisationSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        organisationSchemeVersion = (OrganisationSchemeVersionMetamac) organisationsService.endOrganisationSchemeValidity(ctx, urn, null);

        return organisationSchemeVersion;
    }

    @Override
    public OrganisationMetamac createOrganisation(ServiceContext ctx, String organisationSchemeUrn, OrganisationMetamac organisation) throws MetamacException {

        preCreateOrganisation(ctx, organisationSchemeUrn, organisation);

        // Save organisation
        return (OrganisationMetamac) organisationsService.createOrganisation(ctx, organisationSchemeUrn, organisation);
    }

    @Override
    public OrganisationMetamac preCreateOrganisation(ServiceContext ctx, String organisationSchemeUrn, OrganisationMetamac organisation) throws MetamacException {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);

        // Validation
        OrganisationsMetamacInvocationValidator.checkCreateOrganisation(organisationSchemeVersion, organisation, null);
        checkOrganisationToCreateOrUpdate(ctx, organisationSchemeVersion, organisation);

        return organisation;
    }

    @Override
    public OrganisationMetamac updateOrganisation(ServiceContext ctx, OrganisationMetamac organisation) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkUpdateOrganisation(organisation, null);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByOrganisationUrn(ctx, organisation.getNameableArtefact().getUrn());
        checkOrganisationToCreateOrUpdate(ctx, organisationSchemeVersion, organisation);

        // Update
        return (OrganisationMetamac) organisationsService.updateOrganisation(ctx, organisation);
    }

    @Override
    public OrganisationMetamac retrieveOrganisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationMetamac) organisationsService.retrieveOrganisationByUrn(ctx, urn);
    }

    @Override
    public PagedResult<OrganisationMetamac> findOrganisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Organisation> organisationsPagedResult = organisationsService.findOrganisationsByCondition(ctx, conditions, pagingParameter);
        return pagedResultOrganisationToMetamac(organisationsPagedResult);
    }

    @Override
    public OrganisationMetamac retrieveMaintainerDefault(ServiceContext ctx) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        return retrieveOrganisationByUrn(ctx, maintainerUrn);
    }

    @Override
    public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByOrganisationUrn(ctx, urn);
        checkOrganisationSchemeCanBeModified(organisationSchemeVersion);

        // Delete
        organisationsService.deleteOrganisation(ctx, urn);
    }

    @Override
    public List<OrganisationMetamac> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn) throws MetamacException {

        // Retrieve
        List<Organisation> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrn(ctx, organisationSchemeUrn);

        // Typecast
        List<OrganisationMetamac> organisationsMetamac = organisationsToOrganisationMetamac(organisations);
        return organisationsMetamac;
    }

    @Override
    public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeByOrganisationUrn(ServiceContext ctx, String organisationUrn) throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkRetrieveOrganisationSchemeByOrganisationUrn(organisationUrn, null);

        // Retrieve
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamacRepository().findByOrganisation(organisationUrn);
        if (organisationSchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(organisationUrn).build();
        }
        return organisationSchemeVersion;
    }

    /**
     * Typecast to Metamac type
     */
    private List<OrganisationMetamac> organisationsToOrganisationMetamac(List<Organisation> sources) {
        List<OrganisationMetamac> targets = new ArrayList<OrganisationMetamac>();
        for (Item source : sources) {
            targets.add((OrganisationMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<OrganisationSchemeVersionMetamac> organisationSchemeVersionsToOrganisationSchemeVersionsMetamac(List<OrganisationSchemeVersion> sources) {
        List<OrganisationSchemeVersionMetamac> targets = new ArrayList<OrganisationSchemeVersionMetamac>();
        for (ItemSchemeVersion source : sources) {
            targets.add((OrganisationSchemeVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<OrganisationSchemeVersionMetamac> pagedResultOrganisationSchemeVersionToMetamac(PagedResult<OrganisationSchemeVersion> source) {
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionsMetamac = organisationSchemeVersionsToOrganisationSchemeVersionsMetamac(source.getValues());
        return new PagedResult<OrganisationSchemeVersionMetamac>(organisationSchemeVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<OrganisationMetamac> pagedResultOrganisationToMetamac(PagedResult<Organisation> source) {
        List<OrganisationMetamac> organisationsMetamac = organisationsToOrganisationMetamac(source.getValues());
        return new PagedResult<OrganisationMetamac>(organisationsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    /**
     * Common validations to create or update a organisation scheme
     */
    private void checkOrganisationSchemeToCreateOrUpdate(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {

        if (organisationSchemeVersion.getId() != null) {
            // Proc status
            checkOrganisationSchemeCanBeModified(organisationSchemeVersion);
            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(organisationSchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, organisationSchemeVersion.getMaintainableArtefact(), organisationSchemeVersion.getMaintainableArtefact().getIsImported());

        // Check type modification: type can only be modified when is in initial version and when has no children (this last requisite is checked in SDMX service)
        if (!SrmServiceUtils.isItemSchemeFirstVersion(organisationSchemeVersion)) {
            OrganisationSchemeVersionMetamac previousVersion = (OrganisationSchemeVersionMetamac) itemSchemeVersionRepository.retrieveByVersion(organisationSchemeVersion.getItemScheme().getId(),
                    organisationSchemeVersion.getMaintainableArtefact().getReplaceToVersion());
            if (!ObjectUtils.equals(previousVersion.getOrganisationSchemeType(), organisationSchemeVersion.getOrganisationSchemeType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.ORGANISATION_SCHEME_TYPE)
                        .build();
            }
        }
    }

    /**
     * Common validations to create or update a organisation
     */
    private void checkOrganisationToCreateOrUpdate(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion, OrganisationMetamac organisation) throws MetamacException {
        checkOrganisationSchemeCanBeModified(organisationSchemeVersion);
    }

    private void checkOrganisationSchemeToVersioning(ServiceContext ctx, String urnToCopy) throws MetamacException {
        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = retrieveOrganisationSchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(organisationSchemeVersionToCopy.getLifeCycleMetadata(), urnToCopy);
        // Check does not exist any version 'no final'
        ItemSchemeVersion organisationSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(organisationSchemeVersionToCopy.getItemScheme().getId());
        if (organisationSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(organisationSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkOrganisationSchemeCanBeModified(OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(organisationSchemeVersion)) {
            SrmValidationUtils.checkArtefactCanBeModified(organisationSchemeVersion.getLifeCycleMetadata(), organisationSchemeVersion.getMaintainableArtefact().getUrn());
        }
    }
}