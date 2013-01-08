package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorisationType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorisationsType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorySchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategorySchemesType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;

@Component("categoriesMetamacJaxb2DoCallback")
public class CategoriesJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements CategoriesJaxb2DoCallback {

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public List<CategorySchemeVersion> createCategorySchemesDo(CategorySchemesType source) {
        List<CategorySchemeVersion> categorySchemeVersions = new ArrayList<CategorySchemeVersion>();

        return categorySchemeVersions;
    }

    @Override
    public CategorySchemeVersion createCategorySchemeDo(CategorySchemeType source) {
        CategorySchemeVersion categorySchemeVersion = new CategorySchemeVersionMetamac();

        return categorySchemeVersion;
    }

    @Override
    public Category createCategoryDo(CategoryType source) {
        Category category = new CategoryMetamac();

        return category;
    }

    @Override
    public List<Categorisation> createCategorisationsDo(CategorisationsType source) {
        List<Categorisation> categorisations = new ArrayList<Categorisation>();

        return categorisations;
    }

    @Override
    public Categorisation createCategorisationDo(CategorisationType source) {
        Categorisation categorisation = new Categorisation();

        return categorisation;
    }

    /**************************************************************************
     * EXTENSIONS
     **************************************************************************/
    @Override
    public void categorySchemeJaxbToDoExtension(CategorySchemeType source, CategorySchemeVersion target) {
        // nothing to extend in the default implementation
    }

    @Override
    public void categoryJaxbToDoExtension(CategoryType source, Category target) {
        // nothing to extend in the default implementation
    }

    @Override
    public void categorisationJaxToDoExtension(CategorisationType source, Categorisation target) {
        // nothing to extend in the default implementation
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, CategorySchemeVersion source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
    }

    @Override
    public void validateRestrictions(ServiceContext ctx, Categorisation source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
    }

}
