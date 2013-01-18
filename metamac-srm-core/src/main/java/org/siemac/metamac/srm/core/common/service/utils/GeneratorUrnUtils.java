package org.siemac.metamac.srm.core.common.service.utils;

import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

public class GeneratorUrnUtils {

    /**
     * TODO Generate codelist family urn
     */
    public static String generateCodelistFamilyUrn(CodelistFamily codelistFamily) {
        return "urn:" + codelistFamily.getNameableArtefact().getCode();
    }

    /**
     * TODO Generate variable family urn
     */
    public static String generateVariableFamilyUrn(VariableFamily variableFamily) {
        return "urn:" + variableFamily.getNameableArtefact().getCode();
    }

    /**
     * TODO Generate variable urn
     */
    public static String generateVariableUrn(Variable variable) {
        return "urn:" + variable.getNameableArtefact().getCode();
    }

    /**
     * TODO Generate variable element urn
     */
    public static String generateVariableElementUrn(VariableElement variableElement) {
        return "urn:" + variableElement.getNameableArtefact().getCode();
    }

    /**
     * TODO Generate order visualisation
     */
    public static String generateCodelistOrderVisualisationUrn(CodelistOrderVisualisation codelistOrderVisualisation) {
        return "urn:" + codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getUrn() + "." + codelistOrderVisualisation.getNameableArtefact().getCode();
    }
}