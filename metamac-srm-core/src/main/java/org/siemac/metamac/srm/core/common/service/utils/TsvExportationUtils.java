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
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
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
                writeItemExtensionPointShortName(writer, itemResult, languages);
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
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SHORT_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
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

    public static String exportConcepts(ConceptSchemeVersionMetamac conceptSchemeVersion, List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("concepts", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeConceptsHeader(conceptSchemeVersion, writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
                writeItemExtensionPointPluralName(writer, itemResult, languages);
                writeItemExtensionPointAcronym(writer, itemResult, languages);
                writeItemExtensionPointDescriptionSource(writer, itemResult, languages);
                writeItemExtensionPointContext(writer, itemResult, languages);
                writeItemExtensionPointDocMethod(writer, itemResult, languages);
                writeItemExtensionPointDerivation(writer, itemResult, languages);
                writeItemExtensionPointLegalActs(writer, itemResult, languages);
                writeConceptType(writer, itemResult);
                writeRepresentation(writer, itemResult);
                writeConceptExtends(writer, itemResult);
                writeSdmxRelatedArtefact(conceptSchemeVersion, writer, itemResult);
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

    private static void writeConceptsHeader(ConceptSchemeVersionMetamac conceptSchemeVersion, OutputStreamWriter writer, List<String> languages) throws IOException {
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
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_PLURAL_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_ACRONYM + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_CONTEXT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DOC_METHOD + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DERIVATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LEGAL_ACTS + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_CONCEPT_TYPE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_REPRESENTATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + SrmConstants.TSV_HEADER_REPRESENTATION_SUB_TYPE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_REPRESENTATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + SrmConstants.TSV_HEADER_REPRESENTATION_SUB_VALUE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_CONCEPT_EXTENDS);

        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SDMX_RELATED_ARTEFACT);
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
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportVariableElements(List<VariableElementResult> variableElements, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("variable_elements", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeVariableElementsHeader(writer, languages);

            for (VariableElementResult variableElementResult : variableElements) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeVariableElementCode(writer, variableElementResult);
                writeVariableElementGeographicalGranularity(writer, variableElementResult);
                writeVariableElementShortName(writer, variableElementResult, languages);
                writeVariableElementRenderingColor(writer, variableElementResult);
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

    private static void writeVariableElementsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SHORT_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_RENDERING_COLOR);
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

    private static void writeItemExtensionPointPluralName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getPluralName().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointAcronym(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getAcronym().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDescriptionSource(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDescriptionSource().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointContext(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getContext().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDocMethod(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDocMethod().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDerivation(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDerivation().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointLegalActs(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getLegalActs().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointShortName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) itemResult.getExtensionPoint();

        for (String language : languages) {
            String textInLocale = extensionPoint.getShortName().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeConceptType(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getConceptTypeIdentifier())) {
            writer.write(extensionPoint.getConceptTypeIdentifier());
        }
    }

    private static void writeRepresentation(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getRepresentationType() != null) {
            writer.write(extensionPoint.getRepresentationType().name());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getRepresentationValue());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }
    }

    private static void writeConceptExtends(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getConceptExtendsUrn())) {
            writer.write(extensionPoint.getConceptExtendsUrn());
        }
    }

    private static void writeSdmxRelatedArtefact(ConceptSchemeVersionMetamac conceptSchemeVersion, OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
            ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (extensionPoint.getSdmxRelatedArtefact() != null) {
                writer.write(extensionPoint.getSdmxRelatedArtefact().toString());
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

    private static void writeVariableElementCode(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        writer.write(variableElementResult.getCode());
    }

    private static void writeVariableElementGeographicalGranularity(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        ItemResult geographicalGranularity = variableElementResult.getGeographicalGranularity();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (geographicalGranularity != null) {
            writer.write(geographicalGranularity.getCode());
        }
    }

    private static void writeVariableElementShortName(OutputStreamWriter writer, VariableElementResult variableElementResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String nameInLocale = variableElementResult.getShortName().get(language);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (nameInLocale != null) {
                writer.write(nameInLocale);
            }
        }
    }

    private static void writeVariableElementRenderingColor(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        String renderingColor = variableElementResult.getRenderingColor();
        if (renderingColor != null) {
            writer.write(renderingColor);
        }
    }
}
