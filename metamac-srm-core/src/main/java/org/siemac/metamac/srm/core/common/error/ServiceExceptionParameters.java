package org.siemac.metamac.srm.core.common.error;

public class ServiceExceptionParameters {

    public static final String URN                                             = "urn";

    // MANTAINER
    public static final String MANTAINER                                       = "mantainer";
    public static final String MANTAINER_CODE                                  = MANTAINER + ".code";
    public static final String MANTAINER_URN                                   = MANTAINER + ".urn";
    public static final String MANTAINER_TYPE                                  = MANTAINER + ".type";
    public static final String MANTAINER_URI                                   = MANTAINER + ".uri";
    public static final String MANTAINER_MANAGEMENT_APP_URL                    = MANTAINER + ".management_app_url";
    public static final String MANTAINER_TITLE                                 = MANTAINER + ".title";

    // ITEM_SCHEME
    public static final String ITEM_SCHEME                                     = "item_scheme";
    public static final String ITEM_SCHEME_ITEMS                               = "item_scheme.items";

    // CONCEPT_SCHEME
    public static final String CONCEPT_SCHEME                                  = "concept_scheme";
    public static final String CONCEPT_SCHEME_TYPE                             = "concept_scheme.type";
    public static final String CONCEPT_SCHEME_RELATED_OPERATION                = "concept_scheme.related_operation";

    // CONCEPT
    public static final String CONCEPT                                         = "concept";

    // ANNOTATION
    public static final String ANNOTATION                                      = "annotation";
    public static final String ANNOTATION_CODE                                 = ANNOTATION + ".code";
    public static final String ANNOTATION_TITLE                                = ANNOTATION + ".title";
    public static final String ANNOTATION_TYPE                                 = ANNOTATION + ".type";
    public static final String ANNOTATION_URL                                  = ANNOTATION + ".url";
    public static final String ANNOTATION_TEXT                                 = ANNOTATION + ".text";

    // ANNOTABLE_ARTEFACT
    public static final String ANNOTABLE_ARTEFACT                              = "annotable_artefact";

    // IDENTIFIABLE_ARTEFACT
    public static final String IDENTIFIABLE_ARTEFACT                           = "identifiable_artefact";
    public static final String IDENTIFIABLE_ARTEFACT_CODE                      = IDENTIFIABLE_ARTEFACT + ".code";
    public static final String IDENTIFIABLE_ARTEFACT_URI                       = IDENTIFIABLE_ARTEFACT + ".uri";

    // MAINTAINABLE_ARTEFACT
    public static final String MAINTAINABLE_ARTEFACT                           = "maintainable_artefact";
    public static final String MAINTAINABLE_ARTEFACT_VERSION_LOGIC             = MAINTAINABLE_ARTEFACT + ".version";
    public static final String MAINTAINABLE_ARTEFACT_VALID_FROM                = MAINTAINABLE_ARTEFACT + ".validFrom";
    public static final String MAINTAINABLE_ARTEFACT_VALID_TO                  = MAINTAINABLE_ARTEFACT + ".validTo";
    public static final String MAINTAINABLE_ARTEFACT_FINAL_LOGIC               = MAINTAINABLE_ARTEFACT + ".final";
    public static final String MAINTAINABLE_ARTEFACT_PROC_STATUS_DRAFT         = MAINTAINABLE_ARTEFACT + ".proc_status.draft";

    // NAMEABLE
    public static final String NAMEABLE_ARTEFACT                               = "nameable_artefact";
    public static final String NAMEABLE_ARTEFACT_NAME                          = NAMEABLE_ARTEFACT + ".name";
    public static final String NAMEABLE_ARTEFACT_DESCRIPTION                   = NAMEABLE_ARTEFACT + ".description";

    // COMPONENT
    public static final String COMPONENT                                       = "component";
    public static final String COMPONENT_CONCEPT_ID_REF                        = COMPONENT + ".concept_id_ref";
    public static final String COMPONENT_LOCAL_REPRESENTATION                  = COMPONENT + ".local_representation";
    public static final String COMPONENT_TYPE                                  = COMPONENT + ".type";
    public static final String COMPONENT_TYPE_DATA_ATTRIBUTE                   = COMPONENT + ".type.data_attribute";
    public static final String COMPONENT_TYPE_DIMENSION_COMPONENT              = COMPONENT + ".type.dimension_component";
    public static final String COMPONENT_TYPE_PRIMARY_MEASURE                  = COMPONENT + ".type.primary_measure";
    public static final String COMPONENT_TYPE_TARGET_OBJECT                    = COMPONENT + ".type.target_object";
    public static final String COMPONENT_TYPE_METADATA_ATTRIBUTE               = COMPONENT + ".type.metadata_attribute";

