package org.siemac.metamac.srm.core.importation.serviceimpl.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.importation.domain.InternationalStringCsv;
import org.siemac.metamac.srm.core.importation.domain.VariableElementCsvHeader;

public class ImportationCsvUtils {

    public static VariableElementCsvHeader parseVariableElementHeader(String[] columns, List<MetamacExceptionItem> exceptions) throws MetamacException {
        VariableElementCsvHeader header = new VariableElementCsvHeader();
        header.setColumnsSize(columns.length);
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (i == 0) {
                if (!SrmConstants.CSV_HEADER_VARIABLE_ELEMENT_CODE.equals(column)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, ServiceExceptionParameters.IMPORTATION_CSV_COLUMN_CODE));
                }
                header.setCodePosition(i);
            } else {
                if (header.getShortName() == null) {
                    header.setShortName(new InternationalStringCsv());
                    header.getShortName().setStartPosition(i);
                    header.getShortName().setEndPosition(columns.length - 1);
                }
                String[] shortNameMetadataSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.CSV_HEADER_INTERNATIONAL_STRING_SEPARATOR);
                if (shortNameMetadataSplited.length != 2) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, ServiceExceptionParameters.IMPORTATION_CSV_COLUMN_SHORT_NAME));
                    break;
                }
                String shortNameMetadata = shortNameMetadataSplited[0];
                String shortNameLocale = shortNameMetadataSplited[1];
                if (!SrmConstants.CSV_HEADER_VARIABLE_ELEMENT_SHORT_NAME.equals(shortNameMetadata) || StringUtils.isBlank(shortNameLocale)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, ServiceExceptionParameters.IMPORTATION_CSV_COLUMN_SHORT_NAME));
                    break;
                }
                header.getShortName().addLocale(shortNameLocale);
            }
        }
        if (header.getShortName() == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_CSV_HEADER_INCORRECT, ServiceExceptionParameters.IMPORTATION_CSV_COLUMN_SHORT_NAME));
        }
        return header;
    }

    public static InternationalString csvLineToInternationalString(InternationalStringCsv internationalStringCsv, String[] columns, InternationalString target) {

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
        if (target != null) {
            target.removeAllTexts();
            for (LocalisedString localisedString : localisedStringTargets) {
                target.addText(localisedString);
            }
        }
        return target;
    }
}
