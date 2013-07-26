package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationsType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemesType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategoryType;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2JaxbCallback;

@org.springframework.stereotype.Component("categoriesDo2JaxbRestInternalCallbackMetamac")
public class CategoriesDo2JaxbCallbackImpl implements CategoriesDo2JaxbCallback {

    @Autowired
    private CategoriesDo2RestMapperV10 categoriesDo2RestMapperV10;

    @Override
    public CategorySchemeType createCategorySchemeJaxb(com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion source) {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme();
    }

    @Override
    public void fillCategorySchemeJaxb(CategorySchemeVersion source, CategorySchemeType target) {
        categoriesDo2RestMapperV10.toCategoryScheme((CategorySchemeVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme) target);
    }

    @Override
    public CategoryType createCategoryJaxb(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        throw new IllegalArgumentException("createCategoryJaxb not supported. Do not return items when itemScheme is retrieved");
    }

    @Override
    public boolean mustRetrieveCategoriesInsideCategoryScheme() {
        return false;
    }

    @Override
    public void fillCategoryJaxb(Category source, CategoryType target) {
        throw new IllegalArgumentException("fillCategoryJaxb not supported. Do not return items when itemScheme is retrieved");
    }

    @Override
    public CategorySchemesType createCategorySchemeJaxb(List<CategorySchemeVersion> sourceList) {
        throw new IllegalArgumentException("createCategorySchemeJaxb not supported");
    }

    @Override
    public CategorisationsType createCategorisationsJaxb(List<Categorisation> sourceList) {
        throw new IllegalArgumentException("createCategorisationsJaxb not supported");
    }

    @Override
    public CategorisationType createCategorisationJaxb(Categorisation source) {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation();
    }

    @Override
    public void fillCategorisationJaxb(Categorisation source, CategorisationType target) {
        categoriesDo2RestMapperV10.toCategorisation(source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation) target);
    }
}