package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;

public class ConceptDS extends ItemDS {

    // IDENTIFIERS
    public static final String PLURAL_NAME           = "con-plu-name";
    public static final String ACRONYM               = "con-acron";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION_SOURCE    = "con-desc-source";
    public static final String CONTEXT               = "con-context";
    public static final String DOC_METHOD            = "con-doc-method";
    public static final String VARIABLE              = "con-var";
    // CLASS DESCRIPTORS
    public static final String SDMX_RELATED_ARTEFACT = "con-sdmx-art";
    public static final String TYPE                  = "con-type";
    public static final String ROLES                 = "con-roles";
    // PRODUCTION DESCRIPTORS
    public static final String DERIVATION            = "con-derivation";
    // RELATION WITH CONCEPS
    public static final String EXTENDS               = "con-extends";
    public static final String EXTENDS_VIEW          = "con-extends-view";
    public static final String RELATED_CONCEPTS      = "con-related-concepts";
    // LEGAL ACTS
    public static final String LEGAL_ACTS            = "con-legal-acts";

    public static final String CONCEPT_SCHEME_TYPE   = "con-sch-type";        // Not mapped in DTO
}
