package org.siemac.metamac.srm.core.common.error;

public class ServiceExceptionParameters extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters {

    private static final String PREFIX_PARAMETER_SRM                                       = "parameter.srm";

    public static final String  IDENTIFIER                                                 = createCode("identifier");
    public static final String  LOCALE                                                     = createCode("locale");

    // CONCEPT_SCHEME
    private static final String CONCEPT_SCHEME_PREFIX                                      = "concept_scheme";
    // public static final String CONCEPT_SCHEME_CODE = CONCEPT_SCHEME + ".code";
    public static final String  CONCEPT_SCHEME_RELATED_OPERATION                           = createCode(CONCEPT_SCHEME_PREFIX, "related_operation");
    public static final String  CONCEPT_SCHEME_RELATED_OPERATION_TITLE                     = createCode(CONCEPT_SCHEME_PREFIX, "related_operation.title");
    public static final String  CONCEPT_SCHEME_IS_TYPE_UPDATED                             = createCode(CONCEPT_SCHEME_PREFIX, "is_type_updated");
    public static final String  CONCEPT_SCHEME_TYPE                                        = createCode(CONCEPT_SCHEME_PREFIX, "type");
    public static final String  CONCEPT_SCHEME_TYPE_GLOSSARY                               = createCode(CONCEPT_SCHEME_PREFIX, "type.glossary");
    public static final String  CONCEPT_SCHEME_TYPE_ROLE                                   = createCode(CONCEPT_SCHEME_PREFIX, "type.role");
    public static final String  CONCEPT_SCHEME_TYPE_OPERATION                              = createCode(CONCEPT_SCHEME_PREFIX, "type.operation");
    public static final String  CONCEPT_SCHEME_TYPE_TRANSVERSAL                            = createCode(CONCEPT_SCHEME_PREFIX, "type.transversal");
    public static final String  CONCEPT_SCHEME_TYPE_MEASURE                                = createCode(CONCEPT_SCHEME_PREFIX, "type.measure");

    // CONCEPT
    private static final String CONCEPT_PREFIX                                             = "concept";
    public static final String  CONCEPT_PLURAL_NAME                                        = createCode(CONCEPT_PREFIX, "plural_name");
    public static final String  CONCEPT_ACRONYM                                            = createCode(CONCEPT_PREFIX, "acronym");
    public static final String  CONCEPT_DESCRIPTION_SOURCE                                 = createCode(CONCEPT_PREFIX, "description_source");
    public static final String  CONCEPT_CONTEXT                                            = createCode(CONCEPT_PREFIX, "context");
    public static final String  CONCEPT_DOC_METHOD                                         = createCode(CONCEPT_PREFIX, "doc_method");
    public static final String  CONCEPT_SDMX_RELATED_ARTEFACT                              = createCode(CONCEPT_PREFIX, "sdmx_related_artefact");
    public static final String  CONCEPT_DERIVATION                                         = createCode(CONCEPT_PREFIX, "derivation");
    public static final String  CONCEPT_LEGAL_ACTS                                         = createCode(CONCEPT_PREFIX, "legal_acts");
    public static final String  CONCEPT_EXTENDS                                            = createCode(CONCEPT_PREFIX, "extends");
    public static final String  CONCEPT_ROLE                                               = createCode(CONCEPT_PREFIX, "role");
    public static final String  CONCEPT_VARIABLE                                           = createCode(CONCEPT_PREFIX, "variable");
    // CONCEPT QUANTITY

