package org.siemac.metamac.srm.core.category.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;

/**
 * Implementation of CategoriesMetamacService.
 */
@Service("categoriesMetamacService")
public class CategoriesMetamacServiceImpl extends CategoriesMetamacServiceImplBase {

    @Autowired
    private CategoriesService              categoriesService;

    @Autowired
    private OrganisationsMetamacService    organisationsService;

    @Autowired
    @Qualifier("categorySchemeLifeCycle")
    private LifeCycle                      categorySchemeLifeCycle;

    @Autowired
    private SrmValidation                  srmValidation;

    @Autowired
    private SrmConfiguration               srmConfiguration;

    @Autowired
    @Qualifier("categoriesVersioningCallbackMetamac")
    private ItemSchemesCopyCallback        categoriesVersioningCallback;

    @Autowired
    @Qualifier("categoriesCopyCallbackMetamac")
    private ItemSchemesCopyCallback        categoriesCopyCallback;

    @Autowired
    private MaintainableArtefactRepository maintainableArtefactRepository;

    @Autowired
    private ItemSchemeVersionRepository    itemSchemeVersionRepository;

    @Autowired
    private CategorisationRepository       categorisationRepository;

    @Autowired
    private InternationalStringRepository  internationalStringRepository;

    public CategoriesMetamacServiceImpl() {
    }

    @Override
    public CategorySchemeVersionMetamac createCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        preCreateCategoryScheme(ctx, categorySchemeVersion);

        // Save categoryScheme
        return (CategorySchemeVersionMetamac) categoriesService.createCategoryScheme(ctx, categorySchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public CategorySchemeVersionMetamac preCreateCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategoryScheme(categorySchemeVersion, null);
        checkCategorySchemeToCreateOrUpdate(ctx, categorySchemeVersion);

        // Fill metadata
        categorySchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        categorySchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        categorySchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac updateCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryScheme(categorySchemeVersion, null);
        checkCategorySchemeToCreateOrUpdate(ctx, categorySchemeVersion);

        // Save categoryScheme
        return (CategorySchemeVersionMetamac) categoriesService.updateCategoryScheme(ctx, categorySchemeVersion);
    }

