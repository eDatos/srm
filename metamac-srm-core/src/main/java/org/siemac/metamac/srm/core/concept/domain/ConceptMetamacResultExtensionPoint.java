package org.siemac.metamac.srm.core.concept.domain;

import java.util.HashMap;
import java.util.Map;

public class ConceptMetamacResultExtensionPoint {

    private final Map<String, String> pluralName        = new HashMap<String, String>();
    private final Map<String, String> acronym           = new HashMap<String, String>();
    private final Map<String, String> descriptionSource = new HashMap<String, String>();
    private final Map<String, String> context           = new HashMap<String, String>();
    private final Map<String, String> docMethod         = new HashMap<String, String>();
    private final Map<String, String> derivation        = new HashMap<String, String>();
    private final Map<String, String> legalActs         = new HashMap<String, String>();

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

}
