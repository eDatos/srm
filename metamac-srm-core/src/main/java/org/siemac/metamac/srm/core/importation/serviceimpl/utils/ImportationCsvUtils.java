package org.siemac.metamac.srm.core.importation.serviceimpl.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.importation.domain.InternationalStringCsv;
import org.siemac.metamac.srm.core.importation.domain.VariableElementCsvHeader;

public class ImportationCsvUtils {

    public static VariableElementCsvHeader parseVariableElementHeader(String[] columns) throws MetamacException {
        VariableElementCsvHeader header = new VariableElementCsvHeader();
        header.setColumnsSize(columns.length);
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (i == 0) {
                if (!SrmConstants.CSV_HEADER_VARIABLE_ELEMENT_CODE.equals(column)) {
                    throw new MetamacException(ServiceExceptionType.IMPORT_ERROR); // TODO error
                }
                header.setCodePosition(i);
            } else {
                if (header.getShortName() == null) {
                    header.setShortName(new InternationalStringCsv());
                    header.getShortName().setStartPosition(i);
                    header.getShortName().setEndPosition(columns.length - 1);
                }
                String[] shortNameMetadataSplited = column.split("#"); // TODO separador
                String shortNameMetadata = shortNameMetadataSplited[0];
                String shortNameLocale = shortNameMetadataSplited[1];
                if (!SrmConstants.CSV_HEADER_VARIABLE_ELEMENT_SHORT_NAME.equals(shortNameMetadata)) {
                    throw new MetamacException(ServiceExceptionType.IMPORT_ERROR); // TODO error
                }
                if (shortNameLocale == null) {
                    throw new MetamacException(ServiceExceptionType.IMPORT_ERROR); // TODO error
                }
                header.getShortName().addLocale(shortNameLocale);
            }
        }
        if (header.getShortName() == null) {
            throw new MetamacException(ServiceExceptionType.IMPORT_ERROR); // TODO error
        }
        return header;
    }

    public static InternationalString csvLineToInternationalString(InternationalStringCsv internationalStringCsv, String[] line) {
        InternationalString target = null;
        int j = 0;
        for (int i = internationalStringCsv.getStartPosition(); i <= internationalStringCsv.getEndPosition(); i++) {
            if (i >= line.length) {
                // maybe last label do not exists, so the array has one less element
                break;
            }
            String label = line[i];
            if (!StringUtils.isBlank(label)) {
                String locale = internationalStringCsv.getLocale(j);
                if (target == null) {
                    target = new InternationalString();
                }
                target.addText(new LocalisedString(locale, label));
            }
            j++;
        }
        return target;
    }
}
