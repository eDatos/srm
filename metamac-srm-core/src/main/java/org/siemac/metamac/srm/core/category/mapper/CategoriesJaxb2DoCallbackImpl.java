package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationsType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemesType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategoryType;

@Component("categoriesMetamacJaxb2DoCallback")
public class CategoriesJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements CategoriesJaxb2DoCallback {

    @Autowired
    private CategoriesMetamacService categoriesMetamacService;

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
    public void categorySchemeJaxbToDoExtension(ServiceContext ctx, CategorySchemeType source, CategorySchemeVersion previous, CategorySchemeVersion target) throws MetamacException {
        CategorySchemeVersionMetamac previousMetamac = (CategorySchemeVersionMetamac) previous;
        CategorySchemeVersionMetamac targetMetamac = (CategorySchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Fill Metadata
        categoriesMetamacService.preCreateCategoryScheme(ctx, targetMetamac);
    }

    @Override
    public void categoryJaxbToDoExtension(ServiceContext ctx, CategoryType source, CategorySchemeVersion categorySchemeVersion, Category previous, Category target) throws MetamacException {
        CategoryMetamac previousMetamac = (CategoryMetamac) previous;
        CategoryMetamac targetMetamac = (CategoryMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
        }

        // Fill Metadata
        categoriesMetamacService.preCreateCategory(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    @Override
    public void categorisationJaxToDoExtension(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {

        // Fill Metadata
        // TODO call preCreateCategorisation
        target.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false
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
