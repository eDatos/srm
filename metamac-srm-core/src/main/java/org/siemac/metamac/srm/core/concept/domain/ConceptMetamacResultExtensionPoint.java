package org.siemac.metamac.srm.core.concept.domain;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;

public class ConceptMetamacResultExtensionPoint {

    private final Map<String, String>            pluralName            = new HashMap<String, String>();
    private final Map<String, String>            acronym               = new HashMap<String, String>();
    private final Map<String, String>            descriptionSource     = new HashMap<String, String>();
    private final Map<String, String>            context               = new HashMap<String, String>();
    private final Map<String, String>            docMethod             = new HashMap<String, String>();
    private final Map<String, String>            derivation            = new HashMap<String, String>();
    private final Map<String, String>            legalActs             = new HashMap<String, String>();

    private String                               conceptTypeIdentifier = null;
    private String                               conceptExtendsUrn     = null;
    private RepresentationsTsv.RepresentationEum representationType    = null;
    private String                               representationValue   = null;

    public Map<String, String> getPluralName() {
        return pluralName;
    }

    public Map<String, String> getAcronym() {
        return acronym;
    }

    public Map<String, String> getDescriptionSource() {
        return descriptionSource;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public Map<String, String> getDocMethod() {
        return docMethod;
    }

    public Map<String, String> getDerivation() {
        return derivation;
    }

    public Map<String, String> getLegalActs() {
        return legalActs;
    }

    public String getConceptTypeIdentifier() {
        return conceptTypeIdentifier;
    }

    public void setConceptTypeIdentifier(String conceptTypeIdentifier) {
        this.conceptTypeIdentifier = conceptTypeIdentifier;
    }

    public String getConceptExtendsUrn() {
        return conceptExtendsUrn;
    }

    public void setConceptExtendsUrn(String conceptExtendsUrn) {
        this.conceptExtendsUrn = conceptExtendsUrn;
    }

    public RepresentationsTsv.RepresentationEum getRepresentationType() {
        return representationType;
    }

    public void setRepresentationType(RepresentationsTsv.RepresentationEum representationType) {
        this.representationType = representationType;
    }

    public String getRepresentationValue() {
        return representationValue;
    }

    public void setRepresentationValue(String representationValue) {
        this.representationValue = representationValue;
    }
}
