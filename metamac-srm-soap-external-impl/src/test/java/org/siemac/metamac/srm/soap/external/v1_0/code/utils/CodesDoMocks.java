package org.siemac.metamac.srm.soap.external.v1_0.code.utils;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
        addReplaceMetadatasToCodelist(target, agencyID);
        return target;
    }

    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = CodesMetamacDoMocks.mockCodelistWithCodesFixedValues(agencyID, resourceID, version);
        addReplaceMetadatasToCodelist(target, agencyID);
        return target;
    }

    public static CodeMetamac mockCode(String resourceID, CodelistVersionMetamac codelist, CodeMetamac parent) {
        return CodesMetamacDoMocks.mockCodeFixedValues(resourceID, codelist, parent);
    }

    public static CodelistFamily mockCodelistFamily(String resourceID) {
        return CodesMetamacDoMocks.mockCodelistFamilyFixedValues(resourceID);
    }

    public static Variable mockVariableWithFamilies(String resourceID) {
        Variable variable = mockVariable(resourceID);
        variable.addFamily(mockVariableFamily("family01"));
        variable.addFamily(mockVariableFamily("family02"));
        variable.addFamily(mockVariableFamily("family03"));
        return variable;
    }

    public static Variable mockVariable(String resourceID) {
        Variable target = CodesMetamacDoMocks.mockVariableFixedValues(resourceID);
        target.setReplacedByVariable(CodesMetamacDoMocks.mockVariableFixedValues("replacedByVariable01"));
        target.addReplaceToVariable(CodesMetamacDoMocks.mockVariableFixedValues("replaceToVariable01"));
        target.addReplaceToVariable(CodesMetamacDoMocks.mockVariableFixedValues("replaceToVariable02"));
        return target;
    }

    public static VariableElement mockVariableElement(String resourceID) {
        return CodesMetamacDoMocks.mockVariableElementFixedValues(resourceID);
    }

    public static VariableFamily mockVariableFamily(String resourceID) {
        return CodesMetamacDoMocks.mockVariableFamilyFixedValues(resourceID);
    }

    private static void addReplaceMetadatasToCodelist(CodelistVersionMetamac target, String agencyID) {
        target.setReplacedByCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplacedBy", "01.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo1", "01.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo2", "02.000", AccessTypeEnum.PUBLIC, false, false));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo3", "03.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo4", "04.000", AccessTypeEnum.PUBLIC, false, false));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo5", "05.000", AccessTypeEnum.RESTRICTED, true, true));
    }

    private static CodelistVersionMetamac mockCodelistToReplaceMetadata(String agencyID, String resourceID, String version, AccessTypeEnum accessType, boolean finalLogic, boolean publicLogic) {
        CodelistVersionMetamac codelistReplace = CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
        codelistReplace.setAccessType(accessType);
        codelistReplace.getMaintainableArtefact().setFinalLogicClient(finalLogic);
        codelistReplace.getMaintainableArtefact().setPublicLogic(publicLogic);
        return codelistReplace;
    }

}