    public static final String  CONCEPT_QUANTITY                                           = createCode(CONCEPT_PREFIX, "quantity");
    public static final String  CONCEPT_QUANTITY_TYPE                                      = createCode(CONCEPT_PREFIX, "quantity.type");
    public static final String  CONCEPT_QUANTITY_UNIT_CODE                                 = createCode(CONCEPT_PREFIX, "quantity.unit_code");
    public static final String  CONCEPT_QUANTITY_UNIT_SYMBOL_POSITION                      = createCode(CONCEPT_PREFIX, "quantity.unit_symbol_position");
    public static final String  CONCEPT_QUANTITY_SIGNIFICANT_DIGITS                        = createCode(CONCEPT_PREFIX, "quantity.significant_digits");
    public static final String  CONCEPT_QUANTITY_DECIMAL_PLACES                            = createCode(CONCEPT_PREFIX, "quantity.decimal_places");
    public static final String  CONCEPT_QUANTITY_UNIT_MULTIPLIER                           = createCode(CONCEPT_PREFIX, "quantity.unit_multiplier");
    public static final String  CONCEPT_QUANTITY_MIN                                       = createCode(CONCEPT_PREFIX, "quantity.min");
    public static final String  CONCEPT_QUANTITY_MAX                                       = createCode(CONCEPT_PREFIX, "quantity.max");
    public static final String  CONCEPT_QUANTITY_NUMERATOR                                 = createCode(CONCEPT_PREFIX, "quantity.numerator");
    public static final String  CONCEPT_QUANTITY_DENOMINATOR                               = createCode(CONCEPT_PREFIX, "quantity.denominator");
    public static final String  CONCEPT_QUANTITY_IS_PERCENTAGE                             = createCode(CONCEPT_PREFIX, "quantity.is_percentage");
    public static final String  CONCEPT_QUANTITY_PERCENTAGE_OF                             = createCode(CONCEPT_PREFIX, "quantity.percentage_of");
    public static final String  CONCEPT_QUANTITY_BASE_VALUE                                = createCode(CONCEPT_PREFIX, "quantity.base_value");
    public static final String  CONCEPT_QUANTITY_BASE_TIME                                 = createCode(CONCEPT_PREFIX, "quantity.base_time");
    public static final String  CONCEPT_QUANTITY_BASE_LOCATION                             = createCode(CONCEPT_PREFIX, "quantity.base_location");
    public static final String  CONCEPT_QUANTITY_BASE_QUANTITY                             = createCode(CONCEPT_PREFIX, "quantity.base_quantity");

    // CODELIST
    private static final String CODELIST_PREFIX                                            = "codelist";
    public static final String  CODELIST_SHORT_NAME                                        = createCode(CODELIST_PREFIX, "short_name");
    public static final String  CODELIST_ACCESS_TYPE                                       = createCode(CODELIST_PREFIX, "access_type");
    public static final String  CODELIST_DEFAULT_ORDER_VISUALISATION                       = createCode(CODELIST_PREFIX, "default_order_visualisation");
    public static final String  CODELIST_DEFAULT_OPENNESS_VISUALISATION                    = createCode(CODELIST_PREFIX, "default_openness_visualisation");
    public static final String  CODELIST_VARIABLE                                          = createCode(CODELIST_PREFIX, "variable");
    public static final String  CODELIST_IS_VARIABLE_UPDATED                               = createCode(CODELIST_PREFIX, "is_variable_updated");
    public static final String  CODELIST_DESCRIPTION_SOURCE                                = createCode(CODELIST_PREFIX, "description_source");
    public static final String  CODELIST_REPLACE_TO                                        = createCode(CODELIST_PREFIX, "replace_to");

    // CODELIST VISUALISATION
    public static final String  CODELIST_ORDER_VISUALISATION                               = createCode(CODELIST_PREFIX, "codelist_order_visualisation");
    public static final String  CODELIST_ORDER_VISUALISATION_INDEX                         = createCode(CODELIST_PREFIX, "codelist_order_visualisation.index");
    public static final String  CODELIST_OPENNESS_VISUALISATION                            = createCode(CODELIST_PREFIX, "codelist_openness_visualisation");
    public static final String  CODELIST_OPENNESS_VISUALISATION_EXPANDED                   = createCode(CODELIST_PREFIX, "codelist_openness_visualisation.expanded");

