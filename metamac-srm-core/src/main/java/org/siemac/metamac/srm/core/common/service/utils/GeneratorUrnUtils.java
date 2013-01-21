package org.siemac.metamac.srm.core.common.service.utils;

import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

public class GeneratorUrnUtils extends com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils {

    /**
     * Generate codelist family urn
     */
    public static String generateCodelistFamilyUrn(CodelistFamily codelistFamily) {
        return generateSiemacStructuralResourcesCodelistFamilyUrn(codelistFamily.getNameableArtefact().getCode());
    }

    /**
     * Generate variable family urn
     */
    public static String generateVariableFamilyUrn(VariableFamily variableFamily) {
        return generateSiemacStructuralResourcesVariableFamilyUrn(variableFamily.getNameableArtefact().getCode());
    }

    /**
     * Generate variable urn
     */
    public static String generateVariableUrn(Variable variable) {
        return generateSiemacStructuralResourcesVariableUrn(variable.getNameableArtefact().getCode());
    }

    /**
     * Generate variable element urn
     */
    public static String generateVariableElementUrn(Variable variable, VariableElement variableElement) {
        return generateSiemacStructuralResourcesVariableElementUrn(variable.getNameableArtefact().getCode(), variableElement.getNameableArtefact().getCode());
    }

    /**
     * Generate order visualisation urn
     */
    public static String generateCodelistOrderVisualisationUrn(CodelistOrderVisualisation codelistOrderVisualisation) {
        String[] maintainerCode = generateSdmxUrnMaintainerIdFragment(codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getMaintainer());
        String codelistCode = codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getCode();
        String codelistVersionNumber = codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getVersionLogic();

        return generateSiemacStructuralResourcesCodelistOrderVisualisationUrn(maintainerCode, codelistCode, codelistVersionNumber, codelistOrderVisualisation.getNameableArtefact().getCode());
    }
}