package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.serviceimpl.utils.OrganisationsMetamacInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsInvocationValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

/**
 * Implementation of OrganisationsMetamacService.
 */
@Service("organisationsMetamacService")
public class OrganisationsMetamacServiceImpl extends OrganisationsMetamacServiceImplBase {

    @Autowired
    private OrganisationsService          organisationsService;

    @Autowired
    private CategoriesMetamacService      categoriesMetamacService;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeVersionRepository;

    @Autowired
    @Qualifier("organisationSchemeLifeCycle")
    private LifeCycle                     organisationSchemeLifeCycle;

    @Autowired
    private SrmValidation                 srmValidation;

    @Autowired
    private SrmConfiguration              srmConfiguration;

    @Autowired
    private OrganisationRepository        organisationRepository;

    @Autowired
    private InternationalStringRepository internationalStringRepository;

    @Autowired
    @Qualifier("organisationsVersioningCallbackMetamac")
    private ItemSchemesCopyCallback       organisationsVersioningCallback;

    @Autowired
    @Qualifier("organisationsDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback       organisationsDummyVersioningCallback;

    @Autowired
    @Qualifier("organisationsCopyCallbackMetamac")
    private ItemSchemesCopyCallback       organisationsCopyCallback;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

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
        // Validation
        OrganisationsInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeVersionByUrn(urn);
        return organisationSchemeVersion;
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
    public OrganisationSchemeVersionMetamac publishInternallyOrganisationScheme(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationSchemeLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
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
    public TaskInfo copyOrganisationScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return organisationsService.copyOrganisationScheme(ctx, urnToCopy, maintainerUrn, versionPattern, organisationsCopyCallback);
    }

    @Override
    public TaskInfo versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfOrganisationScheme(ctx, urnToCopy, organisationsVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalOrganisationScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfOrganisationScheme(ctx, urnToCopy, organisationsDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        OrganisationSchemeVersionMetamac organisationSchemeVersionTemporal = retrieveOrganisationSchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(organisationSchemeVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        organisationSchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(organisationSchemeVersion.getMaintainableArtefact().getVersionLogic(), organisationSchemeVersion.getItemScheme().getVersionPattern(),
                        versionTypeEnum));

        organisationSchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        organisationSchemeVersionTemporal.setIsTypeUpdated(Boolean.FALSE);
        organisationSchemeVersionTemporal = (OrganisationSchemeVersionMetamac) organisationsService.updateOrganisationScheme(ctx, organisationSchemeVersionTemporal);

        // Set null replacedBy in the original entity
        organisationSchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        categoriesMetamacService.createVersionFromTemporalCategorisations(ctx, organisationSchemeVersionTemporal.getMaintainableArtefact());

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(organisationSchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public OrganisationSchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(organisationSchemeTemporalVersion.getMaintainableArtefact());
        SrmValidationUtils.checkArtefactProcStatus(organisationSchemeTemporalVersion.getLifeCycleMetadata(), organisationSchemeTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx,
                GeneratorUrnUtils.makeUrnFromTemporal(organisationSchemeTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItemSchemeVersionWithoutItems(organisationSchemeVersion, organisationSchemeTemporalVersion,
                internationalStringRepository);

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(organisationSchemeTemporalVersion.getItems());
        List<ItemResult> organisationsFoundEfficiently = getOrganisationMetamacRepository().findOrganisationsByOrganisationSchemeUnordered(organisationSchemeTemporalVersion.getId(),
                ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> organisationsFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(organisationsFoundEfficiently);
        for (Item item : organisationSchemeVersion.getItems()) {

            OrganisationMetamac organisation = (OrganisationMetamac) item;
            OrganisationMetamac organisationTemp = (OrganisationMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());
            ItemResult organisationTempItemResult = organisationsFoundEfficientlyByUrn.get(organisationTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(organisation, organisationTempItemResult, internationalStringRepository);

            // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from organisationTempItemResult

        }

        if (SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(organisationSchemeVersion)) {
            // Add Contact, always remove and add all contacts
            for (Item item : organisationSchemeVersion.getItems()) {
                OrganisationMetamac organisation = (OrganisationMetamac) item;
                OrganisationMetamac organisationTemp = (OrganisationMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());

                organisation.removeAllContacts();

                for (Contact contact : organisationTemp.getContacts()) {
                    contact.setOrganisation(organisation);
                    organisation.addContact(contact);
                }
            }

            // Add Organisations
            Map<String, Item> currentItemMap = SrmServiceUtils.createMapOfItemsByUrn(organisationSchemeVersion.getItems());
            for (Organisation itemTemp : new ArrayList<Organisation>(organisationSchemeTemporalVersion.getItems())) {
                String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(itemTemp.getNameableArtefact().getUrn());
                if (!currentItemMap.containsKey(urnFromTemporal)) {
                    // Add
                    organisationSchemeVersion.addItem(itemTemp);
                    organisationSchemeVersion.addItemsFirstLevel(itemTemp);

                    itemTemp.getNameableArtefact().setUrn(urnFromTemporal);
                    itemTemp.getNameableArtefact().setUrnProvider(GeneratorUrnUtils.makeUrnFromTemporal(itemTemp.getNameableArtefact().getUrnProvider()));
                }
            }

            // Add Categorisations
            CategorisationsUtils.addCategorisationsTemporalToItemScheme(organisationSchemeTemporalVersion, organisationSchemeVersion);

            // ===============================================================
            // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear
            // the context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
            entityManager.flush();
            entityManager.clear();
            // ===============================================================

        } else {
            // Add items is not supported in temporal version for another types.

            // Add Categorisations
            boolean thereAreNewCategorisations = false;
            thereAreNewCategorisations = CategorisationsUtils.addCategorisationsTemporalToItemScheme(organisationSchemeTemporalVersion, organisationSchemeVersion);
            if (thereAreNewCategorisations) {
                // ===============================================================
                // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear
                // the
                // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
                entityManager.flush();
                entityManager.clear();
                // ===============================================================
            }
        }

        // Delete temporal version
        deleteOrganisationScheme(ctx, organisationSchemeTemporalVersion.getMaintainableArtefact().getUrn());

        organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn());
        return organisationSchemeVersion;
    }

    @Override
    public OrganisationSchemeVersionMetamac startOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkStartValidity(urn, null);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, urn);
        srmValidation.checkStartValidity(ctx, organisationSchemeVersion.getMaintainableArtefact(), organisationSchemeVersion.getLifeCycleMetadata());

        // Start validity
        organisationSchemeVersion = (OrganisationSchemeVersionMetamac) organisationsService.startOrganisationSchemeValidity(ctx, urn, null);
        return organisationSchemeVersion;
    }

    @Override
    public OrganisationSchemeVersionMetamac endOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, urn);
        srmValidation.checkEndValidity(ctx, organisationSchemeVersion.getMaintainableArtefact(), organisationSchemeVersion.getLifeCycleMetadata());

        // End validity
        organisationSchemeVersion = (OrganisationSchemeVersionMetamac) organisationsService.endOrganisationSchemeValidity(ctx, urn, null);
        return organisationSchemeVersion;
    }

    @Override
    public OrganisationMetamac createOrganisation(ServiceContext ctx, String organisationSchemeUrn, OrganisationMetamac organisation) throws MetamacException {

        // Validation
        preCreateOrganisation(ctx, organisationSchemeUrn, organisation);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        checkOrganisationsStructureCanBeModified(ctx, organisationSchemeVersion, organisation, false);

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

        // Check code can be changed
        if (organisation.getNameableArtefact().getIsCodeUpdated()) {
            if (BooleanUtils.isTrue(organisation.getSpecialOrganisationHasBeenPublished())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ORGANISATION_UPDATE_CODE_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED)
                        .withMessageParameters(organisation.getNameableArtefact().getUrn()).build();
            }
        }
        checkOrganisationToCreateOrUpdate(ctx, organisationSchemeVersion, organisation);

        // Update
        return (OrganisationMetamac) organisationsService.updateOrganisation(ctx, organisation);
    }

    @Override
    public OrganisationMetamac retrieveOrganisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        OrganisationsInvocationValidator.checkRetrieveByUrn(urn);
        OrganisationMetamac organisation = getOrganisationMetamacRepository().findByUrn(urn);
        if (organisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return organisation;
    }

    @Override
    public PagedResult<OrganisationMetamac> findOrganisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Organisation> organisationsPagedResult = organisationsService.findOrganisationsByCondition(ctx, conditions, pagingParameter);
        return pagedResultOrganisationToMetamac(organisationsPagedResult);
    }