    public static final String COMPONENT_REPRESENTATION                        = COMPONENT + ServiceExceptionParametersInternal.REPRESENTATION;
    public static final String COMPONENT_REPRESENTATION_ENUMERATED             = COMPONENT + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED;
    public static final String COMPONENT_REPRESENTATION_NON_ENUMERATED         = COMPONENT + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED;

    // DIMENSION
    public static final String DIMENSION                                       = "dimension";
    public static final String DIMENSION_ROLE                                  = DIMENSION + ".role";

    public static final String DIMENSION_FACET_IS_SEQUENCE_FT                  = DIMENSION + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT;
    public static final String DIMENSION_FACET_INTERVAL_FT                     = DIMENSION + ServiceExceptionParametersInternal.FACET_INTERVAL_FT;
    public static final String DIMENSION_FACET_START_VALUE_FT                  = DIMENSION + ServiceExceptionParametersInternal.FACET_START_VALUE_FT;
    public static final String DIMENSION_FACET_END_VALUE_FT                    = DIMENSION + ServiceExceptionParametersInternal.FACET_END_VALUE_FT;
    public static final String DIMENSION_FACET_TIME_INTERVAL_FT                = DIMENSION + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT;
    public static final String DIMENSION_FACET_START_TIME_FT                   = DIMENSION + ServiceExceptionParametersInternal.FACET_START_TIME_FT;
    public static final String DIMENSION_FACET_END_TIME_FT                     = DIMENSION + ServiceExceptionParametersInternal.FACET_END_TIME_FT;
    public static final String DIMENSION_FACET_MIN_LENGTH_FT                   = DIMENSION + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT;
    public static final String DIMENSION_FACET_MAX_LENGTH_FT                   = DIMENSION + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT;
    public static final String DIMENSION_FACET_MIN_VALUE_FT                    = DIMENSION + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT;
    public static final String DIMENSION_FACET_MAX_VALUE_FT                    = DIMENSION + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT;
    public static final String DIMENSION_FACET_DECIMALS_FT                     = DIMENSION + ServiceExceptionParametersInternal.FACET_DECIMALS_FT;
    public static final String DIMENSION_FACET_PATTERN_FT                      = DIMENSION + ServiceExceptionParametersInternal.FACET_PATTERN_FT;
    public static final String DIMENSION_FACET_FACET_XHTML_EFT                 = DIMENSION + ServiceExceptionParametersInternal.FACET_XHTML_EFT;
    public static final String DIMENSION_FACET_IS_MULTI_LINGUAL                = DIMENSION + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL;
    public static final String DIMENSION_FACET_VALUE_TYPE_ENUM                 = DIMENSION + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM;

    public static final String DIMENSION_REPRESENTATION                        = DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION;
    public static final String DIMENSION_REPRESENTATION_ENUMERATED             = DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED;
    public static final String DIMENSION_REPRESENTATION_NON_ENUMERATED         = DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED;

    // DATA_ATTRIBUTE
    public static final String DATA_ATTRIBUTE                                  = "data_attribute";
    public static final String DATA_ATTRIBUTE_USAGE_STATES                     = DATA_ATTRIBUTE + ".usage_states";
    public static final String DATA_ATTRIBUTE_RELATE_TO                        = DATA_ATTRIBUTE + ".relate_to";
    public static final String DATA_ATTRIBUTE_ROLE                             = DATA_ATTRIBUTE + ".role";

    public static final String DATA_ATTRIBUTE_FACET_IS_SEQUENCE_FT             = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT;
    public static final String DATA_ATTRIBUTE_FACET_INTERVAL_FT                = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_INTERVAL_FT;
    public static final String DATA_ATTRIBUTE_FACET_START_VALUE_FT             = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_START_VALUE_FT;
    public static final String DATA_ATTRIBUTE_FACET_END_VALUE_FT               = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_END_VALUE_FT;
    public static final String DATA_ATTRIBUTE_FACET_TIME_INTERVAL_FT           = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT;
    public static final String DATA_ATTRIBUTE_FACET_START_TIME_FT              = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_START_TIME_FT;
    public static final String DATA_ATTRIBUTE_FACET_END_TIME_FT                = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_END_TIME_FT;
    public static final String DATA_ATTRIBUTE_FACET_MIN_LENGTH_FT              = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT;
    public static final String DATA_ATTRIBUTE_FACET_MAX_LENGTH_FT              = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT;
    public static final String DATA_ATTRIBUTE_FACET_MIN_VALUE_FT               = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT;
    public static final String DATA_ATTRIBUTE_FACET_MAX_VALUE_FT               = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT;
    public static final String DATA_ATTRIBUTE_FACET_DECIMALS_FT                = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_DECIMALS_FT;
    public static final String DATA_ATTRIBUTE_FACET_PATTERN_FT                 = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_PATTERN_FT;
    public static final String DATA_ATTRIBUTE_FACET_FACET_XHTML_EFT            = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_XHTML_EFT;
    public static final String DATA_ATTRIBUTE_FACET_IS_MULTI_LINGUAL           = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL;
    public static final String DATA_ATTRIBUTE_FACET_VALUE_TYPE_ENUM            = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM;