    @Override
    public CategorySchemeVersionMetamac retrieveCategorySchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
    }

    @Override
    public List<CategorySchemeVersionMetamac> retrieveCategorySchemeVersions(ServiceContext ctx, String urn) throws MetamacException {

        // Retrieve categorySchemeVersions
        List<CategorySchemeVersion> categorySchemeVersions = categoriesService.retrieveCategorySchemeVersions(ctx, urn);

        // Typecast to CategorySchemeVersionMetamac
        return categorySchemeVersionsToCategorySchemeVersionsMetamac(categorySchemeVersions);
    }

    @Override
    public PagedResult<CategorySchemeVersionMetamac> findCategorySchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<CategorySchemeVersion> categorySchemeVersionPagedResult = categoriesService.findCategorySchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCategorySchemeVersionToMetamac(categorySchemeVersionPagedResult);
    }

    @Override
    public CategorySchemeVersionMetamac sendCategorySchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac sendCategorySchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac rejectCategorySchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac rejectCategorySchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac publishInternallyCategoryScheme(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
    }

    @Override
    public CategorySchemeVersionMetamac publishExternallyCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        // Delete
        categoriesService.deleteCategoryScheme(ctx, urn);
    }

    @Override
    public TaskInfo copyCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return categoriesService.copyCategoryScheme(ctx, urnToCopy, maintainerUrn, versionPattern, categoriesCopyCallback);
    }

    @Override
    public TaskInfo versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, versionType, false);
    }

    @Override
    public TaskInfo createTemporalVersionCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionTemporal = retrieveCategorySchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        if (!VersionUtil.isTemporalVersion(categorySchemeVersionTemporal.getMaintainableArtefact().getVersionLogic())) {
            throw new RuntimeException("Error creating a new version from a temporal. The URN is not for a temporary artifact");
        }

        // Retrieve the original artifact
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        categorySchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(categorySchemeVersion.getMaintainableArtefact().getVersionLogic(), categorySchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        categorySchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        categorySchemeVersionTemporal = (CategorySchemeVersionMetamac) categoriesService.updateCategoryScheme(ctx, categorySchemeVersionTemporal);

        // Set null replacedBy in the original entity
        categorySchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(categorySchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public CategorySchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        if (!VersionUtil.isTemporalVersion(categorySchemeTemporalVersion.getMaintainableArtefact().getVersionLogic())) {
            throw new RuntimeException("Error creating a new version from a temporal. The URN is not for a temporary artifact");
        }
        SrmValidationUtils.checkArtefactProcStatus(categorySchemeTemporalVersion.getLifeCycleMetadata(), categorySchemeTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(categorySchemeTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItemSchemeVersionWithoutItems(categorySchemeVersion, categorySchemeTemporalVersion, internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        categorySchemeVersion.setLifeCycleMetadata(BaseReplaceFromTemporalMetamac.replaceProductionAndDifussionLifeCycleMetadataFromTemporal(categorySchemeVersion.getLifeCycleMetadata(),
                categorySchemeTemporalVersion.getLifeCycleMetadata()));

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(categorySchemeTemporalVersion.getItems());
        List<ItemResult> categoriesFoundEfficiently = getCategoryMetamacRepository().findCategoriesByCategorySchemeUnordered(categorySchemeTemporalVersion.getId(), ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> categoriesFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(categoriesFoundEfficiently);
        for (Item item : categorySchemeVersion.getItems()) {
            CategoryMetamac category = (CategoryMetamac) item;
            CategoryMetamac categoryTemp = (CategoryMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());
            ItemResult categoryTempItemResult = categoriesFoundEfficientlyByUrn.get(categoryTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(category, categoryTempItemResult, internationalStringRepository);

            // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from categoryTempItemResult
        }

        // Delete temporal version
        deleteCategoryScheme(ctx, categorySchemeTemporalVersion.getMaintainableArtefact().getUrn());

        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().retrieveCategorySchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.endCategorySchemeValidity(ctx, urn, null);

        return categorySchemeVersion;
    }

    @Override
    public CategoryMetamac createCategory(ServiceContext ctx, String categorySchemeUrn, CategoryMetamac category) throws MetamacException {

        // Validation
        preCreateCategory(ctx, categorySchemeUrn, category);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        // Save category
        return (CategoryMetamac) categoriesService.createCategory(ctx, categorySchemeUrn, category);
    }

    @Override
    public CategoryMetamac preCreateCategory(ServiceContext ctx, String categorySchemeUrn, CategoryMetamac category) throws MetamacException {
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategory(categorySchemeVersion, category, null);
        checkCategoryToCreateOrUpdate(ctx, categorySchemeVersion, category);

        return category;
    }

    @Override
    public CategoryMetamac updateCategory(ServiceContext ctx, CategoryMetamac category) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategory(category, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, category.getNameableArtefact().getUrn());
        checkCategoryToCreateOrUpdate(ctx, categorySchemeVersion, category);

        return (CategoryMetamac) categoriesService.updateCategory(ctx, category);
    }

    @Override
    public CategoryMetamac retrieveCategoryByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CategoryMetamac) categoriesService.retrieveCategoryByUrn(ctx, urn);
    }

    @Override
    public PagedResult<CategoryMetamac> findCategoriesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Category> categoriesPagedResult = categoriesService.findCategoriesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCategoryToMetamac(categoriesPagedResult);
    }

    @Override
    public void deleteCategory(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, urn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        // Delete
        categoriesService.deleteCategory(ctx, urn);
    }

    @Override
    public List<ItemVisualisationResult> retrieveCategoriesByCategorySchemeUrn(ServiceContext ctx, String categorySchemeUrn, String locale) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategoriesByCategorySchemeUrn(categorySchemeUrn, locale, null);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        return getCategoryMetamacRepository().findCategoriesByCategorySchemeUnorderedToVisualisation(categorySchemeVersion.getId(), locale);
    }

    @Override
    public CategorySchemeVersionMetamac retrieveCategorySchemeByCategoryUrn(ServiceContext ctx, String categoryUrn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategorySchemeByCategoryUrn(categoryUrn, null);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().findByCategory(categoryUrn);
        if (categorySchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(categoryUrn).build();
        }
        return categorySchemeVersion;
    }

    @Override
    public List<MetamacExceptionItem> checkCategorySchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale) {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        getCategorySchemeVersionMetamacRepository().checkCategorySchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItems);
        getCategoryMetamacRepository().checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItems);
        return exceptionItems;
    }

    @Override
    public Categorisation createCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {

        preCreateCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);

        // Create
        Categorisation categorisation = categoriesService.createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn, SrmConstants.VERSION_PATTERN_METAMAC);
        categorisation = postCreateCategorisation(ctx, artefactCategorisedUrn, categorisation);
        return categorisation;
    }

    @Override
    public void preCreateCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {
        // Validation
        CategoriesInvocationValidator.checkCreateCategorisation(categoryUrn, artefactCategorisedUrn, maintainerUrn, null);
        // Category, externally published
        checkCategoryExternallyPublished(ctx, categoryUrn);
        // Maintainer, externally published
        checkMaintainerExternallyPublished(ctx, maintainerUrn);
    }

    @Override
    public Categorisation postCreateCategorisation(ServiceContext ctx, String artefactCategorisedUrn, Categorisation categorisation) throws MetamacException {
        // Automatically publication
        // Note: we can find artefact as maintainable instead of identifiable because in Metamac all artefacts with categorisation inherit to maintainable
        MaintainableArtefact artefact = maintainableArtefactRepository.findByUrn(artefactCategorisedUrn);

        if (artefact.getFinalLogicClient()) {
            categorisation = markCategorisationAsFinal(ctx, categorisation.getMaintainableArtefact().getUrn());
        } else {
            categorisation.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
            categorisationRepository.save(categorisation);
        }
        if (artefact.getPublicLogic()) {
            categorisation = markCategorisationAsPublic(ctx, categorisation.getMaintainableArtefact().getUrn());
        }
        if (artefact.getValidFrom() != null) {
            categorisation = startCategorisationValidity(ctx, categorisation.getMaintainableArtefact().getUrn(), null);
        }
        return categorisation;
    }

    @Override
    public void deleteCategorisation(ServiceContext ctx, String urn) throws MetamacException {
        categoriesService.deleteCategorisation(ctx, urn);
    }

    @Override
    public Categorisation markCategorisationAsFinal(ServiceContext ctx, String urn) throws MetamacException {
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        categorisation.getMaintainableArtefact().setFinalLogicClient(Boolean.TRUE);
        return categoriesService.markCategorisationAsFinal(ctx, urn);
    }

    @Override
    public Categorisation markCategorisationAsPublic(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.markCategorisationAsPublic(ctx, urn);
    }

    @Override
    public Categorisation startCategorisationValidity(ServiceContext ctx, String urn, DateTime validFrom) throws MetamacException {
        return categoriesService.startCategorisationValidity(ctx, urn, validFrom);
    }

    @Override
    public Categorisation endCategorisationValidity(ServiceContext ctx, String urn, DateTime validTo) throws MetamacException {
        return categoriesService.endCategorisationValidity(ctx, urn, validTo);
    }

    @Override
    public Categorisation retrieveCategorisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.retrieveCategorisationByUrn(ctx, urn);
    }

    @Override
    public List<Categorisation> retrieveCategorisationsByArtefact(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.retrieveCategorisationsByArtefact(ctx, urn);
    }

    @Override
    public PagedResult<Categorisation> findCategorisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Categorisation> pagedResults = categoriesService.findCategorisationsByCondition(ctx, conditions, pagingParameter);
        return pagedResults;
    }

    /**
     * Typecast to Metamac type
     */
    private List<CategoryMetamac> categoriesToCategoryMetamac(List<Category> sources) {
        List<CategoryMetamac> targets = new ArrayList<CategoryMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((CategoryMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<CategorySchemeVersionMetamac> categorySchemeVersionsToCategorySchemeVersionsMetamac(List<CategorySchemeVersion> sources) {
        List<CategorySchemeVersionMetamac> targets = new ArrayList<CategorySchemeVersionMetamac>(sources.size());
        for (ItemSchemeVersion source : sources) {
            targets.add((CategorySchemeVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CategorySchemeVersionMetamac> pagedResultCategorySchemeVersionToMetamac(PagedResult<CategorySchemeVersion> source) {
        List<CategorySchemeVersionMetamac> categorySchemeVersionsMetamac = categorySchemeVersionsToCategorySchemeVersionsMetamac(source.getValues());
        return new PagedResult<CategorySchemeVersionMetamac>(categorySchemeVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CategoryMetamac> pagedResultCategoryToMetamac(PagedResult<Category> source) {
        List<CategoryMetamac> categoriesMetamac = categoriesToCategoryMetamac(source.getValues());
        return new PagedResult<CategoryMetamac>(categoriesMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    private void checkCategoryExternallyPublished(ServiceContext ctx, String categoryUrn) throws MetamacException {
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, categoryUrn);
        SrmValidationUtils.checkArtefactExternallyPublished(categorySchemeVersion.getMaintainableArtefact().getUrn(), categorySchemeVersion.getLifeCycleMetadata());
    }

    private void checkMaintainerExternallyPublished(ServiceContext ctx, String maintainerUrn) throws MetamacException {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(ctx, maintainerUrn);
        SrmValidationUtils.checkArtefactExternallyPublished(organisationSchemeVersion.getMaintainableArtefact().getUrn(), organisationSchemeVersion.getLifeCycleMetadata());
    }

    /**
     * Common validations to create or update a category scheme
     */
    private void checkCategorySchemeToCreateOrUpdate(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        if (categorySchemeVersion.getId() != null) {
            // Proc status
            checkCategorySchemeCanBeModified(categorySchemeVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(categorySchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getMaintainableArtefact().getIsImported());
    }

    /**
     * Common validations to create or update a category
     */
    private void checkCategoryToCreateOrUpdate(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion, CategoryMetamac category) throws MetamacException {
        checkCategorySchemeCanBeModified(categorySchemeVersion);
    }

    private TaskInfo createVersionOfCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType, boolean isTemporal) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkVersioningCategoryScheme(urnToCopy, versionType, isTemporal, null, null);
        checkCategorySchemeToVersioning(ctx, urnToCopy, isTemporal);
        // Versioning
        return categoriesService.versioningCategoryScheme(ctx, urnToCopy, versionType, isTemporal, categoriesVersioningCallback);
    }

    private void checkCategorySchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = retrieveCategorySchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(categorySchemeVersionToCopy.getMaintainableArtefact(), categorySchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        // Check does not exist any version 'no final'
        ItemSchemeVersion categorySchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(categorySchemeVersionToCopy.getItemScheme().getId());
        if (categorySchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(categorySchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkCategorySchemeCanBeModified(CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(categorySchemeVersion.getLifeCycleMetadata(), categorySchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(categorySchemeVersion);
    }

}