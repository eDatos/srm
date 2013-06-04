package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;

public class ConceptDS extends ItemDS {

    // IDENTIFIERS
    public static final String PLURAL_NAME                           = "con-plu-name";
    public static final String ACRONYM                               = "con-acron";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION_SOURCE                    = "con-desc-source";
    public static final String CONTEXT                               = "con-context";
    public static final String DOC_METHOD                            = "con-doc-method";
    public static final String VARIABLE                              = "con-var";
    // CLASS DESCRIPTORS
    public static final String SDMX_RELATED_ARTEFACT                 = "con-sdmx-art";
    public static final String TYPE                                  = "con-type";
    public static final String ROLES                                 = "con-roles";
    // PRODUCTION DESCRIPTORS
    public static final String DERIVATION                            = "con-derivation";
    // RELATION WITH CONCEPS
    public static final String EXTENDS                               = "con-extends";
    public static final String RELATED_CONCEPTS                      = "con-related-concepts";
    // LEGAL ACTS
    public static final String LEGAL_ACTS                            = "con-legal-acts";

    // QUANTITY
    public static String       QUANTITY_UNIT_UUID                    = "q-unit-uuid";
    public static String       QUANTITY_UNIT_MULTIPLIER              = "q-unit-mul";
    public static String       QUANTITY_SIGNIFICANT_DIGITS           = "q-sig-dig";
    public static String       QUANTITY_DECIMAL_PLACES               = "q-dec";
    public static String       QUANTITY_MINIMUM                      = "q-min";
    public static String       QUANTITY_MAXIMUM                      = "q-max";
    public static String       QUANTITY_DENOMINATOR_INDICATOR_UUID   = "q-den";
    public static String       QUANTITY_DENOMINATOR_INDICATOR_TEXT   = "q-den-dtext";         // Not mapped in DTO
    public static String       QUANTITY_NUMERATOR_INDICATOR_UUID     = "q-num";
    public static String       QUANTITY_NUMERATOR_INDICATOR_TEXT     = "q-num-dtext";         // Not mapped in DTO

    public static String       QUANTITY_IS_PERCENTAGE                = "q-is-perc";
    public static String       QUANTITY_IS_PERCENTAGE_TEXT           = "q-is-perc-text";      // Not mapped in DTO

    public static String       QUANTITY_INDEX_BASE_TYPE              = "q-base-type";         // Not mapped in DTO
    public static String       QUANTITY_BASE_VALUE                   = "q-value";
    public static String       QUANTITY_BASE_TIME                    = "q-time";
    public static String       QUANTITY_BASE_LOCATION                = "q-loc";
    public static String       QUANTITY_BASE_QUANTITY_INDICATOR_UUID = "q-ind-uuid";
    public static String       QUANTITY_BASE_QUANTITY_INDICATOR_TEXT = "q-ind-uuid-dtext";    // Not mapped in DTO
    public static String       QUANTITY_TYPE                         = "q-type";
    public static String       QUANTITY_TYPE_TEXT                    = "q-type-text";         // Not mapped in DTO
    public static String       QUANTITY_PERCENTAGE_OF                = "q-perc-of";

    public static final String CONCEPT_SCHEME_TYPE                   = "con-sch-type";        // Not mapped in DTO
}