    public static final String DATA_ATTRIBUTE_RELATE_TO_TYPE_DIMENSION         = DATA_ATTRIBUTE_RELATE_TO + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_DIMENSION;
    public static final String DATA_ATTRIBUTE_RELATE_TO_TYPE_GROUP             = DATA_ATTRIBUTE_RELATE_TO + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_GROUP;
    public static final String DATA_ATTRIBUTE_RELATE_TO_TYPE_NO_SPECIFIED      = DATA_ATTRIBUTE_RELATE_TO + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_NO_SPECIFIED;
    public static final String DATA_ATTRIBUTE_RELATE_TO_TYPE_PRIMARY_MEASURE   = DATA_ATTRIBUTE_RELATE_TO + ServiceExceptionParametersInternal.ATTRIBUTE_RELATIONSHIP_TYPE_PRIMARY_MEASURE;

    public static final String DATA_ATTRIBUTE_REPRESENTATION                   = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.REPRESENTATION;
    public static final String DATA_ATTRIBUTE_REPRESENTATION_ENUMERATED        = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED;
    public static final String DATA_ATTRIBUTE_REPRESENTATION_NON_ENUMERATED    = DATA_ATTRIBUTE + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED;

    // REPORTINGYEARSTARTDAY
    public static final String REPORTING_YEAR_START_DAY                        = "reporting_year_start_day";

    // MEASURE_DIMENSION
    public static final String MEASURE_DIMENSION                               = "measure_dimension";
    public static final String MEASURE_DIMENSION_ROLE                          = MEASURE_DIMENSION + ".role";

    public static final String MEASURE_DIMENSION_REPRESENTATION                = MEASURE_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION;
    public static final String MEASURE_DIMENSION_REPRESENTATION_ENUMERATED     = MEASURE_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED;
    public static final String MEASURE_DIMENSION_REPRESENTATION_NON_ENUMERATED = MEASURE_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED;

    // TIME_DIMENSION
    public static final String TIME_DIMENSION                                  = "time_dimension";

    public static final String TIME_DIMENSION_FACET_IS_SEQUENCE_FT             = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT;
    public static final String TIME_DIMENSION_FACET_INTERVAL_FT                = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_INTERVAL_FT;
    public static final String TIME_DIMENSION_FACET_START_VALUE_FT             = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_START_VALUE_FT;
    public static final String TIME_DIMENSION_FACET_END_VALUE_FT               = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_END_VALUE_FT;
    public static final String TIME_DIMENSION_FACET_TIME_INTERVAL_FT           = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT;
    public static final String TIME_DIMENSION_FACET_START_TIME_FT              = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_START_TIME_FT;
    public static final String TIME_DIMENSION_FACET_END_TIME_FT                = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_END_TIME_FT;
    public static final String TIME_DIMENSION_FACET_MIN_LENGTH_FT              = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT;
    public static final String TIME_DIMENSION_FACET_MAX_LENGTH_FT              = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT;
    public static final String TIME_DIMENSION_FACET_MIN_VALUE_FT               = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT;
    public static final String TIME_DIMENSION_FACET_MAX_VALUE_FT               = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT;
    public static final String TIME_DIMENSION_FACET_DECIMALS_FT                = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_DECIMALS_FT;
    public static final String TIME_DIMENSION_FACET_PATTERN_FT                 = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_PATTERN_FT;
    public static final String TIME_DIMENSION_FACET_FACET_XHTML_EFT            = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_XHTML_EFT;
    public static final String TIME_DIMENSION_FACET_IS_MULTI_LINGUAL           = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL;
    public static final String TIME_DIMENSION_FACET_VALUE_TYPE_ENUM            = TIME_DIMENSION + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM;

    public static final String TIME_DIMENSION_REPRESENTATION                   = TIME_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION;
    public static final String TIME_DIMENSION_REPRESENTATION_ENUMERATED        = TIME_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_ENUMERATED;
    public static final String TIME_DIMENSION_REPRESENTATION_NON_ENUMERATED    = TIME_DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION_NON_ENUMERATED;

