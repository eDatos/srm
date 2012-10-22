package org.siemac.metamac.srm.core.category.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.enume.domain.VersionPatternEnum;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesDoCopyUtils.CategoryCopyCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Implementation of CategoriesMetamacService.
 */
@Service("categoriesMetamacService")
public class CategoriesMetamacServiceImpl extends CategoriesMetamacServiceImplBase {

    @Autowired
    private CategoriesService    categoriesService;

    // @Autowired
    // private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    @Qualifier("categorySchemeLifeCycle")
    private LifeCycle            categorySchemeLifeCycle;

    @Autowired
    @Qualifier("categoryCopyCallbackMetamac")
    private CategoryCopyCallback categoryCopyCallback;

    public CategoriesMetamacServiceImpl() {
    }

    @Override
    public CategorySchemeVersionMetamac createCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategoryScheme(categorySchemeVersion, null);

        // Fill metadata
        categorySchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        categorySchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);

        // Save categoryScheme
        return (CategorySchemeVersionMetamac) categoriesService.createCategoryScheme(ctx, categorySchemeVersion, VersionPatternEnum.XX_YYY);
    }

    @Override
    public CategorySchemeVersionMetamac updateCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryScheme(categorySchemeVersion, null);
        // CategoriesService checks categoryScheme is not final (Schemes cannot be updated when procStatus is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED)

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

        // Type cast to CategorySchemeVersionMetamac
        List<CategorySchemeVersionMetamac> categorySchemeVersionMetamacs = new ArrayList<CategorySchemeVersionMetamac>();
        for (CategorySchemeVersion categorySchemeVersion : categorySchemeVersions) {
            categorySchemeVersionMetamacs.add((CategorySchemeVersionMetamac) categorySchemeVersion);
        }

        return categorySchemeVersionMetamacs;
    }

    @Override
    public PagedResult<CategorySchemeVersionMetamac> findCategorySchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkFindCategorySchemesByCondition(conditions, pagingParameter, null);

        // Find (do not call SDMX module to avoid type cast)
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class).distinctRoot().build();
        }
        PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = getCategorySchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return categorySchemeVersionPagedResult;
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

    // TODO Para llevar a cabo la publicación interna de un recurso será necesario que previamente exista al menos un anuncio sobre el esquema de categorías a publicar
    @Override
    public CategorySchemeVersionMetamac publishInternallyCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.publishInternally(ctx, urn);
    }

    // TODO validTo, validFrom: ¿rellenar cuando el artefacto no sea del ISTAC? Pendiente decisión del ISTAC.
    @Override
    public CategorySchemeVersionMetamac publishExternallyCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Note: CategoriesService checks categoryScheme isn't final and other conditions
        categoriesService.deleteCategoryScheme(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkVersioningCategoryScheme(urnToCopy, versionType, null, null);
        // Validations of scheme final, ... are done in sdmx module

        // Versioning
        CategorySchemeVersionMetamac categorySchemeNewVersion = (CategorySchemeVersionMetamac) categoriesService.versioningCategoryScheme(ctx, urnToCopy, versionType, categoryCopyCallback);

        return categorySchemeNewVersion;
    }

    @Override
    public CategorySchemeVersionMetamac endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndCategorySchemeValidity(urn, null);

        // Retrieve version in specific procStatus
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().retrieveCategorySchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.endCategorySchemeValidity(ctx, urn);

        return categorySchemeVersion;
    }

    @Override
    public CategoryMetamac createCategory(ServiceContext ctx, String categorySchemeUrn, CategoryMetamac category) throws MetamacException {

        // Validation
        CategorySchemeVersionMetamac categorySchemeVersion = null;
        if (categorySchemeUrn != null) {
            categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        }
        CategoriesMetamacInvocationValidator.checkCreateCategory(categorySchemeVersion, category, null);
        // CategoriesService checks categoryScheme isn't final

        // Save category
        return (CategoryMetamac) categoriesService.createCategory(ctx, categorySchemeUrn, category);
    }

    @Override
    public CategoryMetamac updateCategory(ServiceContext ctx, CategoryMetamac category) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategory(category, null);
        // CategoriesService checks categoryScheme isn't final

        return (CategoryMetamac) categoriesService.updateCategory(ctx, category);
    }

    @Override
    public CategoryMetamac retrieveCategoryByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CategoryMetamac) categoriesService.retrieveCategoryByUrn(ctx, urn);
    }

    @Override
    public PagedResult<CategoryMetamac> findCategoriesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkFindCategoriesByCondition(conditions, pagingParameter, null);

        // Find (do not call sdmx module to avoid typecast)
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).distinctRoot().build();
        }
        PagedResult<CategoryMetamac> categoryPagedResult = getCategoryMetamacRepository().findByCondition(conditions, pagingParameter);
        return categoryPagedResult;
    }

    // @Override
    // public void deleteCategory(ServiceContext ctx, String urn) throws MetamacException {
    //
    // // Note: CategoriesService checks categoryScheme isn't final
    // categoriesService.deleteCategory(ctx, urn);
    // }
    //
    // @Override
    // public List<CategoryMetamac> retrieveCategoriesByCategorySchemeUrn(ServiceContext ctx, String categorySchemeUrn) throws MetamacException {
    //
    // // Retrieve
    // List<Category> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn);
    //
    // // Typecast
    // List<CategoryMetamac> categoriesMetamac = categoriesToCategoryMetamac(categories);
    // return categoriesMetamac;
    // }

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

    // private List<CategoryMetamac> categoriesToCategoryMetamac(List<Category> items) {
    // List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
    // for (Item item : items) {
    // categories.add((CategoryMetamac) item);
    // }
    // return categories;
    // }

    // /**
    // * Finds versions of category scheme in specific procStatus
    // */
    // private List<CategorySchemeVersionMetamac> findCategorySchemeVersionsOfCategorySchemeInProcStatus(ServiceContext ctx, ItemScheme categoryScheme, ProcStatusEnum... procStatus)
    // throws MetamacException {
    //
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
    // .withProperty(CategorySchemeVersionMetamacProperties.itemScheme().id()).eq(categoryScheme.getId())
    // .withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.noLimits();
    // PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = getCategorySchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
    // return categorySchemeVersionPagedResult.getValues();
    // }
}
