package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import java.math.BigInteger;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategoryType;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.common_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2JaxbCallback;

@Component
public class CategoriesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CategoriesDo2RestMapperV10 {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2JaxbMapper categoriesDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("categoriesDo2JaxbRestInternalCallbackMetamac")
    private CategoriesDo2JaxbCallback                                                categoriesDo2JaxbCallback;

    @Override
    public CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        CategorySchemes targets = new CategorySchemes();
        targets.setKind(RestInternalConstants.KIND_CATEGORY_SCHEMES);

        // Pagination
        String baseLink = toCategorySchemesLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategorySchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getCategorySchemes().add(target);
        }
        return targets;
    }

    @Override
    public CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toCategoryScheme(CategorySchemeVersionMetamac source, CategoryScheme target) method, thank to callback
        return (CategoryScheme) categoriesDo2JaxbSdmxMapper.categorySchemeDoToJaxb(source, categoriesDo2JaxbCallback);
    }

    @Override
    public void toCategoryScheme(CategorySchemeVersionMetamac source, CategoryScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CATEGORY_SCHEME);
        target.setSelfLink(toCategorySchemeSelfLink(source));
        target.setParentLink(toCategorySchemeParentLink(source));
        target.setChildLinks(toCategorySchemeChildLinks(source));
        target.setManagementAppLink(toCategorySchemeManagementApplicationLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
    }

    @Override
    public Categories toCategories(PagedResult<CategoryMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Categories targets = new Categories();
        targets.setKind(RestInternalConstants.KIND_CATEGORIES);

        // Pagination
        String baseLink = toCategoriesLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategoryMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getCategories().add(target);
        }
        return targets;
    }

    @Override
    public Category toCategory(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        Category target = new Category();
        categoriesDo2JaxbSdmxMapper.categoryDoToJaxbWithoutChildren(source, target);

        target.setKind(RestInternalConstants.KIND_CATEGORY);
        target.setSelfLink(toCategorySelfLink(source));
        target.setManagementAppLink(toCategoryManagementApplicationLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toCategoryParentLink(source));
        target.setParent(source.getParent() != null ? source.getParent().getNameableArtefact().getUrn() : null);
        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        target.setChildLinks(toCategoryChildLinks(source));
        return target;
    }

    @Override
    public void toCategory(com.arte.statistic.sdmx.srm.core.category.domain.Category source, CategoryType target) {
        if (source == null) {
            return;
        }
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(toCategorySelfLink(source).getHref());
        }
    }

    @Override
    public Categorisations toCategorisations(PagedResult<Categorisation> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        Categorisations targets = new Categorisations();
        targets.setKind(RestInternalConstants.KIND_CATEGORISATIONS);

        // Pagination
        String baseLink = toCategorisationsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (Categorisation source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getCategorisations().add(target);
        }
        return targets;
    }

    @Override
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation toCategorisation(Categorisation source) {
        if (source == null) {
            return null;
        }
        // following method will call toCategorisation(Categorisation source, Categorisation target) method, thank to callback
        return (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation) categoriesDo2JaxbSdmxMapper.categorisationDoToJaxb(source, categoriesDo2JaxbCallback);
    }

    @Override
    public void toCategorisation(Categorisation source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CATEGORISATION);
        target.setSelfLink(toCategorisationSelfLink(source));
        target.setParentLink(toCategorisationParentLink(source));
        target.setChildLinks(toCategorisationChildLinks(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        return true; // no additional conditions
    }

    private ResourceLink toCategorySchemeSelfLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORY_SCHEME, toCategorySchemeLink(source));
    }

    private ResourceLink toCategorySchemeParentLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORY_SCHEMES, toCategorySchemesLink(null, null, null));
    }

    private ChildLinks toCategorySchemeChildLinks(CategorySchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(RestInternalConstants.KIND_CATEGORIES, toCategoriesLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toCategorySelfLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORY, toCategoryLink(source));
    }

    private ResourceLink toCategoryParentLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORIES, toCategoriesLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toCategoryChildLinks(CategoryMetamac source) {
        // nothing
        return null;
    }

    private ResourceLink toCategorisationSelfLink(Categorisation source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORISATION, toCategorisationLink(source));
    }
    private ResourceLink toCategorisationParentLink(Categorisation source) {
        return toResourceLink(RestInternalConstants.KIND_CATEGORISATIONS, toCategorisationsLink(null, null, null));
    }

    private ChildLinks toCategorisationChildLinks(Categorisation source) {
        // nothing
        return null;
    }

    private ResourceInternal toResource(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CATEGORY_SCHEME, toCategorySchemeSelfLink(source), toCategorySchemeManagementApplicationLink(source));
    }

    private ResourceInternal toResource(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CATEGORY, toCategorySelfLink(source), toCategoryManagementApplicationLink(source));
    }

    private ResourceInternal toResource(Categorisation source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CATEGORISATION, toCategorisationSelfLink(source), null);
    }

    private String toCategorySchemesLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }
    private String toCategorySchemeLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSubpathItemSchemes(), itemSchemeVersion);
    }
    private String toCategoriesLink(String agencyID, String resourceID, String version) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), agencyID, resourceID, version);
    }
    private String toCategoriesLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), itemSchemeVersion);
    }
    private String toCategoryLink(com.arte.statistic.sdmx.srm.core.base.domain.Item item) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item);
    }
    private String toCategorisationsLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), agencyID, resourceID, version);
    }
    private String toCategorisationLink(Categorisation categorisation) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), categorisation.getMaintainableArtefact());
    }

    private String toSubpathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_CATEGORY_SCHEMES;
    }
    private String toSubpathItems() {
        return RestInternalConstants.LINK_SUBPATH_CATEGORIES;
    }
    private String toSubpathCategorisations() {
        return RestInternalConstants.LINK_SUBPATH_CATEGORISATIONS;
    }

    private String toCategorySchemeManagementApplicationLink(CategorySchemeVersionMetamac source) {
        return getInternalWebApplicationNavigation().buildCategorySchemeUrl(source);
    }

    private String toCategoryManagementApplicationLink(CategoryMetamac source) {
        return getInternalWebApplicationNavigation().buildCategoryUrl(source);
    }
}