    // CODELIST FAMILY
    public static final String  CODELIST_FAMILY                                            = createCode("codelist_family");

    // CODE
    private static final String CODE_PREFIX                                                = "code";
    public static final String  CODE_SHORT_NAME                                            = createCode(CODE_PREFIX, "short_name");
    public static final String  CODE_VARIABLE_ELEMENT                                      = createCode(CODE_PREFIX, "variable_element");

    // VARIABLE FAMILY
    public static final String  VARIABLE_FAMILY                                            = createCode("variable_family");

    // VARIABLE
    private static final String VARIABLE_PREFIX                                            = "variable";
    public static final String  VARIABLE                                                   = createCode(VARIABLE_PREFIX);
    public static final String  VARIABLE_SHORT_NAME                                        = createCode(VARIABLE_PREFIX, "short_name");
    public static final String  VARIABLE_VALID_FROM                                        = createCode(VARIABLE_PREFIX, "valid_from");
    public static final String  VARIABLE_VALID_TO                                          = createCode(VARIABLE_PREFIX, "valid_to");
    public static final String  VARIABLE_TYPE                                              = createCode(VARIABLE_PREFIX, "type");

    // VARIABLE ELEMENT
    private static final String VARIABLE_ELEMENT_PREFIX                                    = "variable_element";
    public static final String  VARIABLE_ELEMENT                                           = createCode(VARIABLE_ELEMENT_PREFIX);
    public static final String  VARIABLE_ELEMENT_SHORT_NAME                                = createCode(VARIABLE_ELEMENT_PREFIX, "short_name");
    public static final String  VARIABLE_ELEMENT_COMMENT                                   = createCode(VARIABLE_ELEMENT_PREFIX, "comment");
    public static final String  VARIABLE_ELEMENT_VALID_FROM                                = createCode(VARIABLE_ELEMENT_PREFIX, "valid_from");
    public static final String  VARIABLE_ELEMENT_VALID_TO                                  = createCode(VARIABLE_ELEMENT_PREFIX, "valid_to");
    public static final String  VARIABLE_ELEMENT_VARIABLE                                  = createCode(VARIABLE_ELEMENT_PREFIX, "variable");
    public static final String  VARIABLE_ELEMENT_LATITUDE                                  = createCode(VARIABLE_ELEMENT_PREFIX, "latitude");
    public static final String  VARIABLE_ELEMENT_LONGITUDE                                 = createCode(VARIABLE_ELEMENT_PREFIX, "longitude");
    public static final String  VARIABLE_ELEMENT_SHAPE                                     = createCode(VARIABLE_ELEMENT_PREFIX, "shape");
    public static final String  VARIABLE_ELEMENT_SHAPE_GEOJSON                             = createCode(VARIABLE_ELEMENT_PREFIX, "shape_geojson");
    public static final String  VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY                  = createCode(VARIABLE_ELEMENT_PREFIX, "geographical_granularity");

