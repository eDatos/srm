package org.siemac.metamac.srm.core.common.service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class TsvExportationUtils {

    private TsvExportationUtils() {
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportCodes(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("codes", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeCodesHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeCodeVariableElement(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    private static void writeCodesHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_VARIABLE_ELEMENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    private static void writeCodeVariableElement(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        String variableElement = SrmServiceUtils.getCodeItemResultVariableElementCode(itemResult);
        if (variableElement != null) {
            writer.write(variableElement);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportConcepts(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("concepts", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeConceptsHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    private static void writeConceptsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportOrganisations(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("organisations", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeOrganisationsHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    private static void writeOrganisationsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportCategories(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("categories", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeCategoriesHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCodeFull(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    private static void writeCategoriesHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // COMMON UTILS
    // ---------------------------------------------------------------------------------------------------------------

    private static void writeItemCode(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(itemResult.getCode());
    }

    private static void writeParentCode(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (itemResult.getParent() != null) {
            writer.write(itemResult.getParent().getCode());
        }
    }

    private static void writeParentCodeFull(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (itemResult.getParent() != null) {
            writer.write(itemResult.getParent().getCodeFull());
        }
    }

    private static void writeItemName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String nameInLocale = itemResult.getName().get(language);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (nameInLocale != null) {
                writer.write(nameInLocale);
            }
        }
    }

    private static void writeItemDescription(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String descriptionInLocale = itemResult.getDescription().get(language);
            descriptionInLocale = removeUnsupportedCharaters(descriptionInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (descriptionInLocale != null) {
                writer.write(descriptionInLocale);
            }
        }
    }

    private static void writeItemComment(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String textInLocale = itemResult.getComment().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static String removeUnsupportedCharaters(String string) {
        if (StringUtils.isNotBlank(string)) {
            string = string.replace('\n', ' ');
            string = string.replace('\t', ' ');
            string = string.replace('\r', ' ');
            string = string.replace('\b', ' ');
            string = string.replace('\f', ' ');
        }
        return string;
    }
}
