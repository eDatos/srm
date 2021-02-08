package org.siemac.metamac.srm.core.constants;

import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;

public class SrmConstants {

    public static final String             APPLICATION_ID                                                         = "GESTOR_RECURSOS_ESTRUCTURALES";

    // Attribute name used in repository, is a metamac reserved keyword
    public static String                   ATTRIBUTE_DATA_SOURCE_ID                                               = "DATA_SOURCE_ID";

    // Artefacts
    public static final VersionPatternEnum VERSION_PATTERN_METAMAC                                                = VersionPatternEnum.XX_YYY;

    // Codelists
    public static final int                CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER                            = 20;
    public static final String             CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE                         = "ALPHABETICAL";
    public static final Integer            CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX                 = Integer.valueOf(1);
    public static final int                CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER                         = 20;
    public static final String             CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE                      = "ALL_EXPANDED";
    public static final Integer            CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_COLUMN_INDEX              = Integer.valueOf(1);
    public static final Boolean            CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE                          = Boolean.TRUE;

    // Importation and exportation
    public static final String             TSV_SEPARATOR                                                          = "\t";
    public static final String             TSV_LINE_SEPARATOR                                                     = "\n";
    public static final String             TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR                              = "#";
    public static final String             TSV_HEADER_CODE                                                        = "code";
    public static final String             TSV_HEADER_PARENT                                                      = "parent";
    public static final String             TSV_HEADER_NAME                                                        = "name";
    public static final String             TSV_HEADER_DESCRIPTION                                                 = "description";
    public static final String             TSV_HEADER_COMMENT                                                     = "comment";
    public static final String             TSV_HEADER_SHORT_NAME                                                  = "short_name";
    public static final String             TSV_HEADER_VARIABLE_ELEMENT                                            = "variable_element";
    public static final String             TSV_HEADER_LABEL                                                       = "label";
    public static final String             TSV_HEADER_LEVEL                                                       = "level";
    public static final String             TSV_HEADER_ORDER                                                       = "order";
    public static final String             TSV_HEADER_GEOGRAPHICAL_GRANULARITY                                    = "geographical_granularity";
    public static final String             TSV_HEADER_PLURAL_NAME                                                 = "plural_name";
    public static final String             TSV_HEADER_ACRONYM                                                     = "acronym";
    public static final String             TSV_HEADER_DESCRIPTION_SOURCE                                          = "description_source";
    public static final String             TSV_HEADER_CONTEXT                                                     = "context";
    public static final String             TSV_HEADER_DOC_METHOD                                                  = "docMethod";
    public static final String             TSV_HEADER_DERIVATION                                                  = "derivation";
    public static final String             TSV_HEADER_LEGAL_ACTS                                                  = "legal_acts";
    public static final String             TSV_HEADER_CONCEPT_TYPE                                                = "concept_type";
    public static final String             TSV_HEADER_REPRESENTATION                                              = "representation";
    public static final String             TSV_HEADER_REPRESENTATION_SUB_TYPE                                     = "type";
    public static final String             TSV_HEADER_REPRESENTATION_SUB_VALUE                                    = "value";
    public static final String             TSV_HEADER_CONCEPT_EXTENDS                                             = "concept_extends";
    public static final String             TSV_HEADER_SDMX_RELATED_ARTEFACT                                                   = "sdmx_role";

    public static final String             TSV_EXPORTATION_ENCODING                                               = "UTF-8";

    public static final String             SHAPE_VARIABLE_ELEMENT_ATTRIBUTE                                       = "VAR_ELEM";
    public static final String             SHAPE_POLYGON                                                          = "POLYGON";
    public static final String             SHAPE_MULTIPOLYGON                                                     = "MULTIPOLYGON";
    public static final String             SHAPE_POINT                                                            = "POINT";
    public static final String             SHAPE_OPERATION_IMPORT_SHAPES                                          = "importShapes";
    public static final String             SHAPE_OPERATION_IMPORT_POINTS                                          = "importPoints";

    // Tasks
    public static long                     NUM_BYTES_TO_PLANNIFY_TSV_ITEMS_IMPORTATION                            = 250000;
    public static long                     NUM_BYTES_TO_PLANNIFY_TSV_CODES_ORDERS_IMPORTATION                     = 50000;
    public static long                     NUM_BYTES_TO_PLANNIFY_TSV_VARIABLE_ELEMENTS_IMPORTATION                = 150000;
    public static long                     NUM_BYTES_TO_PLANNIFY_TSV_VARIABLE_ELEMENTS_GEO_IMPORTATION            = 1000000;

    // Misc
    public static final int                METADATA_SHORT_NAME_MAXIMUM_LENGTH                                     = 100;
    public static final int                CODE_QUERY_COLUMN_ORDER_LENGTH                                         = 6;
    public static final int                CONCEPT_QUERY_COLUMN_ORDER_LENGTH                                      = 6;
    public static final String             TABLE_INTERNATIONAL_STRINGS                                            = "TB_INTERNATIONAL_STRINGS";
    public static final String             MISC_VALUE_VARIABLE_ELEMENT_GEOGRAPHICAL_INFORMATION_LAST_UPDATED_DATE = "variable_element.geographical_information.last_updated_date";
}
