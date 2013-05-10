package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Common
    public static final CommonServiceExceptionType CONFIGURATION_PROPERTY_NOT_FOUND                                     = create("exception.srm.configuration.property_not_found");
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS                                         = create("exception.srm.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType ITEM_SCHEME_WITHOUT_ITEMS                                            = create("exception.srm.item_scheme.without_items");
    public static final CommonServiceExceptionType ITEM_WITH_INCORRECT_METADATA                                         = create("exception.srm.item_scheme.item_with_incorrect_metadata");
    public static final CommonServiceExceptionType STRUCTURE_MODIFICATIONS_NOT_SUPPORTED                                = create("exception.srm.structure_modifications.operation_not_supported");
    public static final CommonServiceExceptionType MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED = create("exception.srm.maintainable_artefact.versioning_not_supported.version_unpublished_found");

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE                                            = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND                                               = create("exception.srm.concepts.concept_type.not_found");
    public static final CommonServiceExceptionType CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE        = create("exception.srm.concepts.concept.enumerated_representation.codelist_different_variable");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE                                       = create("exception.srm.organisations.organisation_scheme.wrong_type");
    public static final CommonServiceExceptionType MAINTAINER_MUST_BE_DEFAULT                                           = create("exception.srm.organisations.maintainer.not_default");
    public static final CommonServiceExceptionType ORGANISATION_TYPE_AGENCY_DELETING_NOT_SUPPORTED                      = create("exception.srm.organisations.delete_agency_not_supported");
    public static final CommonServiceExceptionType ORGANISATION_TYPE_AGENCY_UPDATE_CODE_NOT_SUPPORTED                   = create("exception.srm.organisations.update_agency_code_not_supported");

    // Codelists
    public static final CommonServiceExceptionType VARIABLE_ONLY_IN_ONE_FAMILY                                          = create("exception.srm.codelists.variable.variable_only_one_family");
    public static final CommonServiceExceptionType VARIABLE_WITH_RELATIONS                                              = create("exception.srm.codelists.variable.with_relations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_RELATIONS                                      = create("exception.srm.codelists.variable_element.with_relations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_OPERATIONS                                     = create("exception.srm.codelists.variable_element.with_operations");
    public static final CommonServiceExceptionType CODELIST_ALPHABETICAL_ORDER_OPERATION_NOT_SUPPORTED                  = create("exception.srm.codelists.order_visualisation.alphabetical.operation_not_supported");
    public static final CommonServiceExceptionType CODELIST_ORDER_VISUALISATION_MAXIMUM_REACHED                         = create("exception.srm.codelists.order_visualisation.maximum_reached");
    public static final CommonServiceExceptionType CODELIST_ALL_EXPANDED_OPENNESS_VISUALISATION_OPERATION_NOT_SUPPORTED = create("exception.srm.codelists.openness_visualisation.all_expanded.operation_not_supported");
    public static final CommonServiceExceptionType CODELIST_OPENNESS_VISUALISATION_MAXIMUM_REACHED                      = create("exception.srm.codelists.openness_visualisation.maximum_reached");

    // Variable elements
    public static final CommonServiceExceptionType VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE                       = create("exception.srm.codelists.variable_element.must_belong_to_same_variable");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_MUST_HAVE_VALID_TO_FILLED                           = create("exception.srm.codelists.variable_element.valid_to_required");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_OPERATION_NOT_FOUND                                 = create("exception.srm.codelists.variable_element.operation.not_found");

    // Data Structure Definitions
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_OCCURRENCE                = create("exception.srm.dsd.validation.stubheading.occurence");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_INCOMPLETE                = create("exception.srm.dsd.validation.stubheading.incomplete");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_SHOWDECIMALS                               = create("exception.srm.dsd.validation.showdecimals");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_SHOWDECIMALS_PRECISION                     = create("exception.srm.dsd.validation.showdecimals_precision");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_MEASUREDIM_SPECIAL_ATTR            = create("exception.srm.dsd.validation.measure_dimension.special_attr.not_found");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_SPATIALDIM_SPECIAL_ATTR            = create("exception.srm.dsd.validation.spatial_dimension.special_attr.not_found");
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WITHOUT_TIMEDIM_SPECIAL_ATTR               = create("exception.srm.dsd.validation.time_dimension.special_attr.not_found");

    // Codes miscellaneous
    public static final CommonServiceExceptionType ARTEFACT_CAN_NOT_REPLACE_ITSELF                                      = create("exception.srm.codelists.artefact.cannot_replace_itself");
    public static final CommonServiceExceptionType ARTEFACT_IS_ALREADY_REPLACED                                         = create("exception.srm.codelists.artefact.already_replaced");
    public static final CommonServiceExceptionType CODES_VARIABLE_ELEMENTS_NORMALISATION_ERROR                          = create("exception.srm.codelists.error.variable_elements_normalisation");

    // Importation
    public static final CommonServiceExceptionType IMPORTATION_EXIST_NOT_FINAL_VERSION                                  = create("exception.srm.importation.error.there_are_not_final_versions");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR                                                = create("exception.srm.importation.csv.error");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_FILE_PARSING                                   = create("exception.srm.importation.csv.error.file_parse");
    public static final CommonServiceExceptionType IMPORTATION_CSV_HEADER_INCORRECT                                     = create("exception.srm.importation.csv.error.header.incorrect");
    public static final CommonServiceExceptionType IMPORTATION_CSV_HEADER_INCORRECT_COLUMN                              = create("exception.srm.importation.csv.error.header.incorrect_column");
    public static final CommonServiceExceptionType IMPORTATION_CSV_LINE_INCORRECT                                       = create("exception.srm.importation.csv.error.line.incorrect");
    public static final CommonServiceExceptionType IMPORTATION_CSV_METADATA_REQUIRED                                    = create("exception.srm.importation.csv.error.metadata_required");
    public static final CommonServiceExceptionType IMPORTATION_CSV_METADATA_INCORRECT                                   = create("exception.srm.importation.csv.error.metadata_incorrect");
    public static final CommonServiceExceptionType IMPORTATION_CSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER               = create("exception.srm.importation.csv.error.metadata_incorrect_semantic_identifier");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_PARENT_NOT_FOUND                               = create("exception.srm.importation.csv.error.parent.not_found");
    public static final CommonServiceExceptionType IMPORTATION_CSV_INFO_RESOURCE_NOT_UPDATED                            = create("info.srm.importation.csv.resource_not_updated");
    public static final CommonServiceExceptionType IMPORTATION_CSV_INFO_RESOURCE_UPDATED                                = create("info.srm.importation.csv.resource_updated");
    public static final CommonServiceExceptionType IMPORTATION_CSV_INFO_VARIABLE_ELEMENT_NOT_FOUND                      = create("info.srm.importation.csv.error.variable_element.not_found");
    public static final CommonServiceExceptionType IMPORTATION_CSV_RESOURCE_DUPLICATED                                  = create("exception.srm.importation.csv.error.resource_duplicated");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_ORDER_VISUALISATION_NOT_FOUND                  = create("exception.srm.importation.csv.error.order_visualisation.not_found");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED       = create("exception.srm.importation.csv.error.order_visualisation.alphabetical_not_supported");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_CODE_NOT_FOUND                                 = create("exception.srm.importation.csv.error.code.not_found");
    public static final CommonServiceExceptionType IMPORTATION_CSV_ERROR_INCORRECT_NUMBER_CODES                         = create("exception.srm.importation.csv.error.incorrect_number_codes");

}
