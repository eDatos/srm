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
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.task.utils.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseJaxb2DoInheritUtils;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;

@Component("categoriesMetamacJaxb2DoCallback")
public class CategoriesJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements CategoriesJaxb2DoCallback {

    @Autowired
    private OrganisationSchemeVersionMetamacRepository organisationSchemeVersionMetamacRepository;

    @Autowired
    private CategoriesMetamacService                   categoriesMetamacService;

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
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName())); // Name
            targetMetamac.getMaintainableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription())); // Description
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
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName())); // Name
            targetMetamac.getNameableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
        }

        // Fill Metadata
        categoriesMetamacService.preCreateCategory(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    @Override
    public void categorisationJaxToDoExtensionPreCreate(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {
        target.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false
        categoriesMetamacService.preCreateCategorisation(ctx, target.getCategory().getNameableArtefact().getUrn(), target.getArtefactCategorised().getUrn(), target.getMaintainableArtefact()
                .getMaintainer().getNameableArtefact().getUrn());
    }

    @Override
    public void categorisationJaxToDoExtensionPostCreate(ServiceContext ctx, CategorisationType source, Categorisation target) throws MetamacException {
        categoriesMetamacService.postCreateCategorisation(ctx, target.getArtefactCategorised().getUrn(), target);
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
    }

    @Override
    public OrganisationSchemeVersion findOrganisationSchemeByUrnForImportation(String urn) {
        return organisationSchemeVersionMetamacRepository.findByUrnForImportation(urn);
    }

    @Override
    public OrganisationSchemeVersion findOrganisationSchemeByRefForImportation(String maintainer, String code, String version) {
        return organisationSchemeVersionMetamacRepository.findByRefForImportation(maintainer, code, version);
    }

}
