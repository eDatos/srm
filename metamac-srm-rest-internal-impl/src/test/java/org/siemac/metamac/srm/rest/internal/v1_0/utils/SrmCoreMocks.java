package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmCoreMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme(String agencyID, String resourceID, String version) {
        ConceptSchemeVersionMetamac target = ConceptsMetamacDoMocks.mockConceptScheme(mockMaintainer(agencyID));
        ConceptsMetamacDoMocks.fillConceptSchemeAutogeneratedMetadata(target);
        fillMaintainableArtefactWithInmutableValues(agencyID, resourceID, version, target.getMaintainableArtefact());
        return target;
    }

    public static ConceptSchemeVersionMetamac mockConceptSchemeWithConcepts(String agencyID, String resourceID, String version) {

        ConceptSchemeVersionMetamac conceptSchemeVersion = mockConceptScheme(agencyID, resourceID, version);

        // concepts
        ConceptMetamac concept1 = mockConcept("concept1", conceptSchemeVersion, null);
        ConceptMetamac concept2 = mockConcept("concept2", conceptSchemeVersion, null);
        ConceptMetamac concept2A = mockConcept("concept2A", conceptSchemeVersion, concept2);
        ConceptMetamac concept2B = mockConcept("concept2B", conceptSchemeVersion, concept2);

        // concepts hierarchy
        conceptSchemeVersion.addItem(concept1);
        conceptSchemeVersion.addItemsFirstLevel(concept1);
        conceptSchemeVersion.addItem(concept2);
        conceptSchemeVersion.addItemsFirstLevel(concept2);
        conceptSchemeVersion.addItem(concept2A);
        conceptSchemeVersion.addItem(concept2B);
        concept2.addChildren(concept2A);
        concept2.addChildren(concept2B);

        // concept relations
        concept1.setConceptExtends(mockConcept("1_conceptExtends1", null, null));
        concept1.addRoleConcept(mockConcept("1_conceptRole1", null, null));
        concept1.addRoleConcept(mockConcept("1_conceptRole2", null, null));

        concept2A.setConceptExtends(mockConcept("2A_conceptExtends1", null, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole1", null, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole2", null, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole3", null, null));

        concept1.addRelatedConcept(concept2);
        concept2.addRelatedConcept(concept1);

        concept1.addRelatedConcept(concept2B);
        concept2B.addRelatedConcept(concept1);

        concept2A.addRelatedConcept(concept2B);
        concept2B.addRelatedConcept(concept2A);

        return conceptSchemeVersion;
    }

    public static CategorySchemeVersionMetamac mockCategoryScheme(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = CategoriesMetamacDoMocks.mockCategoryScheme(mockMaintainer(agencyID));
        CategoriesMetamacDoMocks.fillCategorySchemeAutogeneratedMetadata(target);
        fillMaintainableArtefactWithInmutableValues(agencyID, resourceID, version, target.getMaintainableArtefact());
        return target;
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeWithCategories(String agencyID, String resourceID, String version) {

        CategorySchemeVersionMetamac categorySchemeVersion = mockCategoryScheme(agencyID, resourceID, version);

        // categorys
        CategoryMetamac category1 = mockCategory("category1", categorySchemeVersion, null);
        CategoryMetamac category2 = mockCategory("category2", categorySchemeVersion, null);
        CategoryMetamac category2A = mockCategory("category2A", categorySchemeVersion, category2);
        CategoryMetamac category2B = mockCategory("category2B", categorySchemeVersion, category2);

        // categorys hierarchy
        categorySchemeVersion.addItem(category1);
        categorySchemeVersion.addItemsFirstLevel(category1);
        categorySchemeVersion.addItem(category2);
        categorySchemeVersion.addItemsFirstLevel(category2);
        categorySchemeVersion.addItem(category2A);
        categorySchemeVersion.addItem(category2B);
        category2.addChildren(category2A);
        category2.addChildren(category2B);

        return categorySchemeVersion;
    }

    public static ConceptMetamac mockConcept(String resourceID, ItemSchemeVersion itemSchemeVersion, ConceptMetamac parent) {

        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(mockConceptType("conceptType1"));
        ConceptsMetamacDoMocks.fillConceptAutogeneratedMetadata(concept);

        // relations
        concept.setParent(parent);
        concept.setItemSchemeVersion(itemSchemeVersion);

        // metadata
        fillNameableArtefactWithInmutableValues(resourceID, concept.getNameableArtefact()); // immutable values to test xml streams
        concept.setPluralName(mockInternationalString("pluralName", resourceID));
        concept.setAcronym(mockInternationalString("acronym", resourceID));
        concept.setDescriptionSource(mockInternationalString("descriptionSource", resourceID));
        concept.setContext(mockInternationalString("context", resourceID));
        concept.setDocMethod(mockInternationalString("docMethod", resourceID));
        concept.setDerivation(mockInternationalString("derivation", resourceID));
        concept.setLegalActs(mockInternationalString("legalActs", resourceID));

        return concept;
    }

    public static ConceptMetamac mockConceptWithConceptRelations(String resourceID, ItemSchemeVersion itemSchemeVersion, ConceptMetamac parent) {

        ConceptMetamac concept = mockConcept(resourceID, itemSchemeVersion, parent);

        // relations
        concept.setConceptExtends(mockConcept("1_conceptExtends1", null, null));
        concept.addRoleConcept(mockConcept("1_conceptRole1", null, null));
        concept.addRoleConcept(mockConcept("1_conceptRole2", null, null));
        concept.addRelatedConcept(mockConcept("1_conceptRelated1", null, null));
        concept.addRelatedConcept(mockConcept("1_conceptRelated2", null, null));
        concept.addRelatedConcept(mockConcept("1_conceptRelated3", null, null));

        return concept;
    }

    public static CategoryMetamac mockCategory(String resourceID, ItemSchemeVersion itemSchemeVersion, CategoryMetamac parent) {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
        CategoriesMetamacDoMocks.fillCategoryAutogeneratedMetadata(category);

        // relations
        category.setParent(parent);
        category.setItemSchemeVersion(itemSchemeVersion);

        // metadata
        fillNameableArtefactWithInmutableValues(resourceID, category.getNameableArtefact()); // immutable values to test xml streams
        return category;
    }

    public static List<ConceptType> mockConceptTypes() {
        List<ConceptType> conceptTypes = new ArrayList<ConceptType>();
        conceptTypes.add(mockConceptType("conceptType1"));
        conceptTypes.add(mockConceptType("conceptType2"));
        conceptTypes.add(mockConceptType("conceptType3"));
        return conceptTypes;
    }

    private static ConceptType mockConceptType(String identifier) {
        ConceptType conceptType = new ConceptType();
        conceptType.setIdentifier(identifier);
        conceptType.setDescription(mockInternationalString("description", identifier));
        return conceptType;
    }

    private static InternationalString mockInternationalString(String metadata, String subCode) {
        String subTitle = subCode != null ? metadata + "-" + subCode : metadata;
        return mockInternationalString("es", subTitle + " en Español", "en", subTitle + " in English");
    }

    private static InternationalString mockInternationalString(String locale1, String label1, String locale2, String label2) {

        InternationalString internationalString = new InternationalString();

        LocalisedString internationalStringLocale1 = new LocalisedString();
        internationalStringLocale1.setLocale(locale1);
        internationalStringLocale1.setLabel(label1);
        internationalString.addText(internationalStringLocale1);

        LocalisedString internationalStringLocale2 = new LocalisedString();
        internationalStringLocale2.setLocale(locale2);
        internationalStringLocale2.setLabel(label2);
        internationalString.addText(internationalStringLocale2);

        return internationalString;
    }

    private static OrganisationMetamac mockMaintainer(String agencyID) {
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.AGENCY);
        organisation.getNameableArtefact().setCode(agencyID);
        OrganisationsMetamacDoMocks.fillOrganisationAutogeneratedMetadata(organisation);
        return organisation;
    }

    private static void fillMaintainableArtefactWithInmutableValues(String agencyID, String resourceID, String version, MaintainableArtefact maintainableArtefact) {
        maintainableArtefact.setCode(resourceID);
        maintainableArtefact.setUrn("urn:" + resourceID + ":" + version);
        maintainableArtefact.setName(mockInternationalString("name", resourceID + "v" + version));
        maintainableArtefact.setDescription(mockInternationalString("description", resourceID + "v" + version));
        maintainableArtefact.setVersionLogic(version);
    }

    private static void fillNameableArtefactWithInmutableValues(String resourceID, NameableArtefact nameableArtefact) {
        nameableArtefact.setUrn("urn:" + resourceID);
        nameableArtefact.setCode(resourceID);
        nameableArtefact.setName(mockInternationalString("name", resourceID));
        nameableArtefact.setDescription(mockInternationalString("description", resourceID));
    }
}