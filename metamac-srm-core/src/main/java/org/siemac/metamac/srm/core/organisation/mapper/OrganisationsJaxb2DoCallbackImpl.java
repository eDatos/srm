package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AgencySchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AgencyType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataConsumerSchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataConsumerType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataProviderSchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataProviderType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.OrganisationSchemesType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.OrganisationUnitSchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.OrganisationUnitType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseJaxb2DoInheritUtils;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@org.springframework.stereotype.Component("organisationsMetamacJaxb2DoCallback")
public class OrganisationsJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements OrganisationsJaxb2DoCallback {

    @Autowired
    private OrganisationsMetamacService organisationsMetamacService;

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public List<OrganisationSchemeVersion> createOrganisationSchemesDo(OrganisationSchemesType source) {
        return new ArrayList<OrganisationSchemeVersion>();
    }

    @Override
    public OrganisationSchemeVersion createOrganisationSchemeDo() {
        return new OrganisationSchemeVersionMetamac();
    }

    @Override
    public Organisation createOrganisationDo() {
        return new OrganisationMetamac();
    }

    /**************************************************************************
     * EXTENSIONS
     * 
     * @throws MetamacException
     **************************************************************************/
    @Override
    public void agencySchemeJaxbToDoExtensionPreCreate(ServiceContext ctx, AgencySchemeType source, OrganisationSchemeVersion previous, OrganisationSchemeVersion target) throws MetamacException {
        OrganisationSchemeVersionMetamac previousMetamac = (OrganisationSchemeVersionMetamac) previous;
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        organisationsMetamacService.preCreateOrganisationScheme(ctx, targetMetamac);
    }

    @Override
    public void agencyJaxbToDoExtensionPreCreate(ServiceContext ctx, AgencyType source, OrganisationSchemeVersion organisationSchemeVersion, Organisation previous, Organisation target)
            throws MetamacException {
        OrganisationMetamac previousMetamac = (OrganisationMetamac) target;
        OrganisationMetamac targetMetamac = (OrganisationMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
            BaseJaxb2DoInheritUtils.inheritAnnotationsContact(previousMetamac.getContacts(), targetMetamac.getContacts()); // Contacts
        }

        // Fill pre-persist meta-data
        organisationsMetamacService.preCreateOrganisation(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    @Override
    public void dataConsumerSchemeJaxbToDoExtensionPreCreate(ServiceContext ctx, DataConsumerSchemeType source, OrganisationSchemeVersion previous, OrganisationSchemeVersion target)
            throws MetamacException {
        OrganisationSchemeVersionMetamac previousMetamac = (OrganisationSchemeVersionMetamac) previous;
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        organisationsMetamacService.preCreateOrganisationScheme(ctx, targetMetamac);
    }

    @Override
    public void dataConsumerJaxbToDoExtensionPreCreate(ServiceContext ctx, DataConsumerType source, OrganisationSchemeVersion organisationSchemeVersion, Organisation previous, Organisation target)
            throws MetamacException {
        OrganisationMetamac previousMetamac = (OrganisationMetamac) target;
        OrganisationMetamac targetMetamac = (OrganisationMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
            BaseJaxb2DoInheritUtils.inheritAnnotationsContact(previousMetamac.getContacts(), targetMetamac.getContacts()); // Contacts
        }

        // Fill pre-persist meta-data
        organisationsMetamacService.preCreateOrganisation(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    @Override
    public void dataProviderSchemeJaxbToDoExtensionPreCreate(ServiceContext ctx, DataProviderSchemeType source, OrganisationSchemeVersion previous, OrganisationSchemeVersion target)
            throws MetamacException {
        OrganisationSchemeVersionMetamac previousMetamac = (OrganisationSchemeVersionMetamac) previous;
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        organisationsMetamacService.preCreateOrganisationScheme(ctx, targetMetamac);
    }

    @Override
    public void dataProviderJaxbToDoExtensionPreCreate(ServiceContext ctx, DataProviderType source, OrganisationSchemeVersion organisationSchemeVersion, Organisation previous, Organisation target)
            throws MetamacException {
        OrganisationMetamac previousMetamac = (OrganisationMetamac) target;
        OrganisationMetamac targetMetamac = (OrganisationMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
            BaseJaxb2DoInheritUtils.inheritAnnotationsContact(previousMetamac.getContacts(), targetMetamac.getContacts()); // Contacts
        }

        // Fill pre-persist meta-data
        organisationsMetamacService.preCreateOrganisation(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    @Override
    public void organisationUnitSchemeJaxbToDoExtensionPreCreate(ServiceContext ctx, OrganisationUnitSchemeType source, OrganisationSchemeVersion previous, OrganisationSchemeVersion target)
            throws MetamacException {
        OrganisationSchemeVersionMetamac previousMetamac = (OrganisationSchemeVersionMetamac) previous;
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        organisationsMetamacService.preCreateOrganisationScheme(ctx, targetMetamac);
    }

    @Override
    public void organisationUnitJaxbToDoExtensionPreCreate(ServiceContext ctx, OrganisationUnitType source, OrganisationSchemeVersion organisationSchemeVersion, Organisation previous,
            Organisation target) throws MetamacException {
        OrganisationMetamac previousMetamac = (OrganisationMetamac) target;
        OrganisationMetamac targetMetamac = (OrganisationMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getName(), targetMetamac.getNameableArtefact().getName()); // Name
            BaseJaxb2DoInheritUtils.inheritInternationString(previousMetamac.getNameableArtefact().getDescription(), targetMetamac.getNameableArtefact().getDescription()); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotationsInternatialString(previousMetamac.getNameableArtefact().getAnnotations(), targetMetamac.getNameableArtefact().getAnnotations()); // Annotations
            BaseJaxb2DoInheritUtils.inheritAnnotationsContact(previousMetamac.getContacts(), targetMetamac.getContacts()); // Contacts
        }

        // Fill pre-persist meta-data
        organisationsMetamacService.preCreateOrganisation(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, OrganisationSchemeVersion source) throws MetamacException {
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(source.getOrganisationSchemeType())) {
            validateRestrictionsItemSchemeVersion(ctx, source, false);
        } else {
            validateRestrictionsItemSchemeVersion(ctx, source, true);
        }
    }

}
