package org.siemac.metamac.srm.core.common.service.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceimpl.utils.OrganisationsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoriesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationConceptsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationItemsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationOrganisationsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.InternationalStringTsv;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class TsvImportationUtils {

    private static Logger logger = LoggerFactory.getLogger(TsvImportationUtils.class);

    // ---------------------------------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationVariableElementsTsvHeader parseTsvHeaderToImportVariableElements(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY, SrmConstants.TSV_HEADER_SHORT_NAME);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationVariableElementsTsvHeader header = new ImportationVariableElementsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];
            String headerExpected = headersExpected.get(headerExpectedIndex);

            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY.equals(columnName)) {
                header.setGeographicalGranularityPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
                if (header.getShortName() == null) {
                    break;
                }
                if (header.getShortName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            }
            if (headersExpected.size() <= headerExpectedIndex) {
                break;
            }
        }

        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationOrganisationsTsvHeader parseTsvHeaderToImportOrganisations(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        return (ImportationOrganisationsTsvHeader) parseTsvHeaderToImportItems(line, exceptions, new ImportationOrganisationsTsvHeader());
    }

    /**
     * Transforms TSV line to {@link OrganisationMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static OrganisationMetamac tsvLineToOrganisation(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean updateAlreadyExisting,
            Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems)
            throws MetamacException {
        return (OrganisationMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, updateAlreadyExisting, itemsPreviousInItemScheme, itemsToPersistByCode, exceptionItems, infoItems,
                getOrganisationType((OrganisationSchemeVersionMetamac) itemSchemeVersion));
    }

    private static TypeExternalArtefactsEnum getOrganisationType(OrganisationSchemeVersionMetamac organisationSchemeVersionMetamac) {
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.AGENCY;
        } else if (OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.DATA_CONSUMER;
        } else if (OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.DATA_PROVIDER;
        } else if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.ORGANISATION_UNIT;
        } else {
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationConceptsTsvHeader parseTsvHeaderToImportConcepts(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION,
                SrmConstants.TSV_HEADER_COMMENT, SrmConstants.TSV_HEADER_PLURAL_NAME, SrmConstants.TSV_HEADER_ACRONYM, SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE, SrmConstants.TSV_HEADER_CONTEXT,
                SrmConstants.TSV_HEADER_DOC_METHOD, SrmConstants.TSV_HEADER_DERIVATION, SrmConstants.TSV_HEADER_LEGAL_ACTS, SrmConstants.TSV_HEADER_CONCEPT_TYPE,
                SrmConstants.TSV_HEADER_REPRESENTATION, SrmConstants.TSV_HEADER_CONCEPT_EXTENDS);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationConceptsTsvHeader header = new ImportationConceptsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_PLURAL_NAME.equals(columnName)) {
                header.setPluralName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getPluralName(), exceptions));
                if (header.getPluralName() == null) {
                    break;
                }
                if (header.getPluralName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_ACRONYM.equals(columnName)) {
                header.setAcronym(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getAcronym(), exceptions));
                if (header.getAcronym() == null) {
                    break;
                }
                if (header.getAcronym().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE.equals(columnName)) {
                header.setDescriptionSource(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescriptionSource(), exceptions));
                if (header.getDescriptionSource() == null) {
                    break;
                }
                if (header.getDescriptionSource().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONTEXT.equals(columnName)) {
                header.setContext(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getContext(), exceptions));
                if (header.getContext() == null) {
                    break;
                }
                if (header.getContext().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DOC_METHOD.equals(columnName)) {
                header.setDocMethod(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDocMethod(), exceptions));
                if (header.getDocMethod() == null) {
                    break;
                }
                if (header.getDocMethod().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DERIVATION.equals(columnName)) {
                header.setDerivation(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDerivation(), exceptions));
                if (header.getDerivation() == null) {
                    break;
                }
                if (header.getDerivation().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_LEGAL_ACTS.equals(columnName)) {
                header.setLegalActs(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getLegalActs(), exceptions));
                if (header.getLegalActs() == null) {
                    break;
                }
                if (header.getLegalActs().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONCEPT_TYPE.equals(columnName)) {
                header.setConceptTypePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_REPRESENTATION.equals(columnName)) {
                header.setRepresentation(columnHeaderToRepresentationTsv(headerColumns, i, columnSplited, headerExpected, header.getRepresentation(), exceptions));
                if (header.getRepresentation().isRepresentationHeaderComplete()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONCEPT_EXTENDS.equals(columnName)) {
                header.setConceptExtendsPosition(i);
                headerExpectedIndex++;
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    /**
     * Transforms TSV line to {@link ConceptMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static ConceptMetamac tsvLineToConcept(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean updateAlreadyExisting,
            Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems)
            throws MetamacException {
        ConceptMetamac conceptMetamac = (ConceptMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, updateAlreadyExisting, itemsPreviousInItemScheme, itemsToPersistByCode,
                exceptionItems, infoItems, TypeExternalArtefactsEnum.CONCEPT);

        if (conceptMetamac == null) {
            return null;
        }

        ImportationConceptsTsvHeader conceptTsvHeader = (ImportationConceptsTsvHeader) header;

        conceptMetamac.setPluralName(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getPluralName(), columns, conceptMetamac.getPluralName()));
        conceptMetamac.setAcronym(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getAcronym(), columns, conceptMetamac.getAcronym()));
        conceptMetamac.setDescriptionSource(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getDescriptionSource(), columns, conceptMetamac.getDescriptionSource()));
        conceptMetamac.setContext(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getContext(), columns, conceptMetamac.getContext()));
        conceptMetamac.setDocMethod(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getDocMethod(), columns, conceptMetamac.getDocMethod()));
        conceptMetamac.setDerivation(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getDerivation(), columns, conceptMetamac.getDerivation()));
        conceptMetamac.setLegalActs(TsvImportationUtils.tsvLineToInternationalString(conceptTsvHeader.getLegalActs(), columns, conceptMetamac.getLegalActs()));

        return conceptMetamac;
    }
    // ---------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationCategoriesTsvHeader parseTsvHeaderToImportCategories(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        return (ImportationCategoriesTsvHeader) parseTsvHeaderToImportItems(line, exceptions, new ImportationCategoriesTsvHeader());
    }

    /**
     * Transforms TSV line to {@link CategoryMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static CategoryMetamac tsvLineToCategory(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean updateAlreadyExisting,
            Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems)
            throws MetamacException {
        return (CategoryMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, updateAlreadyExisting, itemsPreviousInItemScheme, itemsToPersistByCode, exceptionItems, infoItems,
                TypeExternalArtefactsEnum.CATEGORY);
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationCodesTsvHeader parseTsvHeaderToImportCodes(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_VARIABLE_ELEMENT, SrmConstants.TSV_HEADER_NAME,
                SrmConstants.TSV_HEADER_DESCRIPTION, SrmConstants.TSV_HEADER_COMMENT, SrmConstants.TSV_HEADER_SHORT_NAME);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationCodesTsvHeader header = new ImportationCodesTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_VARIABLE_ELEMENT.equals(columnName)) {
                header.setVariableElementPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
                if (header.getShortName() == null) {
                    break;
                }
                if (header.getShortName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    /**
     * Transforms tsv line to Code. IMPORTANT: Do not execute save or update operation
     */
    public static CodeMetamac tsvLineToCode(ImportationCodesTsvHeader header, String[] columns, int lineNumber, CodelistVersionMetamac codelistVersion,
            Map<String, VariableElement> variableElementsInVariableByCode, boolean updateAlreadyExisting, Map<String, CodeMetamac> codesPreviousInCodelist,
            Map<String, CodeMetamac> codesToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems) throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        if (codesToPersistByCode.containsKey(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, codeIdentifier, lineNumber));
            return null;
        }
        if (!SemanticIdentifierValidationUtils.isCodeSemanticIdentifier(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, codeIdentifier,
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // parent
        CodeMetamac codeParent = null;
        String codeParentIdentifier = columns[header.getParentPosition()];
        if (!StringUtils.isBlank(codeParentIdentifier)) {
            if (codesToPersistByCode.containsKey(codeParentIdentifier)) {
                codeParent = codesToPersistByCode.get(codeParentIdentifier);
            } else {
                codeParent = codesPreviousInCodelist.get(codeParentIdentifier); // try code already exists in codelist before this importation
            }
            if (codeParent == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND, codeParentIdentifier, codeIdentifier));
                return null;
            }
        }
        // variable element
        String variableElementIdentifier = columns[header.getVariableElementPosition()];
        VariableElement variableElement = null;
        boolean updateVariableElement = true;
        if (!StringUtils.isBlank(variableElementIdentifier)) {
            variableElement = variableElementsInVariableByCode.get(variableElementIdentifier);
            if (variableElement == null) {
                // do not abort importation. Only inform about this
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND, variableElementIdentifier, codeIdentifier));
                updateVariableElement = false;
            }

        }
        // init code
        CodeMetamac code = codesPreviousInCodelist.get(codeIdentifier);
        if (code == null) {
            code = new CodeMetamac();
            code.setNameableArtefact(new NameableArtefact());
            code.getNameableArtefact().setCode(codeIdentifier);
            String urn = GeneratorUrnUtils.generateCodeUrn(codelistVersion, code.getNameableArtefact().getCode());
            code.getNameableArtefact().setUrn(urn);
            code.getNameableArtefact().setUrnProvider(urn);
            code.setParent(codeParent);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, code.getNameableArtefact().getCode()));
                return null;
            } else {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, code.getNameableArtefact().getCode()));
                if (SdmxSrmUtils.isItemParentChanged(code.getParent(), codeParent)) {
                    // Clear all order columns to put at the end of new level
                    SrmServiceUtils.clearCodeOrders(code);
                }
                code.setParent(codeParent);
                code.setUpdateDate(new DateTime());
            }
        }
        code.getNameableArtefact().setName(TsvImportationUtils.tsvLineToInternationalString(header.getName(), columns, code.getNameableArtefact().getName()));
        if (code.getNameableArtefact().getName() == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, code.getNameableArtefact().getCode(),
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
        }
        code.getNameableArtefact().setDescription(TsvImportationUtils.tsvLineToInternationalString(header.getDescription(), columns, code.getNameableArtefact().getDescription()));
        code.getNameableArtefact().setComment(TsvImportationUtils.tsvLineToInternationalString(header.getComment(), columns, code.getNameableArtefact().getComment()));
        if (updateVariableElement) {
            code.setVariableElement(variableElement);
            // Short name. Only filled if code has not related variable element
            if (variableElement != null) {
                code.setShortName(null);
            } else {
                code.setShortName(TsvImportationUtils.tsvLineToInternationalString(header.getShortName(), columns, code.getShortName()));
            }
        }

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            // extra validation to avoid save some incorrect
            if (code.getId() == null) {
                CodesMetamacInvocationValidator.checkCreateCode(codelistVersion, code, null);
            } else {
                CodesMetamacInvocationValidator.checkUpdateCode(codelistVersion, code, null);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing code from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }

        code.setItemSchemeVersion(codelistVersion);
        if (codeParent == null) {
            code.setItemSchemeVersionFirstLevel(codelistVersion);
        } else {
            code.setItemSchemeVersionFirstLevel(null);
        }

        return code;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODE ORDERS
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Imports header of tsv to import orders of codes.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeOrdersTsvHeader parseTsvHeaderToImportCodeOrders(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 2));
        }
        ImportationCodeOrdersTsvHeader header = new ImportationCodeOrdersTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String columnName = headerColumns[i];
            if (i == 0) {
                if (!SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
                    break;
                }
                header.setCodePosition(i);
            } else if (SrmConstants.TSV_HEADER_LABEL.equals(columnName)) {
                header.setLabelPosition(i);
            } else if (SrmConstants.TSV_HEADER_LEVEL.equals(columnName)) {
                header.setLevelPosition(i);
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
            } else {
                // must be a code of order visualisation
                header.addOrderVisualisations(columnName, i);
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (!header.hasOrderVisualisations()) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER));
            }
        }
        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // INTERNATIONAL STRINGS
    // ---------------------------------------------------------------------------------------------------------------

    public static InternationalString tsvLineToInternationalString(InternationalStringTsv internationalStringTsv, String[] columns, InternationalString target) {
        if (internationalStringTsv == null) {
            return null;
        }
        Set<LocalisedString> localisedStringTargets = new HashSet<LocalisedString>();
        int j = 0;
        for (int i = internationalStringTsv.getStartPosition(); i <= internationalStringTsv.getEndPosition(); i++) {
            String label = columns[i];
            if (!StringUtils.isBlank(label)) {
                String locale = internationalStringTsv.getLocale(j);
                if (target == null) {
                    target = new InternationalString();
                }
                LocalisedString localisedString = target.getLocalisedLabelEntity(locale);
                if (localisedString == null) {
                    localisedString = new LocalisedString(locale, label);
                } else {
                    localisedString.setLabel(label);
                }
                localisedStringTargets.add(localisedString);
            }
            j++;
        }
        if (localisedStringTargets.size() == 0) {
            return null;
        }
        if (target != null) {
            target.removeAllTexts();
            for (LocalisedString localisedString : localisedStringTargets) {
                target.addText(localisedString);
            }
        }
        return target;
    }

    private static InternationalStringTsv columnHeaderToInternationalStringTsv(String[] headerColumns, int headerColumnIndex, String[] columnSplited, String headerExpected,
            InternationalStringTsv target, List<MetamacExceptionItem> exceptions) {
        if (columnSplited == null || columnSplited.length != 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }
        String locale = columnSplited[1];
        if (StringUtils.isBlank(locale)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
        }
        if (target == null) {
            target = new InternationalStringTsv();
            target.setStartPosition(headerColumnIndex);
        }
        target.addLocale(locale);
        if (headerColumnIndex == headerColumns.length - 1 || !headerColumns[headerColumnIndex + 1].startsWith(headerExpected)) {
            target.setEndPosition(headerColumnIndex);
        }
        return target;
    }

    private static RepresentationsTsv columnHeaderToRepresentationTsv(String[] headerColumns, int headerColumnIndex, String[] columnSplited, String headerExpected, RepresentationsTsv target,
            List<MetamacExceptionItem> exceptions) {

        if (columnSplited == null || columnSplited.length != 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }

        if (target == null) {
            target = new RepresentationsTsv();
        }

        String subfield = columnSplited[1];
        if (SrmConstants.TSV_HEADER_REPRESENTATION_SUB_TYPE.equals(subfield.toLowerCase())) {
            target.setTypePosition(headerColumnIndex);
        } else if (SrmConstants.TSV_HEADER_REPRESENTATION_SUB_VALUE.equals(subfield.toLowerCase())) {
            target.setValuePosition(headerColumnIndex);
        } else {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }
        return target;
    }

    public static void checkItemSchemeCanExecuteImportation(ItemSchemeVersion itemSchemeVersion, SrmLifeCycleMetadata srmLifeCycleMetadata, Boolean canBeBackground) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(srmLifeCycleMetadata, itemSchemeVersion.getMaintainableArtefact().getUrn());
        if (BooleanUtils.isTrue(canBeBackground)) {
            SrmValidationUtils.checkArtefactWithoutTaskInBackground(itemSchemeVersion);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ITEMS
    // ---------------------------------------------------------------------------------------------------------------

    private static ImportationItemsTsvHeader parseTsvHeaderToImportItems(String line, List<MetamacExceptionItem> exceptions, ImportationItemsTsvHeader header) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 4) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 4));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION,
                SrmConstants.TSV_HEADER_COMMENT);
        int headerExpectedIndex = 0;
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    private static Item tsvLineToItem(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean updateAlreadyExisting,
            Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems,
            TypeExternalArtefactsEnum itemType) throws MetamacException {

        // semantic identifier
        String itemIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(itemIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        if (!TypeExternalArtefactsEnum.CATEGORY.equals(itemType) && itemsToPersistByCode.containsKey(itemIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, itemIdentifier, lineNumber));
            return null;
        }
        if (!isSemanticIdentifier(itemIdentifier, itemType)) {

            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, itemIdentifier,
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // parent
        Item itemParent = null;
        String itemParentIdentifier = columns[header.getParentPosition()];
        if (!StringUtils.isBlank(itemParentIdentifier)) {
            if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_UNEXPECTED_PARENT, itemIdentifier, itemParentIdentifier));
                return null;
            }
            if (itemsToPersistByCode.containsKey(itemParentIdentifier)) {
                itemParent = itemsToPersistByCode.get(itemParentIdentifier);
            } else {
                itemParent = itemsPreviousInItemScheme.get(itemParentIdentifier);
            }
            if (itemParent == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND, itemParentIdentifier, itemIdentifier));
                return null;
            }
        }
        // initialize item
        Item item = TypeExternalArtefactsEnum.CATEGORY.equals(itemType) ? (itemsPreviousInItemScheme.get(StringUtils.isNotBlank(itemParentIdentifier)
                ? itemParentIdentifier + "." + itemIdentifier
                : itemIdentifier)) : itemsPreviousInItemScheme.get(itemIdentifier);
        if (item == null) {
            item = createItem(itemType);
            item.setNameableArtefact(new NameableArtefact());
            item.getNameableArtefact().setCode(itemIdentifier);
            item.setParent(itemParent);
            item.getNameableArtefact().setIsCodeUpdated(false);
            if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
                String codeFull = GeneratorUrnUtils.generateCategoryCodeFull(item);
                item.getNameableArtefact().setCodeFull(codeFull);
                if (itemsToPersistByCode.containsKey(codeFull)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, itemIdentifier, lineNumber));
                    return null;
                }
            }
            String urn = generateItemUrn(itemSchemeVersion, item, itemType);
            item.getNameableArtefact().setUrn(urn);
            item.getNameableArtefact().setUrnProvider(urn);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, item.getNameableArtefact().getCode()));
                return null;
            } else {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, item.getNameableArtefact().getCode()));
                item.setParent(itemParent);
                item.setUpdateDate(new DateTime());
            }
            item.getNameableArtefact().setIsCodeUpdated(false);
        }
        item.getNameableArtefact().setName(TsvImportationUtils.tsvLineToInternationalString(header.getName(), columns, item.getNameableArtefact().getName()));
        if (item.getNameableArtefact().getName() == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, item.getNameableArtefact().getCode(),
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
        }
        item.getNameableArtefact().setDescription(TsvImportationUtils.tsvLineToInternationalString(header.getDescription(), columns, item.getNameableArtefact().getDescription()));
        item.getNameableArtefact().setComment(TsvImportationUtils.tsvLineToInternationalString(header.getComment(), columns, item.getNameableArtefact().getComment()));

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            if (item.getId() == null) {
                checkCanCreateItem(itemSchemeVersion, item, itemType);
            } else {
                checkCanUpdateItem(itemSchemeVersion, item, itemType);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing item from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }

        item.setItemSchemeVersion(itemSchemeVersion);
        if (itemParent == null) {
            item.setItemSchemeVersionFirstLevel(itemSchemeVersion);
        } else {
            item.setItemSchemeVersionFirstLevel(null);
        }

        return item;
    }

    private static void checkCanCreateItem(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) throws MetamacException {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ConceptsMetamacInvocationValidator.checkCreateConcept((ConceptSchemeVersionMetamac) itemSchemeVersion, (ConceptMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            OrganisationsMetamacInvocationValidator.checkCreateOrganisation((OrganisationSchemeVersionMetamac) itemSchemeVersion, (OrganisationMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            CategoriesMetamacInvocationValidator.checkCreateCategory((CategorySchemeVersionMetamac) itemSchemeVersion, (CategoryMetamac) item, null);
        }
    }

    private static void checkCanUpdateItem(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) throws MetamacException {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ConceptsMetamacInvocationValidator.checkUpdateConcept((ConceptSchemeVersionMetamac) itemSchemeVersion, (ConceptMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            OrganisationsMetamacInvocationValidator.checkUpdateOrganisation((OrganisationMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            CategoriesMetamacInvocationValidator.checkUpdateCategory((CategoryMetamac) item, null);
        }
    }

    private static Item createItem(TypeExternalArtefactsEnum type) {
        Item item = null;
        if (TypeExternalArtefactsEnum.CONCEPT.equals(type)) {
            item = new ConceptMetamac();
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(type) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(type) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(type)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(type)) {
            item = new OrganisationMetamac();
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
            item = new CategoryMetamac();
        }
        return item;
    }

    private static boolean isSemanticIdentifier(String itemIdentifier, TypeExternalArtefactsEnum type) {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(type)) {
            return SemanticIdentifierValidationUtils.isConceptSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(type)) {
            return SemanticIdentifierValidationUtils.isAgencySemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.DATA_CONSUMER.equals(type)) {
            return SemanticIdentifierValidationUtils.isDataConsumerSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.DATA_PROVIDER.equals(type)) {
            return SemanticIdentifierValidationUtils.isDataProviderSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(type)) {
            return SemanticIdentifierValidationUtils.isOrganisationUnitSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
            return SemanticIdentifierValidationUtils.isCategorySemanticIdentifier(itemIdentifier);
        }
        return false;
    }

    private static String generateItemUrn(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            return GeneratorUrnUtils.generateConceptUrn(itemSchemeVersion, item.getNameableArtefact().getCode());
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            return GeneratorUrnUtils.generateOrganisationUrn(itemSchemeVersion, item.getNameableArtefact().getCode());
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            return GeneratorUrnUtils.generateCategoryUrn(itemSchemeVersion, item);
        }
        return null;
    }

}
