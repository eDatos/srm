package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Common
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS                                                       = create("exception.srm.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType ITEM_SCHEME_WITHOUT_ITEMS                                                          = create("exception.srm.item_scheme.without_items");
    public static final CommonServiceExceptionType STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED                                     = create(
            "exception.srm.structure_modifications.operation_not_supported.imported");
    public static final CommonServiceExceptionType STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_MAINTAINER_IS_NOT_DEFAULT_NOR_SDMX           = create(
            "exception.srm.structure_modifications.operation_not_supported.maintainer_not_default_nor_sdmx");
    public static final CommonServiceExceptionType MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED               = create(
            "exception.srm.maintainable_artefact.versioning_not_supported.version_unpublished_found");
    public static final CommonServiceExceptionType ARTEFACT_CAN_NOT_REPLACE_ITSELF                                                    = create("exception.srm.artefact.cannot_replace_itself");
    public static final CommonServiceExceptionType ARTEFACT_IS_ALREADY_REPLACED                                                       = create("exception.srm.artefact.already_replaced");
    public static final CommonServiceExceptionType METAMAC_RESERVED_KEYWORD                                                           = create("exception.srm.reserved_keyword");

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE                                                          = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND                                                             = create("exception.srm.concepts.concept_type.not_found");
    public static final CommonServiceExceptionType CONCEPT_DELETE_NOT_SUPPORTED_CONCEPT_IN_QUANTITY                                   = create(
            "exception.srm.concepts.delete_not_supported.concept_in_quantity");
    public static final CommonServiceExceptionType CONCEPT_NOT_EXTERNALLY_PUBLISHED                                                   = create(
            "exception.srm.concepts.concept_not_externally_published");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_NOT_EXTERNALLY_PUBLISHED                                            = create(
            "exception.srm.concepts.concept_scheme_not_externally_published");
    public static final CommonServiceExceptionType CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_VARIABLE_REQUIRED                       = create(
            "exception.srm.concepts.concept.enumerated_representation_codelist.variable_required");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE                                                     = create(
            "exception.srm.organisations.organisation_scheme.wrong_type");
    public static final CommonServiceExceptionType MAINTAINER_MUST_BE_DEFAULT                                                         = create("exception.srm.organisations.maintainer.not_default");
    public static final CommonServiceExceptionType ORGANISATION_DELETING_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED         = create(
            "exception.srm.organisations.delete_not_supported.belonged_to_organisation_scheme_was_published");
    public static final CommonServiceExceptionType ORGANISATION_UPDATE_CODE_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED      = create(
            "exception.srm.organisations.update_code_not_supported.belonged_to_organisation_scheme_was_published");
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_NOT_EXTERNALLY_PUBLISHED                                       = create(
            "exception.srm.organisations.organisation_scheme_not_externally_published");

    // Codelists
    public static final CommonServiceExceptionType VARIABLE_ONLY_IN_ONE_FAMILY                                                        = create(
            "exception.srm.codelists.variable.variable_only_one_family");
    public static final CommonServiceExceptionType VARIABLE_WITH_RELATIONS                                                            = create("exception.srm.codelists.variable.with_relations");
    public static final CommonServiceExceptionType VARIABLE_WORLD_OPERATION_NOT_SUPPORTED                                             = create(
            "exception.srm.codelists.variable.operation_unsupported_variable_world");
    public static final CommonServiceExceptionType VARIABLE_MUST_BE_GEOGRAPHICAL                                                      = create("exception.srm.codelists.variable.must_be_geographical");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_RELATIONS                                                    = create(
            "exception.srm.codelists.variable_element.with_relations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_OPERATIONS                                                   = create(
            "exception.srm.codelists.variable_element.with_operations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WORLD_OPERATION_NOT_SUPPORTED                                     = create(
            "exception.srm.codelists.variable_element.operation_unsupported_variable_element_world");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_OPERATION_VARIABLE_ELEMENT_IN_SOURCE_AND_TARGET                   = create(
            "exception.srm.codelists.variable_element_operation.variable_element_in_source_and_target");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_ALREADY_AS_SOURCE_IN_OPERATION                                    = create(
            "exception.srm.codelists.variable_element.already_as_source_in_operation");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_ALREADY_AS_TARGET_IN_OPERATION                                    = create(
            "exception.srm.codelists.variable_element.already_as_target_in_operation");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_RENDERING_COLOR_NOT_VALID                                         = create(
            "exception.srm.codelists.variable_element.rendering_color_not_valid");
    public static final CommonServiceExceptionType CODELIST_ALPHABETICAL_ORDER_OPERATION_NOT_SUPPORTED                                = create(
            "exception.srm.codelists.order_visualisation.alphabetical.operation_not_supported");
    public static final CommonServiceExceptionType CODELIST_ORDER_VISUALISATION_MAXIMUM_REACHED                                       = create(
            "exception.srm.codelists.order_visualisation.maximum_reached");
    public static final CommonServiceExceptionType CODELIST_ORDER_VISUALISATION_NOT_FOUND                                             = create("exception.srm.codelists.order_visualisation.not_found");
    public static final CommonServiceExceptionType CODELIST_ORDER_VISUALISATION_DELETING_NOT_SUPPORTED_CODELIST_WAS_EVER_PUBLISHED    = create(
            "exception.srm.codelists.order_visualisation.delete_not_supported.belonged_to_codelist_was_published");
    public static final CommonServiceExceptionType CODELIST_ALL_EXPANDED_OPENNESS_VISUALISATION_OPERATION_NOT_SUPPORTED               = create(
            "exception.srm.codelists.openness_visualisation.all_expanded.operation_not_supported");
    public static final CommonServiceExceptionType CODELIST_OPENNESS_VISUALISATION_MAXIMUM_REACHED                                    = create(
            "exception.srm.codelists.openness_visualisation.maximum_reached");
    public static final CommonServiceExceptionType CODELIST_OPENNESS_VISUALISATION_NOT_FOUND                                          = create(
            "exception.srm.codelists.openness_visualisation.not_found");
    public static final CommonServiceExceptionType CODELIST_OPENNESS_VISUALISATION_DELETING_NOT_SUPPORTED_CODELIST_WAS_EVER_PUBLISHED = create(
            "exception.srm.codelists.openness_visualisation.delete_not_supported.belonged_to_codelist_was_published");
    public static final CommonServiceExceptionType CODE_NOT_EXTERNALLY_PUBLISHED                                                      = create("exception.srm.codelists.code_not_externally_published");
    public static final CommonServiceExceptionType CODELIST_NOT_EXTERNALLY_PUBLISHED                                                  = create(
            "exception.srm.codelists.codelist_not_externally_published");
    public static final CommonServiceExceptionType CODE_VARIABLE_ELEMENT_REQUIRED_WHEN_GEOGRAPHICAL                                   = create(
            "exception.srm.codelists.code_variable_element_required_when_geographical");
    public static final CommonServiceExceptionType CODELIST_UNSUPPORTED_TO_GEOGRAPHICAL_GRANULARITY                                   = create(
            "exception.srm.codelists.codelist_unsupported_to_geographical_granularity");

    // Variables
    public static final CommonServiceExceptionType VARIABLE_TYPE_UPDATE_TO_GEOGRAPHICAL_UNSUPPORTED                                   = create(
            "exception.srm.codelists.variable.update_type_geographical_unsupported");

    // Variable elements
    public static final CommonServiceExceptionType VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE                                     = create(
            "exception.srm.codelists.variable_element.must_belong_to_same_variable");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_MUST_HAVE_VALID_TO_FILLED                                         = create(
            "exception.srm.codelists.variable_element.valid_to_required");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_OPERATION_NOT_FOUND                                               = create(
            "exception.srm.codelists.variable_element.operation.not_found");

    // Data Structure Definitions
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_OCCURRENCE                              = create("exception.srm.dsd.validation.stubheading.occurence");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_INCOMPLETE                              = create("exception.srm.dsd.validation.stubheading.incomplete");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_SHOWDECIMALS                                             = create("exception.srm.dsd.validation.showdecimals");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_SHOWDECIMALS_PRECISION                                   = create("exception.srm.dsd.validation.showdecimals_precision");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_MEASUREDIM_SPECIAL_ATTR                          = create(
            "exception.srm.dsd.validation.measure_dimension.special_attr.not_found");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_SPATIALDIM_SPECIAL_ATTR                          = create(
            "exception.srm.dsd.validation.spatial_dimension.special_attr.not_found");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_TIMEDIM_SPECIAL_ATTR                             = create(
            "exception.srm.dsd.validation.time_dimension.special_attr.not_found");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_DIM_REPRESENTATION_INHERITED_INVALID                     = create(
            "exception.srm.dsd.validation.dimension.representation.inherited.invalid");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_DIM_REPRESENTATION_ENUM_REQUIRED                         = create(
            "exception.srm.dsd.validation.dimension.representation.enumerated.required");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_INVALID                              = create(
            "exception.srm.dsd.validation.attribute.representation.inherited.invalid");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_ENUM_REQUIRED                        = create(
            "exception.srm.dsd.validation.attribute.representation.enumerated.required");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_ATTR_REPRESENTATION_NONENUM_REQUIRED                     = create(
            "exception.srm.dsd.validation.attribute.representation.nonenumerated.required");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_ATTR_RELATETO_INVALID                                    = create(
            "exception.srm.dsd.validation.attribute.relateto.invalid");

    // Categories
    public static final CommonServiceExceptionType CATEGORY_NOT_EXTERNALLY_PUBLISHED                                                  = create(
            "exception.srm.categories.category_not_externally_published");

    // Codes miscellaneous
    public static final CommonServiceExceptionType CODES_VARIABLE_ELEMENTS_NORMALISATION_ERROR                                        = create(
            "exception.srm.codelists.error.variable_elements_normalisation");

    // Importation and exportation
    public static final CommonServiceExceptionType IMPORTATION_EXIST_NOT_FINAL_VERSION                                                = create(
            "exception.srm.importation.error.there_are_not_final_versions");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR                                                              = create("exception.srm.importation.tsv.error");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_FILE_PARSING                                                 = create("exception.srm.importation.tsv.error.file_parse");
    public static final CommonServiceExceptionType IMPORTATION_TSV_HEADER_INCORRECT                                                   = create("exception.srm.importation.tsv.error.header.incorrect");
    public static final CommonServiceExceptionType IMPORTATION_TSV_HEADER_INCORRECT_COLUMN                                            = create(
            "exception.srm.importation.tsv.error.header.incorrect_column");
    public static final CommonServiceExceptionType IMPORTATION_TSV_HEADER_COLUMN_REQUIRED                                             = create(
            "exception.srm.importation.tsv.error.header.column_required");
    public static final CommonServiceExceptionType IMPORTATION_TSV_HEADER_COLUMN_UNEXPECTED                                           = create(
            "exception.srm.importation.tsv.error.header.column_unexpected");
    public static final CommonServiceExceptionType IMPORTATION_TSV_LINE_INCORRECT                                                     = create("exception.srm.importation.tsv.error.line.incorrect");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_REQUIRED                                                  = create("exception.srm.importation.tsv.error.metadata_required");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_INCORRECT                                                 = create(
            "exception.srm.importation.tsv.error.metadata_incorrect");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_UNEXPECTED                                                = create(
            "exception.srm.importation.tsv.error.metadata_unexpected");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER                             = create(
            "exception.srm.importation.tsv.error.metadata_incorrect_semantic_identifier");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_UNEXPECTED_PARENT                                         = create(
            "exception.srm.importation.tsv.error.metadata_unexpected_parent");
    public static final CommonServiceExceptionType IMPORTATION_TSV_METADATA_UNEXPECTED_SDMX_RELATED_ARTEFACT                          = create(
            "exception.srm.importation.tsv.error.metadata_unexpected_smdx_related_artefact");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND                                             = create("exception.srm.importation.tsv.error.parent.not_found");
    public static final CommonServiceExceptionType IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED                                          = create("info.srm.importation.tsv.resource_not_updated");
    public static final CommonServiceExceptionType IMPORTATION_TSV_INFO_RESOURCE_UPDATED                                              = create("info.srm.importation.tsv.resource_updated");
    public static final CommonServiceExceptionType IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND                                    = create(
            "info.srm.importation.tsv.error.variable_element.not_found");
    public static final CommonServiceExceptionType IMPORTATION_TSV_RESOURCE_DUPLICATED                                                = create(
            "exception.srm.importation.tsv.error.resource_duplicated");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_ORDER_VISUALISATION_NOT_FOUND                                = create(
            "exception.srm.importation.tsv.error.order_visualisation.not_found");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED                     = create(
            "exception.srm.importation.tsv.error.order_visualisation.alphabetical_not_supported");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_CODE_NOT_FOUND                                               = create("exception.srm.importation.tsv.error.code.not_found");
    public static final CommonServiceExceptionType IMPORTATION_TSV_ERROR_INCORRECT_NUMBER_CODES                                       = create(
            "exception.srm.importation.tsv.error.incorrect_number_codes");
    public static final CommonServiceExceptionType IMPORTATION_SHAPE_ERROR                                                            = create("exception.srm.importation.shape.error");
    public static final CommonServiceExceptionType IMPORTATION_SHAPE_ERROR_FILE_PARSING                                               = create("exception.srm.importation.shape.error.file_parse");
    public static final CommonServiceExceptionType EXPORTATION_TSV_ERROR                                                              = create("exception.srm.exportation.tsv.error");

    // Notices
    public static final CommonServiceExceptionType IMPORT_TSV_JOB_ERROR                                                               = create("notice_message.srm.exception.import_tsv_job.fails");
    public static final CommonServiceExceptionType IMPORT_TSV_JOB_ERROR_AND_CANT_MARK_AS_ERROR                                        = create(
            "notice_message.srm.exception.import_tsv_job.fails_and_cant_mark_as_error");
    public static final CommonServiceExceptionType IMPORT_SHAPE_JOB_ERROR                                                             = create("notice_message.srm.exception.import_shape_job.fails");
    public static final CommonServiceExceptionType IMPORT_SHAPE_JOB_ERROR_AND_CANT_MARK_AS_ERROR                                      = create(
            "notice_message.srm.exception.import_shape_job.fails_and_cant_mark_as_error");
    public static final CommonServiceExceptionType IMPORT_SHAPE_JOB_ERROR_MALFORMED_URL                                               = create(
            "notice_message.srm.exception.import_shape_job.fails_malformed_shapefile_url");
    public static final CommonServiceExceptionType PUBLISH_INTERNALLY_CODELIST_JOB_ERROR                                              = create(
            "notice_message.srm.exception.publish_internally_codelist_job.fails");
    public static final CommonServiceExceptionType PUBLISH_INTERNALLY_CODELIST_JOB_ERROR_AND_CANT_MARK_AS_ERROR                       = create(
            "notice_message.srm.exception.publish_internally_codelist_job.fails_and_cant_mark_as_error");

    // Kafka
    public static final CommonServiceExceptionType UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER                         = create("stream_message.srm.exception.send_message.fails");
    public static final CommonServiceExceptionType STREAM_MESSAGING_MISSING_MANDATORY_SETTINGS                                        = create("stream_message.srm.exception.config.missing_settings");

}