    // DSD
    private static final String DATA_STRUCTURE_DEFINITION_PREFIX                           = "data_structure_definition";
    public static final String  DATA_STRUCTURE_DEFINITION_RELATED_OPERATION                = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "related_operation");
    public static final String  DATA_STRUCTURE_DEFINITION_RELATED_OPERATION_TITLE          = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "related_operation.title");
    public static final String  DATA_STRUCTURE_DEFINITION_AUTOPEN                          = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "autopen");
    public static final String  DATA_STRUCTURE_DEFINITION_HEADING                          = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "heading");
    public static final String  DATA_STRUCTURE_DEFINITION_STUB                             = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "stub");
    public static final String  DATA_STRUCTURE_DEFINITION_SHOW_DEC_PREC                    = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "show_decimals_precision");
    public static final String  DATA_STRUCTURE_DEFINITION_DIMENSION_REPRESENTATION_UPDATED = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "dimension.is_representation_updated");
    public static final String  DATA_STRUCTURE_DEFINITION_DIMENSION_CONCEPTID_UPDATED      = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "dimension.is_conceptid_updated");
    public static final String  DATA_STRUCTURE_DEFINITION_STATISTICAL_OPERATION            = createCode(DATA_STRUCTURE_DEFINITION_PREFIX, "statistical_operation");

    // LIFECYCLE
    private static final String PROC_STATUS_PREFIX                                         = "proc_status";
    public static final String  PROC_STATUS_DRAFT                                          = createCode(PROC_STATUS_PREFIX, "draft");
    public static final String  PROC_STATUS_PRODUCTION_VALIDATION                          = createCode(PROC_STATUS_PREFIX, "production_validation");
    public static final String  PROC_STATUS_DIFFUSION_VALIDATION                           = createCode(PROC_STATUS_PREFIX, "diffusion_validation");
    public static final String  PROC_STATUS_VALIDATION_REJECTED                            = createCode(PROC_STATUS_PREFIX, "validation_rejected");
    public static final String  PROC_STATUS_INTERNALLY_PUBLISHED                           = createCode(PROC_STATUS_PREFIX, "internally_published");
    public static final String  PROC_STATUS_EXTERNALLY_PUBLISHED                           = createCode(PROC_STATUS_PREFIX, "externally_published");

    // IMPORTATION
    private static final String IMPORTATION_PREFIX                                         = "importation";
    public static final String  IMPORTATION_TSV_UPDATE_ALREADY_EXISTING                    = createCode(IMPORTATION_PREFIX, "update_already_existing");
    // Note: name of columns must be identical to SrmContants.TSV_HEADER_*
    public static final String  IMPORTATION_TSV_COLUMN_CODE                                = createCode(IMPORTATION_PREFIX, "code");
    public static final String  IMPORTATION_TSV_COLUMN_PARENT                              = createCode(IMPORTATION_PREFIX, "parent");
    public static final String  IMPORTATION_TSV_COLUMN_VARIABLE_ELEMENT                    = createCode(IMPORTATION_PREFIX, "variable_element");
    public static final String  IMPORTATION_TSV_COLUMN_SHORT_NAME                          = createCode(IMPORTATION_PREFIX, "short_name");
    public static final String  IMPORTATION_TSV_COLUMN_NAME                                = createCode(IMPORTATION_PREFIX, "name");
    public static final String  IMPORTATION_TSV_COLUMN_DESCRIPTION                         = createCode(IMPORTATION_PREFIX, "description");
    public static final String  IMPORTATION_TSV_COLUMN_ORDER                               = createCode(IMPORTATION_PREFIX, "order");
    public static final String  IMPORTATION_TSV_COLUMN_GEOGRAPHICAL_GRANULARITY            = createCode(IMPORTATION_PREFIX, "geographical_granularity");

    // MISC
    public static final String  STREAM                                                     = createCode("stream");
    public static final String  FILE_URL                                                   = createCode("file_url");
    public static final String  FILE_NAME                                                  = createCode("file_name");
    public static final String  QUERY_SELECTION                                            = createCode("query_selection");
    // MISC internal
    public static final String  CHARSET                                                    = createCode("charset");
    public static final String  CAN_BE_BACKGROUND                                          = createCode("can_be_background");
    public static final String  INFORMATION_ITEMS                                          = createCode("information_items");

    private static String createCode(String fieldCode) {
        return PREFIX_PARAMETER_SRM + "." + fieldCode;
    }

    private static String createCode(String classCode, String fieldCode) {
        if (fieldCode.startsWith(".")) {
            return PREFIX_PARAMETER_SRM + "." + classCode + fieldCode;
        } else {
            return PREFIX_PARAMETER_SRM + "." + classCode + "." + fieldCode;
        }
    }

    public static String createCodeImportation(String fieldCode) {
        return createCode(IMPORTATION_PREFIX, fieldCode);
    }
}
