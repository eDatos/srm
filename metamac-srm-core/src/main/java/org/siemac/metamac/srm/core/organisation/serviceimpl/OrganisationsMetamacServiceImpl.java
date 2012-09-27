package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceimpl.utils.OrganisationsMetamacInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;

/**
 * Implementation of OrganisationsMetamacService.
 */
@Service("organisationsMetamacService")
public class OrganisationsMetamacServiceImpl extends OrganisationsMetamacServiceImplBase {

    @Autowired
    private OrganisationsService        organisationsService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    // @Autowired
    // private OrganisationRepository organisationRepository;

    // @Autowired
    // private OrganisationSchemeLifecycle organisationSchemeLifecycle;

    public OrganisationsMetamacServiceImpl() {
    }

    @Override
    public OrganisationSchemeVersionMetamac createOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkCreateOrganisationScheme(organisationSchemeVersion, null);

        // Fill metadata
        organisationSchemeVersion.setLifecycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        organisationSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save organisationScheme
        return (OrganisationSchemeVersionMetamac) organisationsService.createOrganisationScheme(ctx, organisationSchemeVersion);
    }

    @Override
    public OrganisationSchemeVersionMetamac updateOrganisationScheme(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkUpdateOrganisationScheme(organisationSchemeVersion, null);
        // OrganisationsService checks organisationScheme is not final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

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

        // Type cast to OrganisationSchemeVersionMetamac
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionMetamacs = new ArrayList<OrganisationSchemeVersionMetamac>();
        for (OrganisationSchemeVersion organisationSchemeVersion : organisationSchemeVersions) {
            organisationSchemeVersionMetamacs.add((OrganisationSchemeVersionMetamac) organisationSchemeVersion);
        }

        return organisationSchemeVersionMetamacs;
    }

    @Override
    public PagedResult<OrganisationSchemeVersionMetamac> findOrganisationSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkFindOrganisationSchemesByCondition(conditions, pagingParameter, null);

        // Find (do not call SDMX module to avoid type cast)
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).build();
        }
        PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = getOrganisationSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return organisationSchemeVersionPagedResult;
    }

    // @Override
    // public OrganisationSchemeVersionMetamac sendOrganisationSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.sendToProductionValidation(ctx, urn);
    // }
    //
    // @Override
    // public OrganisationSchemeVersionMetamac sendOrganisationSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.sendToDiffusionValidation(ctx, urn);
    // }
    //
    // @Override
    // public OrganisationSchemeVersionMetamac rejectOrganisationSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.rejectProductionValidation(ctx, urn);
    // }
    //
    // @Override
    // public OrganisationSchemeVersionMetamac rejectOrganisationSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.rejectDiffusionValidation(ctx, urn);
    // }
    //
    // // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de organisationos a publicar
    // @Override
    // public OrganisationSchemeVersionMetamac publishInternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.publishInternally(ctx, urn);
    // }
    //
    // // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    // @Override
    // public OrganisationSchemeVersionMetamac publishExternallyOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationSchemeVersionMetamac) organisationSchemeLifecycle.publishExternally(ctx, urn);
    // }

    @Override
    public void deleteOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Note: OrganisationsService checks organisationScheme isn't final and other conditions
        organisationsService.deleteOrganisationScheme(ctx, urn);
    }

    // @SuppressWarnings({"rawtypes", "unchecked"})
    // @Override
    // public OrganisationSchemeVersionMetamac versioningOrganisationScheme(ServiceContext ctx, String urn, VersionTypeEnum versionType) throws MetamacException {
    //
    // // Validation
    // OrganisationsMetamacInvocationValidator.checkVersioningOrganisationScheme(urn, versionType, null);
    //
    // // Initialize new version, copying values of version selected
    // OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = getOrganisationSchemeVersionMetamacRepository().retrieveOrganisationSchemeVersionByProcStatus(urn,
    // new ProcStatusEnum[]{ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED});
    //
    // // Check not exists version not published
    // List<OrganisationSchemeVersionMetamac> versionsNotPublished = findOrganisationSchemeVersionsOfOrganisationSchemeInProcStatus(ctx, organisationSchemeVersionToCopy.getItemScheme(),
    // ProcStatusEnum.DRAFT,
    // ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION, ProcStatusEnum.VALIDATION_REJECTED);
    // if (versionsNotPublished.size() != 0) {
    // throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ORGANISATION_SCHEME_VERSIONING_NOT_SUPPORTED)
    // .withMessageParameters(versionsNotPublished.get(0).getMaintainableArtefact().getUrn()).build();
    // }
    //
    // // Copy values
    // OrganisationSchemeVersionMetamac organisationSchemeNewVersion = DoCopyUtils.copyOrganisationSchemeVersionMetamac(organisationSchemeVersionToCopy);
    // organisationSchemeNewVersion.setLifecycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
    // List organisations = DoCopyUtils.copyOrganisationsMetamac(organisationSchemeVersionToCopy);
    //
    // // Versioning
    // organisationSchemeNewVersion = (OrganisationSchemeVersionMetamac) organisationsService
    // .versioningOrganisationScheme(ctx, organisationSchemeVersionToCopy.getItemScheme(), organisationSchemeNewVersion, organisations, versionType);
    //
    // return organisationSchemeNewVersion;
    // }
    //
    // @Override
    // public OrganisationSchemeVersionMetamac cancelOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
    //
    // // Validation
    // OrganisationsMetamacInvocationValidator.checkCancelOrganisationSchemeValidity(urn, null);
    //
    // // Retrieve version in specific procStatus
    // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamacRepository().retrieveOrganisationSchemeVersionByProcStatus(urn,
    // new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});
    //
    // // Cancel validity
    // organisationSchemeVersion = (OrganisationSchemeVersionMetamac) organisationsService.endOrganisationSchemeValidity(ctx, urn);
    //
    // return organisationSchemeVersion;
    // }
    //
    // @Override
    // public OrganisationMetamac createOrganisation(ServiceContext ctx, String organisationSchemeUrn, OrganisationMetamac organisation) throws MetamacException {
    //
    // // Validation
    // OrganisationSchemeVersionMetamac organisationSchemeVersion = null;
    // if (organisationSchemeUrn != null) {
    // organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
    // }
    // OrganisationsMetamacInvocationValidator.checkCreateOrganisation(organisationSchemeVersion, organisation, null);
    // // OrganisationsService checks organisationScheme isn't final
    //
    // // Save organisation
    // return (OrganisationMetamac) organisationsService.createOrganisation(ctx, organisationSchemeUrn, organisation);
    // }
    //
    // @Override
    // public OrganisationMetamac updateOrganisation(ServiceContext ctx, OrganisationMetamac organisation) throws MetamacException {
    //
    // // Validation
    // OrganisationsMetamacInvocationValidator.checkUpdateOrganisation(organisation, null);
    // // OrganisationsService checks organisationScheme isn't final
    //
    // return (OrganisationMetamac) organisationsService.updateOrganisation(ctx, organisation);
    // }
    //
    // @Override
    // public OrganisationMetamac retrieveOrganisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
    // return (OrganisationMetamac) organisationsService.retrieveOrganisationByUrn(ctx, urn);
    // }
    //
    // @Override
    // public PagedResult<OrganisationMetamac> findOrganisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
    //
    // // Validation
    // OrganisationsMetamacInvocationValidator.checkFindOrganisationsByCondition(conditions, pagingParameter, null);
    //
    // // Find (do not call sdmx module to avoid typecast)
    // if (conditions == null) {
    // conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).build();
    // }
    // PagedResult<OrganisationMetamac> organisationPagedResult = getOrganisationMetamacRepository().findByCondition(conditions, pagingParameter);
    // return organisationPagedResult;
    // }
    //
    // // TODO Pendiente de confirmación de Alberto: se está lanzando excepción si hay organisationos relacionados
    // @Override
    // public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {
    //
    // Organisation organisation = retrieveOrganisationByUrn(ctx, urn);
    //
    // // Note: OrganisationsService checks organisationScheme isn't final
    // organisationsService.deleteOrganisation(ctx, urn);
    // }
    //
    // @Override
    // public List<OrganisationMetamac> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn) throws MetamacException {
    //
    // // Retrieve
    // List<Organisation> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrn(ctx, organisationSchemeUrn);
    //
    // // Typecast
    // List<OrganisationMetamac> organisationsMetamac = organisationsToOrganisationMetamac(organisations);
    // return organisationsMetamac;
    // }
    //
    // @Override
    // public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeByOrganisationUrn(ServiceContext ctx, String organisationUrn) throws MetamacException {
    // // Validation
    // OrganisationsMetamacInvocationValidator.checkRetrieveOrganisationSchemeByOrganisationUrn(organisationUrn, null);
    //
    // // Retrieve
    // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamacRepository().findByOrganisation(organisationUrn);
    // if (organisationSchemeVersion == null) {
    // throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ORGANISATION_NOT_FOUND).withMessageParameters(organisationUrn).build();
    // }
    // return organisationSchemeVersion;
    // }
    //
    // private List<OrganisationMetamac> organisationsToOrganisationMetamac(List<Organisation> items) {
    // List<OrganisationMetamac> organisations = new ArrayList<OrganisationMetamac>();
    // for (Item item : items) {
    // organisations.add((OrganisationMetamac) item);
    // }
    // return organisations;
    // }
    //
    // /**
    // * Finds versions of organisation scheme in specific procStatus
    // */
    // private List<OrganisationSchemeVersionMetamac> findOrganisationSchemeVersionsOfOrganisationSchemeInProcStatus(ServiceContext ctx, ItemScheme organisationScheme, ProcStatusEnum... procStatus)
    // throws MetamacException {
    //
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.itemScheme().id())
    // .eq(organisationScheme.getId()).withProperty(OrganisationSchemeVersionMetamacProperties.lifecycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.noLimits();
    // PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = getOrganisationSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
    // return organisationSchemeVersionPagedResult.getValues();
    // }
    //
    // /**
    // * Retrieves version of a organisation scheme, checking that can be modified
    // */
    // private OrganisationSchemeVersionMetamac retrieveOrganisationSchemeVersionCanBeModified(ServiceContext ctx, String urn) throws MetamacException {
    // return getOrganisationSchemeVersionMetamacRepository().retrieveOrganisationSchemeVersionByProcStatus(urn,
    // new ProcStatusEnum[]{ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION});
    // }
    //
    // private Boolean isOrganisationSchemeFirstVersion(ItemSchemeVersion itemSchemeVersion) {
    // return itemSchemeVersion.getMaintainableArtefact().getReplaceTo() == null;
    // }
}
