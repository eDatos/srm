package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CodesDoMocks {

    public static CodelistVersionMetamac mockCodelist(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
        addReplaceMetadatasToCodelist(target, agencyID);
        addConfigurationsMetadatasToCodelist(target);
        return target;
    }

    public static CodelistVersionMetamac mockCodelistWithCodes(String agencyID, String resourceID, String version) {
        CodelistVersionMetamac target = CodesMetamacDoMocks.mockCodelistWithCodesFixedValues(agencyID, resourceID, version);
        addReplaceMetadatasToCodelist(target, agencyID);
        addConfigurationsMetadatasToCodelist(target);
        return target;
    }

    public static CodeMetamac mockCode(String resourceID, CodelistVersionMetamac codelist, CodeMetamac parent) {
        CodeMetamac target = CodesMetamacDoMocks.mockCodeFixedValues(resourceID, codelist, parent);
        target.setVariableElement(CodesMetamacDoMocks.mockVariableElementFixedValues("variableElement-" + resourceID));
        return target;
    }

    public static ItemResult mockCodeItemResult(String resourceID, ItemResult parent, Integer order, Boolean open) {
        ItemResult itemResult = CodesMetamacDoMocks.mockCodeResultFixedValues(resourceID, parent);
        itemResult.setUriProvider(null);
        itemResult.setUrn(itemResult.getUrn().replace("urn:sdmx:org.sdmx.infomodel.xxx=", "urn:sdmx:org.sdmx.infomodel.codelist.Code=agency1:itemScheme1(01.000)."));
        itemResult.setUrnProvider(itemResult.getUrn());

        CodeMetamacResultExtensionPoint extensionPoint = new CodeMetamacResultExtensionPoint();
        itemResult.setExtensionPoint(extensionPoint);
        extensionPoint.setOpenness(open);
        extensionPoint.setOrder(order);
        extensionPoint.setVariableElement(CodesMetamacDoMocks.mockVariableElementResultFixedValues("variableElement-" + resourceID));
        return itemResult;
    }

    public static VariableFamily mockVariableFamily(String resourceID) {
        return CodesMetamacDoMocks.mockVariableFamilyFixedValues(resourceID);
    }

    public static Variable mockVariable(String resourceID) {
        Variable target = CodesMetamacDoMocks.mockVariableFixedValues(resourceID);
        addReplaceMetadatasToVariable(target);
        target.addFamily(mockVariableFamily("variableFamily1"));
        target.addFamily(mockVariableFamily("variableFamily2"));
        target.addFamily(mockVariableFamily("variableFamily3"));
        target.setType(VariableTypeEnum.GEOGRAPHICAL);
        return target;
    }

    public static VariableElement mockVariableElement(String resourceID) {
        VariableElement target = CodesMetamacDoMocks.mockVariableElementFixedValues(resourceID);
        addReplaceMetadatasToVariableElement(target);
        return target;
    }

    public static CodelistFamily mockCodelistFamily(String resourceID) {
        return CodesMetamacDoMocks.mockCodelistFamilyFixedValues(resourceID);
    }

    private static void addReplaceMetadatasToCodelist(CodelistVersionMetamac target, String agencyID) {
        target.setReplacedByCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplacedBy", "01.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo1", "01.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo2", "02.000", AccessTypeEnum.PUBLIC, false, false));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo3", "03.000", AccessTypeEnum.PUBLIC, true, true));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo4", "04.000", AccessTypeEnum.PUBLIC, false, false));
        target.addReplaceToCodelist(mockCodelistToReplaceMetadata(agencyID, "codelistReplaceTo5", "05.000", AccessTypeEnum.RESTRICTED, true, true));
    }

    private static void addConfigurationsMetadatasToCodelist(CodelistVersionMetamac target) {
        target.addOrderVisualisation(mockCodelistOrderVisualisation("order1"));
        target.addOrderVisualisation(mockCodelistOrderVisualisation("order2"));
        target.addOrderVisualisation(mockCodelistOrderVisualisation("order3"));
        target.setDefaultOrderVisualisation(target.getOrderVisualisations().get(0));
        target.addOpennessVisualisation(mockCodelistOpennessVisualisation("openness1"));
        target.addOpennessVisualisation(mockCodelistOpennessVisualisation("openness2"));
        target.setDefaultOpennessVisualisation(target.getOpennessVisualisations().get(1));
    }

    private static CodelistOrderVisualisation mockCodelistOrderVisualisation(String code) {
        CodelistOrderVisualisation target = new CodelistOrderVisualisation();
        target.setNameableArtefact(new NameableArtefact());
        target.getNameableArtefact().setCode(code);
        target.getNameableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("order", code));
        return target;
    }

    private static CodelistOpennessVisualisation mockCodelistOpennessVisualisation(String code) {
        CodelistOpennessVisualisation target = new CodelistOpennessVisualisation();
        target.setNameableArtefact(new NameableArtefact());
        target.getNameableArtefact().setCode(code);
        target.getNameableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("openness", code));
        return target;
    }

    private static void addReplaceMetadatasToVariable(Variable target) {
        target.setReplacedByVariable(CodesMetamacDoMocks.mockVariableFixedValues("variableReplacedBy1"));
        target.addReplaceToVariable(CodesMetamacDoMocks.mockVariableFixedValues("variableReplaceTo1"));
        target.addReplaceToVariable(CodesMetamacDoMocks.mockVariableFixedValues("variableReplaceTo2"));
    }

    private static void addReplaceMetadatasToVariableElement(VariableElement target) {
        target.setReplacedByVariableElement(CodesMetamacDoMocks.mockVariableElementFixedValues("variableElementReplacedBy1"));
        target.addReplaceToVariableElement(CodesMetamacDoMocks.mockVariableElementFixedValues("variableElementReplaceTo1"));
        target.addReplaceToVariableElement(CodesMetamacDoMocks.mockVariableElementFixedValues("variableElementReplaceTo2"));
    }

    private static CodelistVersionMetamac mockCodelistToReplaceMetadata(String agencyID, String resourceID, String version, AccessTypeEnum accessType, boolean finalLogic, boolean publicLogic) {
        CodelistVersionMetamac codelistReplace = CodesMetamacDoMocks.mockCodelistFixedValues(agencyID, resourceID, version);
        codelistReplace.setAccessType(accessType);
        codelistReplace.getMaintainableArtefact().setFinalLogicClient(finalLogic);
        codelistReplace.getMaintainableArtefact().setPublicLogic(publicLogic);
        return codelistReplace;
    }
}