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
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
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
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationConceptsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.InternationalStringTsv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;

public class TsvImportationUtils {

    private static Logger logger = LoggerFactory.getLogger(TsvImportationUtils.class);

    // ---------------------------------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationVariableElementsTsvHeader parseTsvHeaderToImportVariableElements(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 2));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_SHORT_NAME, SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY);
        int headerExpectedIndex = 0;
        ImportationVariableElementsTsvHeader header = new ImportationVariableElementsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];
            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                if (SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY.equals(headerExpected)) {
                    // optional
                } else {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                    break;
                }
            }
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
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getShortName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_SHORT_NAME));
            }
        }
        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationConceptsTsvHeader parseTsvHeaderToImportConcepts(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 4) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 4));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION);
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
     * Transforms tsv line to {@link ConceptMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static ConceptMetamac tsvLineToConcept(ImportationConceptsTsvHeader header, String[] columns, int lineNumber, ConceptSchemeVersionMetamac conceptSchemeVersion,
            boolean updateAlreadyExisting, Map<String, ConceptMetamac> conceptsPreviousInConceptScheme, Map<String, ConceptMetamac> conceptsToPersistByCode, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems) throws MetamacException {

        // semantic identifier
        String conceptIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(conceptIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        if (conceptsToPersistByCode.containsKey(conceptIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, conceptIdentifier, lineNumber));
            return null;
        }
        if (!SemanticIdentifierValidationUtils.isConceptSemanticIdentifier(conceptIdentifier)) {

            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, conceptIdentifier,
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // parent
        ConceptMetamac conceptParent = null;
        String conceptParentIdentifier = columns[header.getParentPosition()];
        if (!StringUtils.isBlank(conceptParentIdentifier)) {
            if (conceptsToPersistByCode.containsKey(conceptParentIdentifier)) {
                conceptParent = conceptsToPersistByCode.get(conceptParentIdentifier);
            } else {
                conceptParent = conceptsPreviousInConceptScheme.get(conceptParentIdentifier); // try concept already exists in concept scheme before this importation
            }
            if (conceptParent == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND, conceptParentIdentifier, conceptIdentifier));
                return null;
            }
        }
        // init concept
        ConceptMetamac concept = conceptsPreviousInConceptScheme.get(conceptIdentifier);
        if (concept == null) {
            concept = new ConceptMetamac();
            concept.setNameableArtefact(new NameableArtefact());
            concept.getNameableArtefact().setCode(conceptIdentifier);
            String urn = GeneratorUrnUtils.generateConceptUrn(conceptSchemeVersion, concept.getNameableArtefact().getCode());
            concept.getNameableArtefact().setUrn(urn);
            concept.getNameableArtefact().setUrnProvider(urn);
            concept.setParent(conceptParent);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, concept.getNameableArtefact().getCode()));
                return null;
            } else {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, concept.getNameableArtefact().getCode()));
                concept.setParent(conceptParent);
                concept.setUpdateDate(new DateTime());
            }
        }
        concept.getNameableArtefact().setName(TsvImportationUtils.tsvLineToInternationalString(header.getName(), columns, concept.getNameableArtefact().getName()));
        if (concept.getNameableArtefact().getName() == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, concept.getNameableArtefact().getCode(),
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
        }
        concept.getNameableArtefact().setDescription(TsvImportationUtils.tsvLineToInternationalString(header.getDescription(), columns, concept.getNameableArtefact().getDescription()));

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            // extra validation to avoid save some incorrect
            if (concept.getId() == null) {
                ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
            } else {
                ConceptsMetamacInvocationValidator.checkUpdateConcept(conceptSchemeVersion, concept, null);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing concept from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }

        concept.setItemSchemeVersion(conceptSchemeVersion);
        if (conceptParent == null) {
            concept.setItemSchemeVersionFirstLevel(conceptSchemeVersion);
        } else {
            concept.setItemSchemeVersionFirstLevel(null);
        }

        return concept;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationCodesTsvHeader parseTsvHeaderToImportCodes(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 4) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 4));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_VARIABLE_ELEMENT, SrmConstants.TSV_HEADER_NAME,
                SrmConstants.TSV_HEADER_DESCRIPTION);
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
        if (updateVariableElement) {
            code.setVariableElement(variableElement);
            if (variableElement != null) {
                code.setShortName(null);
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

    public static void checkItemSchemeCanExecuteImportation(ItemSchemeVersion itemSchemeVersion, SrmLifeCycleMetadata srmLifeCycleMetadata, Boolean canBeBackground) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(srmLifeCycleMetadata, itemSchemeVersion.getMaintainableArtefact().getUrn());
        if (BooleanUtils.isTrue(canBeBackground)) {
            SrmValidationUtils.checkArtefactWithoutTaskInBackground(itemSchemeVersion);
        }
    }
}
