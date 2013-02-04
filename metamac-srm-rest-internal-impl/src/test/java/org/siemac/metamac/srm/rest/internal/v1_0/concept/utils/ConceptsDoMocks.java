package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.DoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.EnumeratedRepresentation;
import com.arte.statistic.sdmx.srm.core.base.domain.Facet;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.base.domain.TextFormatRepresentation;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;

public class ConceptsDoMocks extends DoMocks {

    public static ConceptSchemeVersionMetamac mockConceptScheme(String agencyID, String resourceID, String version) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(ConceptSchemeTypeEnum.OPERATION);
        target.setRelatedOperation(mockOperationExternalItem("operation-" + resourceID));
        target.setLifeCycleMetadata(mockLifecycleExternallyPublished());
        mockItemSchemeVersion(target, resourceID, version, agencyID);
        return target;
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

        return target;
    }

    public static ConceptMetamac mockConcept(String resourceID, ItemSchemeVersion itemSchemeVersion, ConceptMetamac parent) {
        ConceptMetamac target = new ConceptMetamac();
        target.setPluralName(mockInternationalString("pluralName", resourceID));
        target.setAcronym(mockInternationalString("acronym", resourceID));
        target.setDescriptionSource(mockInternationalString("descriptionSource", resourceID));
        target.setContext(mockInternationalString("context", resourceID));
        target.setDocMethod(mockInternationalString("docMethod", resourceID));
        target.setDerivation(mockInternationalString("derivation", resourceID));
        target.setLegalActs(mockInternationalString("legalActs", resourceID));
        target.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        target.setType(mockConceptType("conceptType1"));
        target.setVariable(CodesDoMocks.mockVariable("variable1"));
        target.setCoreRepresentation(mockRepresentation(TypeRepresentationEnum.ENUMERATED));
        mockItem(target, resourceID, itemSchemeVersion, parent);
        return target;
    }

    public static ConceptMetamac mockConceptWithConceptRelations(String resourceID, ItemSchemeVersion itemSchemeVersion, ConceptMetamac parent) {
        ConceptMetamac target = mockConcept(resourceID, itemSchemeVersion, parent);
        target.setConceptExtends(mockConcept("1_conceptExtends1", mockConceptScheme("agencyID1", "resourceID11", "01.000"), null));
        target.addRoleConcept(mockConcept("1_conceptRole1", mockConceptScheme("agencyID1", "resourceID22", "02.000"), null));
        target.addRoleConcept(mockConcept("1_conceptRole2", mockConceptScheme("agencyID1", "resourceID33", "03.000"), null));
        target.addRelatedConcept(mockConcept("1_conceptRelated1", itemSchemeVersion, null));
        target.addRelatedConcept(mockConcept("1_conceptRelated2", itemSchemeVersion, null));
        target.addRelatedConcept(mockConcept("1_conceptRelated3", itemSchemeVersion, null));
        return target;
    }

    public static List<ConceptType> mockConceptTypes() {
        List<ConceptType> targets = new ArrayList<ConceptType>();
        targets.add(mockConceptType("conceptType1"));
        targets.add(mockConceptType("conceptType2"));
        targets.add(mockConceptType("conceptType3"));
        return targets;
    }

    private static ConceptType mockConceptType(String identifier) {
        ConceptType target = new ConceptType();
        target.setIdentifier(identifier);
        target.setDescription(mockInternationalString("description", identifier));
        return target;
    }

    private static ExternalItem mockOperationExternalItem(String code) {
        ExternalItem target = new ExternalItem();
        target.setCode(code);
        target.setUri(code);
        target.setUrn("urn:" + code);
        target.setType(TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
        target.setManagementAppUrl("managementAppUrl" + code);
        target.setTitle(mockInternationalString("title", code));
        return target;
    }

    private static Representation mockRepresentation(TypeRepresentationEnum type) {
        if (TypeRepresentationEnum.TEXT_FORMAT.equals(type)) {
            return mockTextFormatRepresentation();
        } else if (TypeRepresentationEnum.ENUMERATED.equals(type)) {
            return mockRepresentationEnumerated();
        } else {
            return null;
        }
    }

    private static EnumeratedRepresentation mockRepresentationEnumerated() {
        EnumeratedRepresentation enumeratedRepresentation = new EnumeratedRepresentation();
        enumeratedRepresentation.setEnumerated(new ExternalItem());
        enumeratedRepresentation.getEnumerated().setCode("enumeratedRepresentation");
        enumeratedRepresentation.getEnumerated().setUri("uriEnumerated");
        enumeratedRepresentation.getEnumerated().setUrn("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CodelistMock(1.0)");
        enumeratedRepresentation.getEnumerated().setType(TypeExternalArtefactsEnum.CODELIST);
        return enumeratedRepresentation;
    }

    private static TextFormatRepresentation mockTextFormatRepresentation() {
        TextFormatRepresentation textFormatRepresentation = new TextFormatRepresentation();
        Facet notEnumerated = new Facet(FacetValueTypeEnum.STRING_FVT);
        notEnumerated.setPatternFT("yyyy");
        textFormatRepresentation.setNonEnumerated(notEnumerated);
        return textFormatRepresentation;
    }
}