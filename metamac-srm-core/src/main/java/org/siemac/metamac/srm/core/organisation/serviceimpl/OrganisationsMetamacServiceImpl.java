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
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
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
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsDoCopyUtils.OrganisationCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

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
    @Qualifier("organisationCopyCallbackMetamac")
    private OrganisationCopyCallback    organisationCopyCallback;

    public OrganisationsMetamacServiceImpl() {
    }

    @Override
    public OrganisationSchemeVersionMetamac createOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkCreateOrganisationScheme(organisationSchemeVersion, null);

        // Fill metadata
        organisationSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        organisationSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save organisationScheme. If it is an AgencyScheme, call another method to avoid being marked as final in creation
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeVersion.getOrganisationSchemeType())) {
            return (OrganisationSchemeVersionMetamac) organisationsService.createAgencySchemeNotFinal(ctx, organisationSchemeVersion, versionPattern);
        } else {
            return (OrganisationSchemeVersionMetamac) organisationsService.createOrganisationScheme(ctx, organisationSchemeVersion, versionPattern);
        }
    }

    @Override
    public OrganisationSchemeVersionMetamac updateOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkUpdateOrganisationScheme(organisationSchemeVersion, null);
        SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(organisationSchemeVersion.getMaintainableArtefact());
        // OrganisationsService checks organisationScheme is not final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

        // Check type modification: type can only be modified when is in initial version and when has no children (this last requisite is checked in SDMX service)
        if (!SdmxVersionUtils.isInitialVersion(organisationSchemeVersion.getMaintainableArtefact().getVersionLogic())) {
            OrganisationSchemeVersionMetamac previousVersion = (OrganisationSchemeVersionMetamac) itemSchemeVersionRepository.findByVersion(organisationSchemeVersion.getItemScheme().getId(),
                    organisationSchemeVersion.getMaintainableArtefact().getReplaceToVersion());
            if (!ObjectUtils.equals(previousVersion.getOrganisationSchemeType(), organisationSchemeVersion.getOrganisationSchemeType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.ORGANISATION_SCHEME_TYPE)
                        .build();
            }
        }

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

    // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de organisationos a publicar
    @Override
    public OrganisationSchemeVersionMetamac publishInternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public OrganisationSchemeVersionMetamac publishExternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Note: OrganisationsService checks organisationScheme isn't final and other conditions
        organisationsService.deleteOrganisationScheme(ctx, urn);
    }

    @Override
    public OrganisationSchemeVersionMetamac versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkVersioningOrganisationScheme(urnToCopy, versionType, null, null);
        checkVersioningOrganisationSchemeIsSupported(ctx, urnToCopy);

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

        // Validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = null;
        if (organisationSchemeUrn != null) {
            organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        }
        OrganisationsMetamacInvocationValidator.checkCreateOrganisation(organisationSchemeVersion, organisation, null);
        // OrganisationsService checks organisationScheme isn't final

        // Save organisation
        return (OrganisationMetamac) organisationsService.createOrganisation(ctx, organisationSchemeUrn, organisation);
    }

    @Override
    public OrganisationMetamac updateOrganisation(ServiceContext ctx, OrganisationMetamac organisation) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkUpdateOrganisation(organisation, null);
        // OrganisationsService checks organisationScheme isn't final

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
    public PagedResult<OrganisationMetamac> findOrganisationsAsMaintainerByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Organisation> organisationsPagedResult = organisationsService.findOrganisationsAsMaintainerByCondition(ctx, conditions, pagingParameter);
        return pagedResultOrganisationToMetamac(organisationsPagedResult);
    }

    @Override
    public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {

        // Note: OrganisationsService checks organisationScheme isn't final
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

    private void checkVersioningOrganisationSchemeIsSupported(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Retrieve version to copy and check it is final (internally published)
        OrganisationSchemeVersion organisationSchemeVersionToCopy = retrieveOrganisationSchemeByUrn(ctx, urnToCopy);
        if (!organisationSchemeVersionToCopy.getMaintainableArtefact().getFinalLogic()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn()).build();
        }
        // Check does not exist any version 'no final'
        ItemSchemeVersion organisationSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(organisationSchemeVersionToCopy.getItemScheme().getId());
        if (organisationSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(organisationSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }
}