    @Override
    public PagedResult<Contact> findOrganisationContactsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return organisationsService.findOrganisationContactsByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public OrganisationMetamac retrieveMaintainerDefault(ServiceContext ctx) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        return retrieveOrganisationByUrn(ctx, maintainerUrn);
    }

    @Override
    public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        OrganisationMetamac organisation = retrieveOrganisationByUrn(ctx, urn);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByOrganisationUrn(ctx, urn);
        checkOrganisationsStructureCanBeModified(ctx, organisationSchemeVersion, organisation, true);

        // Delete
        organisationsService.deleteOrganisation(ctx, urn);
    }

    @Override
    public List<OrganisationMetamacVisualisationResult> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn, String locale) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkRetrieveOrganisationsByOrganisationSchemeUrn(organisationSchemeUrn, locale, null);

        // Retrieve
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        return getOrganisationMetamacRepository().findOrganisationsByOrganisationSchemeUnorderedToVisualisation(organisationSchemeVersion.getId(), locale);
    }

    @Override
    public List<ItemResult> retrieveOrganisationsByOrganisationSchemeUrnUnordered(ServiceContext ctx, String organisationSchemeUrn, ItemResultSelection itemResultSelection) throws MetamacException {

        // Validation
        OrganisationsMetamacInvocationValidator.checkRetrieveOrganisationsByOrganisationSchemeUrnUnordered(organisationSchemeUrn, itemResultSelection, null);

        if (itemResultSelection == null) {
            itemResultSelection = ItemResultSelection.RETRIEVE; // default
        }
        OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        return organisationRepository.findOrganisationsByOrganisationSchemeUnordered(organisationSchemeVersion.getId(), itemResultSelection);
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

    @Override
    public void checkOrganisationSchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn)
            throws MetamacException {
        getOrganisationSchemeVersionMetamacRepository().checkOrganisationSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getOrganisationMetamacRepository().checkOrganisationTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void checkOrganisationSchemeWithRelatedResourcesExternallyPublished(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        String itemSchemeVersionUrn = organisationSchemeVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        categoriesMetamacService.checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    /**
     * Typecast to Metamac type
     */
    private List<OrganisationMetamac> organisationsToOrganisationMetamac(List<Organisation> sources) {
        List<OrganisationMetamac> targets = new ArrayList<OrganisationMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((OrganisationMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<OrganisationSchemeVersionMetamac> organisationSchemeVersionsToOrganisationSchemeVersionsMetamac(List<OrganisationSchemeVersion> sources) {
        List<OrganisationSchemeVersionMetamac> targets = new ArrayList<OrganisationSchemeVersionMetamac>(sources.size());
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

    private void checkOrganisationSchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {
        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = retrieveOrganisationSchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(organisationSchemeVersionToCopy.getMaintainableArtefact(), organisationSchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        // Check does not exist any version 'no final'
        ItemSchemeVersion organisationSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(organisationSchemeVersionToCopy.getItemScheme().getId());
        if (organisationSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(organisationSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkOrganisationSchemeCanBeModified(OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(organisationSchemeVersion.getLifeCycleMetadata(), organisationSchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(organisationSchemeVersion);
    }

    private TaskInfo createVersionOfOrganisationScheme(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, VersionTypeEnum versionType, boolean isTemporal)
            throws MetamacException {
        // Validation
        OrganisationsMetamacInvocationValidator.checkVersioningOrganisationScheme(urnToCopy, versionType, isTemporal, null, null);
        checkOrganisationSchemeToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        return organisationsService.versioningOrganisationScheme(ctx, urnToCopy, versionType, isTemporal, itemSchemesCopyCallback);
    }

    /**
     * Validations to can modify structure of organisations is different than other items type.
     */
    private void checkOrganisationsStructureCanBeModified(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion, OrganisationMetamac organisation, boolean deleting)
            throws MetamacException {

        checkOrganisationSchemeCanBeModified(organisationSchemeVersion);

        if (BooleanUtils.isTrue(organisationSchemeVersion.getMaintainableArtefact().getIsImported())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED).build();
        }

        if (deleting && BooleanUtils.isTrue(organisation.getSpecialOrganisationHasBeenPublished())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ORGANISATION_DELETING_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED)
                    .withMessageParameters(organisation.getNameableArtefact().getUrn()).build();
        }

        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeVersion.getOrganisationSchemeType())) {
            // add, delete... organisations are unsupported in temporal versions. Must create version from temporal to allow these actions
            SrmValidationUtils.checkArtefactIsNotTemporal(organisationSchemeVersion.getMaintainableArtefact());
        } else if (SdmxSrmValidationUtils.isOrganisationSchemeWithSpecialTreatment(organisationSchemeVersion)) {
            // allowed when temporal and not temporal and maintainer is default or root (really, this condition is similar to be not imported)
            if (!srmValidation.isMaintainerSdmxRoot(ctx, organisationSchemeVersion.getMaintainableArtefact().getMaintainer())
                    && !srmValidation.isMaintainerIsDefault(ctx, organisationSchemeVersion.getMaintainableArtefact().getMaintainer())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_MAINTAINER_IS_NOT_DEFAULT_NOR_SDMX).build();
            }
        } else {
            throw new IllegalArgumentException("Unexpected organisation type: " + organisationSchemeVersion.getOrganisationSchemeType());
        }
    }

    private OrganisationSchemeVersionMetamac retrieveOrganisationSchemeVersionByUrn(String urn) throws MetamacException {
        OrganisationsInvocationValidator.checkRetrieveByUrn(urn);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationSchemeVersionMetamacRepository().findByUrn(urn);
        if (organisationSchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return organisationSchemeVersion;
    }

}