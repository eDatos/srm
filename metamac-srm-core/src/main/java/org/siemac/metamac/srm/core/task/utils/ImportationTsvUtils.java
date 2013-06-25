package org.siemac.metamac.srm.core.task.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.InternationalStringTsv;

public class ImportationTsvUtils {

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

    public static ImportationCodesTsvHeader parseTsvHeaderToImportCodes(String[] headerColumns, List<MetamacExceptionItem> exceptions) throws MetamacException {
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
     * Imports header of tsv to import orders of codes.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeOrdersTsvHeader parseTsvHeaderToImportCodeOrders(String[] headerColumns, List<MetamacExceptionItem> exceptions) throws MetamacException {
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
}
