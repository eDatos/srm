package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorisationsType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategorySchemesType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategoryType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseJaxb2DoInheritUtils;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;

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
            // Inherit translations (for all international strings)
            targetMetamac.getMaintainableArtefact().setName(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName())); // Name
            targetMetamac.getMaintainableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations
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
            // Inherit translations (for all international strings)
            targetMetamac.getNameableArtefact().setName(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName())); // Name
            targetMetamac.getNameableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
        }

        // Fill Metadata
        categoriesMetamacService.preCreateCategory(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }
    // @Override
    // public void categorisationJaxToDoExtension(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {
    //
    // // Fill Metadata
    // // TODO call preCreateCategorisation
    // categoriesMetamacService
    // .preCreateCategorisation(ctx, target.getCategory().getNameableArtefact().getUrn(), target.getArtefactCategorised().getUrn(), target.getMaintainableArtefact().getCode());
    //
    // target.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false
    //
    // }

    @Override
    public void categorisationJaxToDoExtensionPreCreate(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {
        target.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false
        categoriesMetamacService
                .preCreateCategorisation(ctx, target.getCategory().getNameableArtefact().getUrn(), target.getArtefactCategorised().getUrn(), target.getMaintainableArtefact().getCode());
    }

    @Override
    public void categorisationJaxToDoExtensionPostCreate(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {
        categoriesMetamacService.postCreateCategorisation(ctx, target.getCategory().getNameableArtefact().getUrn(), target);
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, CategorySchemeVersion source) throws MetamacException {
        validateRestrictionsItemSchemeVersion(ctx, source, false);
    }

    @Override
    public void validateRestrictions(ServiceContext ctx, Categorisation source) throws MetamacException {
        validateRestrictionsCategorisation(ctx, source.getMaintainableArtefact(), false);
        // TODO añadir validación de que en metamac el artefacto (o el esquema al que pertenezca) debe de ser final
        // TODO añadir validación de que en metamac el esquema de la categoría debe de ser final
    }

}
