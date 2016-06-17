package org.siemac.metamac.srm.core.category.serviceimpl;

import static org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils.addExceptionToExceptionItemsByResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.code.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvImportationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoriesTsvHeader;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

/**
 * Implementation of CategoriesMetamacService.
 */
@Service("categoriesMetamacService")
public class CategoriesMetamacServiceImpl extends CategoriesMetamacServiceImplBase {

    private static Logger                  logger = LoggerFactory.getLogger(CategoriesMetamacServiceImpl.class);

    @Autowired
    private BaseService                    baseService;

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
    @Qualifier("categoriesDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback        categoriesDummyVersioningCallback;

    @Autowired
    @Qualifier("categoriesCopyCallbackMetamac")
    private ItemSchemesCopyCallback        categoriesCopyCallback;

    @Autowired
    private TasksMetamacService            tasksMetamacService;

    @Autowired
    private MaintainableArtefactRepository maintainableArtefactRepository;

    @Autowired
    private ItemSchemeVersionRepository    itemSchemeVersionRepository;

    @Autowired
    private CategoryRepository             categoryRepository;

    @Autowired
    private CategorisationRepository       categorisationRepository;

    @Autowired
    private InternationalStringRepository  internationalStringRepository;

    @Autowired
    private ItemSchemeRepository           itemSchemeRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                entityManager;

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
        // Validation
        CategoriesInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeVersionByUrn(urn);
        return categorySchemeVersion;
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
    public PagedResult<CategorySchemeVersionMetamac> findCategorySchemesWithCategoriesCanBeCategorisationCategoryByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).distinctRoot().build();
        }
        // Category scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                .withProperty(CategorySchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return findCategorySchemesByCondition(ctx, conditions, pagingParameter);
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
    public TaskInfo copyCategoryScheme(ServiceContext ctx, String urnToCopy, String newCode) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return categoriesService.copyCategoryScheme(ctx, urnToCopy, newCode, maintainerUrn, versionPattern, categoriesCopyCallback);
    }

    @Override
    public TaskInfo versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, categoriesVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalVersionCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, categoriesDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionTemporal = retrieveCategorySchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(categorySchemeVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        categorySchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(categorySchemeVersion.getMaintainableArtefact().getVersionLogic(), categorySchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        categorySchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        categorySchemeVersionTemporal = (CategorySchemeVersionMetamac) categoriesService.updateCategoryScheme(ctx, categorySchemeVersionTemporal);

        // Set null replacedBy in the original entity
        categorySchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        createVersionFromTemporalCategorisations(ctx, categorySchemeVersionTemporal.getMaintainableArtefact());

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(categorySchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public CategorySchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(categorySchemeTemporalVersion.getMaintainableArtefact());
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

        // Merge Categorizations
        Map<String, Categorisation> temporalCategorisationMap = SrmServiceUtils.createMapOfCategorisationsByOriginalUrn(categorySchemeTemporalVersion.getMaintainableArtefact().getCategorisations());
        for (Categorisation categorisation : categorySchemeVersion.getMaintainableArtefact().getCategorisations()) {
            Categorisation categorisationTemp = temporalCategorisationMap.get(categorisation.getMaintainableArtefact().getUrn());
            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCategorisation(categorisation, categorisationTemp, internationalStringRepository);

            // Valid To
            if (!categorisation.getMaintainableArtefact().getIsImported()) {
                categorisation.getMaintainableArtefact().setValidTo(categorisationTemp.getMaintainableArtefact().getValidTo());
            }
        }

        // Add Categorisations
        boolean thereAreNewCategorisations = false;
        thereAreNewCategorisations = CategorisationsUtils.addCategorisationsTemporalToItemScheme(categorySchemeTemporalVersion, categorySchemeVersion);
        if (thereAreNewCategorisations) {
            // ===============================================================
            // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear the
            // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
            entityManager.flush();
            entityManager.clear();
            // ===============================================================
        }

        // Delete temporal version
        deleteCategoryScheme(ctx, categorySchemeTemporalVersion.getMaintainableArtefact().getUrn());

        categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn());
        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac startCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkStartValidity(urn, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        srmValidation.checkStartValidity(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getLifeCycleMetadata());

        // Start validity
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.startCategorySchemeValidity(ctx, urn, null);
        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        srmValidation.checkEndValidity(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getLifeCycleMetadata());

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
        CategoriesMetamacInvocationValidator.checkRetrieveByUrn(urn);
        CategoryMetamac category = getCategoryMetamacRepository().findByUrn(urn);
        if (category == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return category;
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
    public TaskImportationInfo importCategoriesTsv(ServiceContext ctx, String categorySchemeUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkImportCategoriesTsv(categorySchemeUrn, file, fileName, updateAlreadyExisting, canBeBackground, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(categorySchemeVersion, categorySchemeVersion.getLifeCycleMetadata(), canBeBackground);

        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_ITEMS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportCategoriesTsvInBackground(ctx, categorySchemeUrn, file, fileName, updateAlreadyExisting); 
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Execute importation now
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<CategoryMetamac> categtoriesToPersist = null;
        Map<String, Item> categoriesToPersistByCode = null;
        Map<String, Item> categoriesPreviousInCategorySchemeByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCategoriesTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCategories(line, exceptionItems);

            // Categories
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Retrieve actual categories in category scheme
                categtoriesToPersist = new ArrayList<CategoryMetamac>(); // save in order is required (first parent and then children)
                categoriesToPersistByCode = new HashMap<String, Item>();
                categoriesPreviousInCategorySchemeByCode = new HashMap<String, Item>();
                for (Item item : categorySchemeVersion.getItems()) {
                    categoriesPreviousInCategorySchemeByCode.put(item.getNameableArtefact().getCode(), (CategoryMetamac) item);
                }

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform category and add to list to persist
                    CategoryMetamac category = TsvImportationUtils.tsvLineToCategory(header, columns, lineNumber, categorySchemeVersion, updateAlreadyExisting,
                            categoriesPreviousInCategorySchemeByCode, categoriesToPersistByCode, exceptionItems, informationItems);
                    if (category != null) {
                        categtoriesToPersist.add(category);
                        categoriesToPersistByCode.put(category.getNameableArtefact().getCode(), category);
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(bufferedReader);
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            // rollback and inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        // Save categories and scheme
        saveCategoriesEfficiently(categtoriesToPersist, categoriesToPersistByCode);
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
        itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    @Override
    public String exportCategoriesTsv(ServiceContext ctx, String categorySchemetUrn) throws MetamacException {

        CategoriesMetamacInvocationValidator.checkExportCategoriesTsv(categorySchemetUrn, null);

        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemetUrn);

        List<ItemResult> items = getCategoryMetamacRepository().findCategoriesByCategorySchemeUnordered(categorySchemeVersion.getId(), CategoryMetamacResultSelection.EXPORT);

        List<String> languages = srmConfiguration.retrieveLanguages();

        return TsvExportationUtils.exportCategories(items, languages);
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
    public List<ItemResult> retrieveCategoriesByCategorySchemeUrnUnordered(ServiceContext ctx, String categorySchemeUrn, ItemResultSelection itemResultSelection) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategoriesByCategorySchemeUrnUnordered(categorySchemeUrn, itemResultSelection, null);

        if (itemResultSelection == null) {
            itemResultSelection = ItemResultSelection.RETRIEVE; // default
        }
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        return categoryRepository.findCategoriesByCategorySchemeUnordered(categorySchemeVersion.getId(), itemResultSelection);
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
    public void checkCategorySchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn)
            throws MetamacException {
        getCategorySchemeVersionMetamacRepository().checkCategorySchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCategoryMetamacRepository().checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void checkCategorySchemeWithRelatedResourcesExternallyPublished(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        String itemSchemeVersionUrn = categorySchemeVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    @Override
    public Categorisation createCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {

        preCreateCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);
        srmValidation.checkMaintainerIsDefault(ctx, maintainerUrn);

        // Create
        Categorisation categorisation = categoriesService.createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn, SrmConstants.VERSION_PATTERN_METAMAC);
        categorisation = postCreateCategorisation(ctx, artefactCategorisedUrn, categorisation);
        return categorisation;
    }

    @Override
    public void preCreateCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {
        // Validation
        CategoriesInvocationValidator.checkCreateCategorisation(categoryUrn, artefactCategorisedUrn, maintainerUrn, null);
        checkCategorisationToCreateOrUpdate(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);
    }

    @Override
    public PagedResult<CategoryMetamac> findCategoriesCanBeCategorisationCategoryByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).distinctRoot().build();
        }
        // Category scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class)
                .withProperty(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return findCategoriesByCondition(ctx, conditions, pagingParameter);
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

        // Validation
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkMaintainerIsDefault(ctx, categorisation.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());

        // Delete
        categoriesService.deleteCategorisation(ctx, urn);
    }

    /**
     * Important: Categorisations must be created with new code
     */
    @Override
    public void createVersionFromTemporalCategorisations(ServiceContext ctx, MaintainableArtefact maintainableArtefact) throws MetamacException {
        List<Categorisation> categorisationsToDelete = new ArrayList<Categorisation>(maintainableArtefact.getCategorisations());
        if (!CollectionUtils.isEmpty(categorisationsToDelete)) {
            for (Categorisation categorisationToDelete : categorisationsToDelete) {
                maintainableArtefact.removeCategorisation(categorisationToDelete);
            }
            String maintainerUrn = maintainableArtefact.getMaintainer().getNameableArtefact().getUrn();
            String artefactCategorisedUrn = maintainableArtefact.getUrn();
            for (Categorisation categorisationToDelete : categorisationsToDelete) {
                String categoryUrn = categorisationToDelete.getCategory().getNameableArtefact().getUrn();
                Categorisation categorisation = categoriesService.createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn, SrmConstants.VERSION_PATTERN_METAMAC);
                maintainableArtefact.getCategorisations().add(categorisation);
            }
        }
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
        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkModifyCategorisationValidity(ctx, categorisation.getMaintainableArtefact());

        return categoriesService.startCategorisationValidity(ctx, urn, validFrom);
    }

    @Override
    public Categorisation endCategorisationValidity(ServiceContext ctx, String urn, DateTime validTo) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkModifyCategorisationValidity(ctx, categorisation.getMaintainableArtefact());

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

    @Override
    public void checkCategorisationsWithRelatedResourcesExternallyPublished(ServiceContext ctx, String artefactCategorisedUrn, Map<String, MetamacExceptionItem> exceptionItemsByUrn)
            throws MetamacException {
        List<Categorisation> categorisations = retrieveCategorisationsByArtefact(ctx, artefactCategorisedUrn);
        for (Categorisation categorisation : categorisations) {
            Category category = categorisation.getCategory();
            if (!category.getItemSchemeVersion().getMaintainableArtefact().getPublicLogic()) {
                addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, category.getNameableArtefact().getUrn());
            }
            Organisation maintainer = categorisation.getMaintainableArtefact().getMaintainer();
            if (!maintainer.getItemSchemeVersion().getMaintainableArtefact().getPublicLogic()) {
                addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.ORGANISATION_SCHEME_NOT_EXTERNALLY_PUBLISHED, maintainer.getNameableArtefact().getUrn());
            }
        }
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

    private void checkCategorisationCategory(ServiceContext ctx, String categoryUrn) throws MetamacException {
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        Category category = retrieveCategoryByUrn(ctx, categoryUrn);
        Long categoryId = category.getId();
        List<ConditionalCriteria> criteriaToVerifyCategory = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).withProperty(CategoryMetamacProperties.id()).eq(categoryId).build();
        PagedResult<CategoryMetamac> result = findCategoriesCanBeCategorisationCategoryByCondition(ctx, criteriaToVerifyCategory, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(categoryId)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CATEGORY);
        }
    }

    private void checkCategorisationMaintainer(ServiceContext ctx, String maintainerUrn) throws MetamacException {
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        Organisation organisation = organisationsService.retrieveOrganisationByUrn(ctx, maintainerUrn);
        Long organisationId = organisation.getId();
        List<ConditionalCriteria> criteriaToVerifyMaintainer = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.id()).eq(organisationId)
                .build();
        PagedResult<OrganisationMetamac> result = findOrganisationsCanBeCategorisationMaintainerByCondition(ctx, criteriaToVerifyMaintainer, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(organisationId)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINER);
        }
    }

    private PagedResult<OrganisationMetamac> findOrganisationsCanBeCategorisationMaintainerByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).distinctRoot().build();
        }
        // Organisation scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class)
                .withProperty(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return organisationsService.findOrganisationsByCondition(ctx, conditions, pagingParameter);
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

    /**
     * Common validations to create or update a categorisation
     */
    private void checkCategorisationToCreateOrUpdate(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {
        // Category
        checkCategorisationCategory(ctx, categoryUrn);
        // Maintainer
        checkCategorisationMaintainer(ctx, maintainerUrn);
    }

    private TaskInfo createVersionOfCategoryScheme(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, VersionTypeEnum versionType, boolean isTemporal)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkVersioningCategoryScheme(urnToCopy, versionType, isTemporal, null, null);
        checkCategorySchemeToVersioning(ctx, urnToCopy, isTemporal);
        // Versioning
        return categoriesService.versioningCategoryScheme(ctx, urnToCopy, versionType, isTemporal, itemSchemesCopyCallback);
    }

    private void checkCategorySchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = retrieveCategorySchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(categorySchemeVersionToCopy.getMaintainableArtefact(), categorySchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        // Check does not exist any version 'no final'
        ItemSchemeVersion categorySchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(categorySchemeVersionToCopy.getItemScheme().getId());
        if (categorySchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(categorySchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkCategorySchemeCanBeModified(CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(categorySchemeVersion.getLifeCycleMetadata(), categorySchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(categorySchemeVersion);
    }

    private CategorySchemeVersionMetamac retrieveCategorySchemeVersionByUrn(String urn) throws MetamacException {
        CategoriesInvocationValidator.checkRetrieveByUrn(urn);
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().findByUrn(urn);
        if (categorySchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return categorySchemeVersion;
    }

    private void saveCategoriesEfficiently(Collection<CategoryMetamac> categoriesToPersist, Map<String, Item> categoriesToPersistByCode) {
        for (CategoryMetamac categoryMetamac : categoriesToPersist) {
            if (categoryMetamac.getParent() != null) {
                if (categoriesToPersistByCode.containsKey(categoryMetamac.getParent().getNameableArtefact().getCode())) {
                    // update reference because it was saved
                    categoryMetamac.setParent(categoriesToPersistByCode.get(categoryMetamac.getParent().getNameableArtefact().getCode()));
                }
            }
            categoryMetamac = getCategoryMetamacRepository().save(categoryMetamac);
            // update reference after save to assign to children
            categoriesToPersistByCode.put(categoryMetamac.getNameableArtefact().getCode(), categoryMetamac);
        }
    }
}