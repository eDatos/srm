package org.siemac.metamac.srm.rest.external.v1_0.mapper.category;

import java.math.BigInteger;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemResource;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CategoriesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CategoriesDo2RestMapperV10 {

    @Override
    public CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        CategorySchemes targets = new CategorySchemes();
        targets.setKind(SrmRestConstants.KIND_CATEGORY_SCHEMES);

        // Pagination
        String baseLink = toCategorySchemesLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategorySchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCategorySchemes().add(target);
        }
        return targets;
    }

    @Override
    public CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }

        CategoryScheme target = new CategoryScheme();

        target.setKind(SrmRestConstants.KIND_CATEGORY_SCHEME);
        target.setSelfLink(toCategorySchemeSelfLink(source));
        target.setParentLink(toCategorySchemeParentLink(source));
        target.setChildLinks(toCategorySchemeChildLinks(source));

        toItemScheme(source, source.getLifeCycleMetadata(), target);

        return target;
    }

    @Override
    public Categories toCategories(PagedResult<CategoryMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Categories targets = new Categories();
        targets.setKind(SrmRestConstants.KIND_CATEGORIES);

        // Pagination
        String baseLink = toCategoriesLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategoryMetamac source : sourcesPagedResult.getValues()) {
            ItemResource target = toResource(source);
            targets.getCategories().add(target);
        }
        return targets;
    }

    @Override
    public Categories toCategories(List<ItemResult> sources, CategorySchemeVersionMetamac categorySchemeVersion) {

        Categories targets = new Categories();
        targets.setKind(SrmRestConstants.KIND_CATEGORIES);

        // No pagination
        targets.setSelfLink(toCategoriesLink(categorySchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResource target = toResource(source, categorySchemeVersion);
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

        target.setKind(SrmRestConstants.KIND_CATEGORY);
        target.setSelfLink(toCategorySelfLink(source));
        target.setParentLink(toCategoryParentLink(source));
        target.setChildLinks(toCategoryChildLinks(source));

        toItem(source, target);

        target.setNestedId(source.getNameableArtefact().getCodeFull());

        return target;
    }

    @Override
    public Categorisations toCategorisations(PagedResult<Categorisation> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        Categorisations targets = new Categorisations();
        targets.setKind(SrmRestConstants.KIND_CATEGORISATIONS);

        // Pagination
        String baseLink = toCategorisationsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (Categorisation source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCategorisations().add(target);
        }
        return targets;
    }

    @Override
    public org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation toCategorisation(Categorisation source) {
        if (source == null) {
            return null;
        }

        org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation target = new org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation();

        target.setKind(SrmRestConstants.KIND_CATEGORISATION);
        target.setSelfLink(toCategorisationSelfLink(source));
        target.setParentLink(toCategorisationParentLink(source));
        target.setChildLinks(toCategorisationChildLinks(source));

        toMaintainableArtefact(source.getMaintainableArtefact(), null, target);

        target.setSource(source.getArtefactCategorised().getUrn());
        target.setTarget(source.getCategory().getNameableArtefact().getUrn());

        return target;
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        return true; // no additional conditions
    }

    private ResourceLink toCategorySchemeSelfLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY_SCHEME, toCategorySchemeLink(source));
    }

    private ResourceLink toCategorySchemeParentLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY_SCHEMES, toCategorySchemesLink(null, null, null));
    }

    private ChildLinks toCategorySchemeChildLinks(CategorySchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_CATEGORIES, toCategoriesLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toCategorySelfLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY, toCategoryLink(source));
    }
    private ResourceLink toCategorySelfLink(ItemResult source, CategorySchemeVersion categorySchemeVersion) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY, toCategoryLink(source, categorySchemeVersion));
    }

    private ResourceLink toCategoryParentLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORIES, toCategoriesLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toCategoryChildLinks(CategoryMetamac source) {
        // nothing
        return null;
    }

    private ResourceLink toCategorisationSelfLink(Categorisation source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORISATION, toCategorisationLink(source));
    }
    private ResourceLink toCategorisationParentLink(Categorisation source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORISATIONS, toCategorisationsLink(null, null, null));
    }

    private ChildLinks toCategorisationChildLinks(Categorisation source) {
        // nothing
        return null;
    }

    private Resource toResource(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CATEGORY_SCHEME, toCategorySchemeSelfLink(source), target);
        return target;
    }

    private ItemResource toResource(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        ItemResource target = new ItemResource();
        toResource(source, SrmRestConstants.KIND_CATEGORY, toCategorySelfLink(source), target);
        target.setNestedId(source.getNameableArtefact().getCodeFull());
        return target;
    }

    private ItemResource toResource(ItemResult source, CategorySchemeVersionMetamac categorySchemeVersion) {
        if (source == null) {
            return null;
        }
        ItemResource target = new ItemResource();
        toResource(source, SrmRestConstants.KIND_CATEGORY, toCategorySelfLink(source, categorySchemeVersion), target, categorySchemeVersion.getMaintainableArtefact().getIsImported());
        target.setNestedId(source.getCodeFull());
        return target;
    }

    private Resource toResource(Categorisation source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CATEGORISATION, toCategorisationSelfLink(source), target);
        return target;
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
    private String toCategoryLink(ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item, itemSchemeVersion);
    }

    private String toCategorisationsLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), agencyID, resourceID, version);
    }
    private String toCategorisationLink(Categorisation categorisation) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), categorisation.getMaintainableArtefact());
    }

    private String toSubpathItemSchemes() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORY_SCHEMES;
    }
    private String toSubpathItems() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORIES;
    }
    private String toSubpathCategorisations() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORISATIONS;
    }

}