    // PRIMARY_MEASURE
    public static final String PRIMARY_MEASURE                                 = "primary_measure";

    public static final String PRIMARY_MEASURE_FACET_IS_SEQUENCE_FT            = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_IS_SEQUENCE_FT;
    public static final String PRIMARY_MEASURE_FACET_INTERVAL_FT               = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_INTERVAL_FT;
    public static final String PRIMARY_MEASURE_FACET_START_VALUE_FT            = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_START_VALUE_FT;
    public static final String PRIMARY_MEASURE_FACET_END_VALUE_FT              = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_END_VALUE_FT;
    public static final String PRIMARY_MEASURE_FACET_TIME_INTERVAL_FT          = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_TIME_INTERVAL_FT;
    public static final String PRIMARY_MEASURE_FACET_START_TIME_FT             = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_START_TIME_FT;
    public static final String PRIMARY_MEASURE_FACET_END_TIME_FT               = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_END_TIME_FT;
    public static final String PRIMARY_MEASURE_FACET_MIN_LENGTH_FT             = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_MIN_LENGTH_FT;
    public static final String PRIMARY_MEASURE_FACET_MAX_LENGTH_FT             = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_MAX_LENGTH_FT;
    public static final String PRIMARY_MEASURE_FACET_MIN_VALUE_FT              = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_MIN_VALUE_FT;
    public static final String PRIMARY_MEASURE_FACET_MAX_VALUE_FT              = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_MAX_VALUE_FT;
    public static final String PRIMARY_MEASURE_FACET_DECIMALS_FT               = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_DECIMALS_FT;
    public static final String PRIMARY_MEASURE_FACET_PATTERN_FT                = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_PATTERN_FT;
    public static final String PRIMARY_MEASURE_FACET_FACET_XHTML_EFT           = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_XHTML_EFT;
    public static final String PRIMARY_MEASURE_FACET_IS_MULTI_LINGUAL          = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_IS_MULTI_LINGUAL;
    public static final String PRIMARY_MEASURE_FACET_VALUE_TYPE_ENUM           = PRIMARY_MEASURE + ServiceExceptionParametersInternal.FACET_VALUE_TYPE_ENUM;

    // DATASTRUCTUREDEFINITION
    public static final String DATA_STRUCTURE_DEFINITION                       = "data_structure_definition";
    public static final String DATA_STRUCTURE_DEFINITION_NAME                  = DATA_STRUCTURE_DEFINITION + ".name";
    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER            = DATA_STRUCTURE_DEFINITION + ".maintainer";
    public static final String DATA_STRUCTURE_DEFINITION_GROUPING              = DATA_STRUCTURE_DEFINITION + ".grouping";
    public static final String DATA_STRUCTURE_DEFINITION_CODE                  = DATA_STRUCTURE_DEFINITION + ".code";
    public static final String DATA_STRUCTURE_DEFINITION_URN                   = DATA_STRUCTURE_DEFINITION + ".urn";

    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER_CODE       = DATA_STRUCTURE_DEFINITION_MAINTAINER + ServiceExceptionParametersInternal.EXTERNAL_ITEM_CODE;
    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER_TITLE      = DATA_STRUCTURE_DEFINITION_MAINTAINER + ServiceExceptionParametersInternal.EXTERNAL_ITEM_TITLE;
    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER_URI        = DATA_STRUCTURE_DEFINITION_MAINTAINER + ServiceExceptionParametersInternal.EXTERNAL_ITEM_URI;
    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER_TYPE       = DATA_STRUCTURE_DEFINITION_MAINTAINER + ServiceExceptionParametersInternal.EXTERNAL_ITEM_TYPE;
    public static final String DATA_STRUCTURE_DEFINITION_MAINTAINER_URN        = DATA_STRUCTURE_DEFINITION_MAINTAINER + ServiceExceptionParametersInternal.EXTERNAL_ITEM_URN;

    // FACET
    public static final String FACET                                           = "facet";

    // COMPONENT
    public static final String COMPONENT_LIST                                  = "component_list";
    public static final String COMPONENT_LIST_ATTRIBUTE_DESCRIPTOR             = COMPONENT_LIST + ".attribute_descriptor";
    public static final String COMPONENT_LIST_DIMENSION_DESCRIPTOR             = COMPONENT_LIST + ".dimension_descriptor";
    public static final String COMPONENT_LIST_GROUP_DIMENSION_DESCRIPTOR       = COMPONENT_LIST + ".group_dimension_descriptor";
    public static final String COMPONENT_LIST_MEASURE_DESCRIPTOR               = COMPONENT_LIST + ".measure_descriptor";
}
