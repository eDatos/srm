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
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersCsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesCsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsCsvHeader;
import org.siemac.metamac.srm.core.task.domain.InternationalStringCsv;

public class ImportationCsvUtils {

    public static ImportationVariableElementsCsvHeader parseCsvHeaderToImportVariableElements(String[] headerColumns, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, 2));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.CSV_HEADER_CODE, SrmConstants.CSV_HEADER_SHORT_NAME);
        int headerExpectedIndex = 0;
        ImportationVariableElementsCsvHeader header = new ImportationVariableElementsCsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.CSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];
            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, headerExpected));
                break;
            }
            if (SrmConstants.CSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.CSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringCsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
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
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, SrmConstants.CSV_HEADER_SHORT_NAME));
            }
        }
        // description is optional
        return header;
    }

    public static ImportationCodesCsvHeader parseCsvHeaderToImportCodes(String[] headerColumns, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (headerColumns.length < 4) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, 4));
        }
        List<String> headersExpected = Arrays.asList(SrmConstants.CSV_HEADER_CODE, SrmConstants.CSV_HEADER_PARENT, SrmConstants.CSV_HEADER_VARIABLE_ELEMENT, SrmConstants.CSV_HEADER_NAME,
                SrmConstants.CSV_HEADER_DESCRIPTION);
        int headerExpectedIndex = 0;
        ImportationCodesCsvHeader header = new ImportationCodesCsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.CSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, headerExpected));
                break;
            }
            if (SrmConstants.CSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.CSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.CSV_HEADER_VARIABLE_ELEMENT.equals(columnName)) {
                header.setVariableElementPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.CSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringCsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.CSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringCsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
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
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, SrmConstants.CSV_HEADER_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, SrmConstants.CSV_HEADER_NAME));
            }
            // description is optional
        }
        return header;
    }

    /**
     * Imports header of csv to import orders of codes.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeOrdersCsvHeader parseCsvHeaderToImportCodeOrders(String[] headerColumns, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, 2));
        }
        ImportationCodeOrdersCsvHeader header = new ImportationCodeOrdersCsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String columnName = headerColumns[i];
            if (i == 0) {
                if (!SrmConstants.CSV_HEADER_CODE.equals(columnName)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, SrmConstants.CSV_HEADER_CODE));
                    break;
                }
                header.setCodePosition(i);
            } else if (SrmConstants.CSV_HEADER_LABEL.equals(columnName)) {
                header.setLabelPosition(i);
            } else if (SrmConstants.CSV_HEADER_LEVEL.equals(columnName)) {
                header.setLevelPosition(i);
            } else if (SrmConstants.CSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
            } else {
                // must be a code of order visualisation
                header.addOrderVisualisations(columnName, i);
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (!header.hasOrderVisualisations()) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, SrmConstants.CSV_HEADER_ORDER));
            }
        }
        return header;
    }

    public static InternationalString csvLineToInternationalString(InternationalStringCsv internationalStringCsv, String[] columns, InternationalString target) {
        if (internationalStringCsv == null) {
            return null;
        }
        Set<LocalisedString> localisedStringTargets = new HashSet<LocalisedString>();
        int j = 0;
        for (int i = internationalStringCsv.getStartPosition(); i <= internationalStringCsv.getEndPosition(); i++) {
            String label = columns[i];
            if (!StringUtils.isBlank(label)) {
                String locale = internationalStringCsv.getLocale(j);
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

    private static InternationalStringCsv columnHeaderToInternationalStringCsv(String[] headerColumns, int headerColumnIndex, String[] columnSplited, String headerExpected,
            InternationalStringCsv target, List<MetamacExceptionItem> exceptions) {
        if (columnSplited.length != 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, headerExpected));
            return null;
        }
        String locale = columnSplited[1];
        if (StringUtils.isBlank(locale)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, headerExpected));
        }
        if (target == null) {
            target = new InternationalStringCsv();
            target.setStartPosition(headerColumnIndex);
        }
        target.addLocale(locale);
        if (headerColumnIndex == headerColumns.length - 1 || !headerColumns[headerColumnIndex + 1].startsWith(headerExpected)) {
            target.setEndPosition(headerColumnIndex);
        }
        return target;
    }
}