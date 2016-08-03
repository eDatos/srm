package org.siemac.metamac.srm.web.client.utils;

import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.AGENCY_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.ATTRIBUTE_COMPONENT_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CATEGORY_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CATEGORY_SCHEME_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CODELIST_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CODE_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CONCEPT_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.CONCEPT_SCHEME_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.DATA_STRUCTURE_DEFINITION_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.DIMENSION_COMPONENT_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.GROUP_DESCRIPTOR_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.ORGANISATION_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.ORGANISATION_SCHEME_CODE_PATTERN;
import static com.arte.statistic.sdmx.srm.core.constants.SemanticIdentifierConstants.PRIMARY_MEASURE_COMPONENT_CODE_PATTERN;
import static org.siemac.metamac.srm.core.constants.SemanticIdentifierConstants.CODELIST_FAMILY_CODE_PATTERN;
import static org.siemac.metamac.srm.core.constants.SemanticIdentifierConstants.CODELIST_VISUALISATION_CODE_PATTERN;
import static org.siemac.metamac.srm.core.constants.SemanticIdentifierConstants.VARIABLE_CODE_PATTERN;
import static org.siemac.metamac.srm.core.constants.SemanticIdentifierConstants.VARIABLE_ELEMENT_CODE_PATTERN;
import static org.siemac.metamac.srm.core.constants.SemanticIdentifierConstants.VARIABLE_FAMILY_CODE_PATTERN;

import org.siemac.metamac.web.common.client.MetamacWebCommon;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

public class SemanticIdentifiersUtils {

    // -------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------

    // DSD

    private static boolean isDsdIdentifier(String value) {
        return value != null ? value.matches(DATA_STRUCTURE_DEFINITION_CODE_PATTERN) : false;
    }

    public static CustomValidator getDsdIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isDsdIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Primary measure

    private static boolean isPrimaryMeasureIdentifier(String value) {
        return value != null ? value.matches(PRIMARY_MEASURE_COMPONENT_CODE_PATTERN) : false;
    }

    public static CustomValidator getPrimaryMeasureIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isPrimaryMeasureIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Dimension

    private static boolean isDimensionIdentifier(String value) {
        return value != null ? value.matches(DIMENSION_COMPONENT_CODE_PATTERN) : false;
    }

    public static CustomValidator getDimensionIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isDimensionIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Attribute

    private static boolean isAttributeIdentifier(String value) {
        return value != null ? value.matches(ATTRIBUTE_COMPONENT_CODE_PATTERN) : false;
    }

    public static CustomValidator getAttributeIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isAttributeIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Group descriptor

    private static boolean isGroupDescriptorIdentifier(String value) {
        return value != null ? value.matches(GROUP_DESCRIPTOR_CODE_PATTERN) : false;
    }

    public static CustomValidator getGroupDescriptorIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isGroupDescriptorIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // -------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------

    // Concept scheme

    private static boolean isConceptSchemeIdentifier(String value) {
        return value != null ? value.matches(CONCEPT_SCHEME_CODE_PATTERN) : false;
    }

    public static CustomValidator getConceptSchemeIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isConceptSchemeIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Concept

    private static boolean isConceptIdentifier(String value) {
        return value != null ? value.matches(CONCEPT_CODE_PATTERN) : false;
    }

    public static CustomValidator getConceptIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isConceptIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // -------------------------------------------------------------------------------
    // ORGANISATIONS
    // -------------------------------------------------------------------------------

    // Organisation scheme

    private static boolean isOrganisationSchemeIdentifier(String value) {
        return value != null ? value.matches(ORGANISATION_SCHEME_CODE_PATTERN) : false;
    }

    public static CustomValidator getOrganisationSchemeIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isOrganisationSchemeIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Agency

    private static boolean isAgencyIdentifier(String value) {
        return value != null ? value.matches(AGENCY_CODE_PATTERN) : false;
    }

    public static CustomValidator getAgencyIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isAgencyIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Organisation

    private static boolean isOrganisationIdentifier(String value) {
        return value != null ? value.matches(ORGANISATION_CODE_PATTERN) : false;
    }

    public static CustomValidator getOrganisationIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isOrganisationIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // -------------------------------------------------------------------------------
    // CATEGORIES
    // -------------------------------------------------------------------------------

    // Category scheme

    private static boolean isCategorySchemeIdentifier(String value) {
        return value != null ? value.matches(CATEGORY_SCHEME_CODE_PATTERN) : false;
    }

    public static CustomValidator getCategorySchemeIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCategorySchemeIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Category

    private static boolean isCategoryIdentifier(String value) {
        return value != null ? value.matches(CATEGORY_CODE_PATTERN) : false;
    }

    public static CustomValidator getCategoryIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCategoryIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // -------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------

    // Codelist

    private static boolean isCodelistIdentifier(String value) {
        return value != null ? value.matches(CODELIST_CODE_PATTERN) : false;
    }

    public static CustomValidator getCodelistIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCodelistIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Code

    private static boolean isCodeIdentifier(String value) {
        return value != null ? value.matches(CODE_CODE_PATTERN) : false;
    }

    public static CustomValidator getCodeIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCodeIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Codelist Family

    private static boolean isCodelistFamilyIdentifier(String value) {
        return value != null ? value.matches(CODELIST_FAMILY_CODE_PATTERN) : false;
    }

    public static CustomValidator getCodelistFamilyIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCodelistFamilyIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Variable Family

    private static boolean isVariableFamilyIdentifier(String value) {
        return value != null ? value.matches(VARIABLE_FAMILY_CODE_PATTERN) : false;
    }

    public static CustomValidator getVariableFamilyIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isVariableFamilyIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Variable

    private static boolean isVariableIdentifier(String value) {
        return value != null ? value.matches(VARIABLE_CODE_PATTERN) : false;
    }

    public static CustomValidator getVariableIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isVariableIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Variable

    private static boolean isVariableElementIdentifier(String value) {
        return value != null ? value.matches(VARIABLE_ELEMENT_CODE_PATTERN) : false;
    }

    public static CustomValidator getVariableElementIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isVariableElementIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }

    // Visualisation

    private static boolean isCodelistVisualisationIdentifier(String value) {
        return value != null ? value.matches(CODELIST_VISUALISATION_CODE_PATTERN) : false;
    }

    public static CustomValidator getCodelistVisualisationIdentifierCustomValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return value != null ? isCodelistVisualisationIdentifier(value.toString()) : false;
            }
        };
        validator.setErrorMessage(MetamacWebCommon.getMessages().errorSemanticIdentifierFormat());
        return validator;
    }
}
