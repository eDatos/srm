package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;

public class ConceptsDoMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme(String agencyID, String resourceID, String version) {
        return ConceptsMetamacDoMocks.mockConceptSchemeFixedValues(agencyID, resourceID, version);
    }

    public static ConceptMetamac mockConcept(String resourceID, ConceptSchemeVersionMetamac conceptScheme, ConceptMetamac parent) {
        return ConceptsMetamacDoMocks.mockConceptFixedValues(resourceID, conceptScheme, parent);
    }

    public static ConceptSchemeVersionMetamac mockConceptSchemeWithConcepts(String agencyID, String resourceID, String version) {

        ConceptSchemeVersionMetamac target = mockConceptScheme(agencyID, resourceID, version);

        // concepts
        ConceptMetamac concept1 = mockConcept("concept1", target, null);
        ConceptMetamac concept2 = mockConcept("concept2", target, null);
        ConceptMetamac concept2A = mockConcept("concept2A", target, concept2);
        ConceptMetamac concept2B = mockConcept("concept2B", target, concept2);

        // concepts hierarchy
        target.addItem(concept1);
        target.addItemsFirstLevel(concept1);
        target.addItem(concept2);
        target.addItemsFirstLevel(concept2);
        target.addItem(concept2A);
        target.addItem(concept2B);
        concept2.addChildren(concept2A);
        concept2.addChildren(concept2B);

        // concept relations
        ConceptSchemeVersionMetamac conceptSchemeVersion2 = mockConceptScheme("agency02", "conceptScheme02", "01.000");
        ConceptSchemeVersionMetamac conceptSchemeVersion3 = mockConceptScheme("agency03", "conceptScheme03", "01.000");

        concept1.setConceptExtends(mockConcept("1_conceptExtends1", conceptSchemeVersion2, null));
        concept1.addRoleConcept(mockConcept("1_conceptRole1", conceptSchemeVersion3, null));
        concept1.addRoleConcept(mockConcept("1_conceptRole2", conceptSchemeVersion2, null));

        concept2A.setConceptExtends(mockConcept("2A_conceptExtends1", conceptSchemeVersion3, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole1", conceptSchemeVersion2, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole2", conceptSchemeVersion3, null));
        concept2A.addRoleConcept(mockConcept("2A_conceptRole3", conceptSchemeVersion2, null));

        concept1.addRelatedConcept(concept2);
        concept2.addRelatedConcept(concept1);

        concept1.addRelatedConcept(concept2B);
        concept2B.addRelatedConcept(concept1);

        concept2A.addRelatedConcept(concept2B);
        concept2B.addRelatedConcept(concept2A);

        return target;
    }

    public static ConceptMetamac mockConceptWithConceptRelations(String resourceID, ConceptSchemeVersionMetamac itemSchemeVersion, ConceptMetamac parent) {
        ConceptMetamac target = mockConcept(resourceID, itemSchemeVersion, parent);
        target.setConceptExtends(mockConcept("1_conceptExtends1", mockConceptScheme("agencyID1", "resourceID11", "01.000"), null));
        target.addRoleConcept(mockConcept("1_conceptRole1", mockConceptScheme("agencyID1", "resourceID22", "02.000"), null));
        target.addRoleConcept(mockConcept("1_conceptRole2", mockConceptScheme("agencyID1", "resourceID33", "03.000"), null));
        target.addRelatedConcept(mockConcept("1_conceptRelated1", itemSchemeVersion, null));
        target.addRelatedConcept(mockConcept("1_conceptRelated2", itemSchemeVersion, null));
        target.addRelatedConcept(mockConcept("1_conceptRelated3", itemSchemeVersion, null));
        return target;
    }

    public static ConceptType mockConceptType(String identifier) {
        return ConceptsMetamacDoMocks.mockConceptTypeFixedValues(identifier);
    }

    public static List<ConceptType> mockConceptTypes() {
        List<ConceptType> targets = new ArrayList<ConceptType>();
        targets.add(mockConceptType("conceptType1"));
        targets.add(mockConceptType("conceptType2"));
        targets.add(mockConceptType("conceptType3"));
        return targets;
    }
}