package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class ServiceExceptionParameters extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters {

    public static final String IDENTIFIER                                                         = "identifier";

    // CONCEPT_SCHEME
    public static final String CONCEPT_SCHEME_CODE                                                = CONCEPT_SCHEME + ".code";
    public static final String CONCEPT_SCHEME_TYPE                                                = CONCEPT_SCHEME + ".type";
    public static final String CONCEPT_SCHEME_RELATED_OPERATION                                   = CONCEPT_SCHEME + ".related_operation";
    // TYPES
    public static final String CONCEPT_SCHEME_IS_TYPE_UPDATED                                     = CONCEPT_SCHEME + ".is_type_updated";
    public static final String CONCEPT_SCHEME_TYPE_GLOSSARY                                       = ConceptSchemeTypeEnum.GLOSSARY.name();
    public static final String CONCEPT_SCHEME_TYPE_ROLE                                           = ConceptSchemeTypeEnum.ROLE.name();
    public static final String CONCEPT_SCHEME_TYPE_OPERATION                                      = ConceptSchemeTypeEnum.OPERATION.name();
    public static final String CONCEPT_SCHEME_TYPE_TRANSVERSAL                                    = ConceptSchemeTypeEnum.TRANSVERSAL.name();
    public static final String CONCEPT_SCHEME_TYPE_MEASURE                                        = ConceptSchemeTypeEnum.MEASURE.name();

    // CONCEPT
    public static final String CONCEPT_PLURAL_NAME                                                = CONCEPT + ".plural_name";
    public static final String CONCEPT_ACRONYM                                                    = CONCEPT + ".acronym";
    public static final String CONCEPT_DESCRIPTION_SOURCE                                         = CONCEPT + ".description_source";
    public static final String CONCEPT_CONTEXT                                                    = CONCEPT + ".context";
    public static final String CONCEPT_DOC_METHOD                                                 = CONCEPT + ".doc_method";
    public static final String CONCEPT_SDMX_RELATED_ARTEFACT                                      = CONCEPT + ".sdmx_related_artefact";
    public static final String CONCEPT_DERIVATION                                                 = CONCEPT + ".derivation";
    public static final String CONCEPT_LEGAL_ACTS                                                 = CONCEPT + ".legal_acts";
    public static final String CONCEPT_EXTENDS                                                    = CONCEPT + ".extends";
    public static final String CONCEPT_VARIABLE                                                   = CONCEPT + ".variable";

    // CODELIST
    public static final String CODELIST_SHORT_NAME                                                = CODELIST + ".short_name";
    public static final String CODELIST_ACCESS_TYPE                                               = CODELIST + ".access_type";
    public static final String CODELIST_DEFAULT_ORDER_VISUALISATION                               = CODELIST + ".default_order_visualisation";
    public static final String CODELIST_VARIABLE                                                  = CODELIST + ".variable";
    public static final String CODELIST_IS_VARIABLE_UPDATED                                       = CODELIST + ".is_variable_updated";

    // CODELIST VISUALISATION
    public static final String CODELIST_ORDER_VISUALISATION                                       = "codelist_order_visualisation";
    public static final String CODELIST_ORDER_VISUALISATION_CODES                                 = CODELIST_ORDER_VISUALISATION + ".codes";
    public static final String CODE_ORDER_VISUALISATION                                           = "code_order_visualisation";
    public static final String CODE_ORDER_VISUALISATION_INDEX                                     = CODE_ORDER_VISUALISATION + ".index";

    // CODELIST FAMILY
    public static final String CODELIST_FAMILY                                                    = "codelist_family";

    // CODE
    public static final String CODE_SHORT_NAME                                                    = CODE + ".short_name";
    public static final String CODE_VARIABLE_ELEMENT                                              = CODE + ".variable_element";

    // VARIABLE FAMILY
    public static final String VARIABLE_FAMILY                                                    = "variable_family";

    // VARIABLE
    public static final String VARIABLE                                                           = "variable";
    public static final String VARIABLE_SHORT_NAME                                                = VARIABLE + ".short_name";
    public static final String VARIABLE_VALID_FROM                                                = VARIABLE + ".valid_from";
    public static final String VARIABLE_VALID_TO                                                  = VARIABLE + ".valid_to";

    // VARIABLE ELEMENT
    public static final String VARIABLE_ELEMENT                                                   = "variable_element";
    public static final String VARIABLE_ELEMENT_SHORT_NAME                                        = VARIABLE_ELEMENT + ".short_name";
    public static final String VARIABLE_ELEMENT_VALID_FROM                                        = VARIABLE_ELEMENT + ".valid_from";
    public static final String VARIABLE_ELEMENT_VALID_TO                                          = VARIABLE_ELEMENT + ".valid_to";
    public static final String VARIABLE_ELEMENT_VARIABLE                                          = VARIABLE_ELEMENT + ".variable";

    // DSD VISUALISATION
    public static final String DATA_STRUCTURE_DEFINITION_AUTOPEN                                  = DATA_STRUCTURE_DEFINITION + ".autopen";
    public static final String DATA_STRUCTURE_DEFINITION_HEADING                                  = DATA_STRUCTURE_DEFINITION + ".heading";
    public static final String DATA_STRUCTURE_DEFINITION_STUB                                     = DATA_STRUCTURE_DEFINITION + ".stub";
    public static final String DATA_STRUCTURE_DEFINITION_SHOW_DEC_PREC                            = DATA_STRUCTURE_DEFINITION + ".show_decimals_precision";
    public static final String DATA_STRUCTURE_DEFINITION_MEASURE_DIMENSION_REPRESENTATION_UPDATED = DATA_STRUCTURE_DEFINITION + ".measure_dimension.is_representation_updated";
    public static final String DATA_STRUCTURE_DEFINITION_STATISTICAL_OPERATION                    = DATA_STRUCTURE_DEFINITION + ".statistical_operation";

    // LIFECYCLE
    public static final String PROC_STATUS_DRAFT                                                  = ProcStatusEnum.DRAFT.name();
    public static final String PROC_STATUS_PRODUCTION_VALIDATION                                  = ProcStatusEnum.PRODUCTION_VALIDATION.name();
    public static final String PROC_STATUS_DIFFUSION_VALIDATION                                   = ProcStatusEnum.DIFFUSION_VALIDATION.name();
    public static final String PROC_STATUS_VALIDATION_REJECTED                                    = ProcStatusEnum.VALIDATION_REJECTED.name();
    public static final String PROC_STATUS_INTERNALLY_PUBLISHED                                   = ProcStatusEnum.INTERNALLY_PUBLISHED.name();
    public static final String PROC_STATUS_EXTERNALLY_PUBLISHED                                   = ProcStatusEnum.EXTERNALLY_PUBLISHED.name();
}
