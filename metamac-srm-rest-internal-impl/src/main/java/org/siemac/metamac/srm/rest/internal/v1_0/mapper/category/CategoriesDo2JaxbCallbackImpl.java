package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorisationType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorisationsType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorySchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorySchemesType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;

@org.springframework.stereotype.Component("categoriesDo2JaxbCallbackMetamac")
public class CategoriesDo2JaxbCallbackImpl implements CategoriesDo2JaxbCallback {

    @Autowired
    private CategoriesDo2RestMapperV10 categoriesDo2RestMapperV10;

    @Override
    public CategorySchemeType createCategorySchemeJaxb(com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion source) {
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme();
    }

    @Override
    public void fillCategorySchemeJaxb(CategorySchemeVersion source, CategorySchemeType target) {
        categoriesDo2RestMapperV10.toCategoryScheme((CategorySchemeVersionMetamac) source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme) target);
    }

    @Override
    public CategoryType createCategoryJaxb(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        // do not return Metamac type because when ItemScheme is retrieved, the items must be SDMX type
        return new CategoryType();
    }

    @Override
    public void fillCategoryJaxb(Category source, CategoryType target) {
        // do not fill Metamac type because when ItemScheme is retrieved, the items must be SDMX type
        categoriesDo2RestMapperV10.toCategory(source, target);
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
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.Categorisation();
    }

    @Override
    public void fillCategorisationJaxb(Categorisation source, CategorisationType target) {
        categoriesDo2RestMapperV10.toCategorisation(source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.Categorisation) target);
    }
}