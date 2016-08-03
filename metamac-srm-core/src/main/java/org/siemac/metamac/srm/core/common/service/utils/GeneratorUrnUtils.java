package org.siemac.metamac.srm.core.common.service.utils;

import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;

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
        return generateVariableUrn(variable.getNameableArtefact().getCode());
    }
    public static String generateVariableUrn(String variableCode) {
        return generateSiemacStructuralResourcesVariableUrn(variableCode);
    }

    /**
     * Generate variable element urn
     */
    public static String generateVariableElementUrn(Variable variable, VariableElement variableElement) {
        return generateSiemacStructuralResourcesVariableElementUrn(variable.getNameableArtefact().getCode(), variableElement.getIdentifiableArtefact().getCode());
    }

    /**
     * Generate order visualisation urn
     */
    public static String generateCodelistOrderVisualisationUrn(CodelistVersion codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation) {
        String[] maintainerCode = generateSdmxUrnMaintainerIdFragment(codelistVersion.getMaintainableArtefact().getMaintainer());
        String codelistCode = codelistVersion.getMaintainableArtefact().getCode();
        String codelistVersionNumber = codelistVersion.getMaintainableArtefact().getVersionLogic();

        return generateSiemacStructuralResourcesCodelistOrderVisualisationUrn(maintainerCode, codelistCode, codelistVersionNumber, codelistOrderVisualisation.getNameableArtefact().getCode());
    }

    /**
     * Generate openness visualisation urn
     */
    public static String generateCodelistOpennessVisualisationUrn(CodelistVersion codelistVersion, CodelistOpennessVisualisation codelistOpennessVisualisation) {
        String[] maintainerCode = generateSdmxUrnMaintainerIdFragment(codelistVersion.getMaintainableArtefact().getMaintainer());
        String codelistCode = codelistVersion.getMaintainableArtefact().getCode();
        String codelistVersionNumber = codelistVersion.getMaintainableArtefact().getVersionLogic();

        return generateSiemacStructuralResourcesCodelistOpennessLevelsUrn(maintainerCode, codelistCode, codelistVersionNumber, codelistOpennessVisualisation.getNameableArtefact().getCode());
    }
}