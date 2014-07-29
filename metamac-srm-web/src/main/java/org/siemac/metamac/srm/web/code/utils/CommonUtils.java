package org.siemac.metamac.srm.web.code.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementOperationRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.model.record.VariableRecord;
import org.siemac.metamac.srm.web.shared.code.enums.VariableElementShapeTypeEnum;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CommonUtils {

    private static String variableWorldUrn;
    private static String variableElementWorldUrn;

    public static LinkedHashMap<String, String> getAccessTypeHashMap() {
        LinkedHashMap<String, String> accessTypeHashMap = new LinkedHashMap<String, String>();
        accessTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (AccessTypeEnum a : AccessTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().accessTypeEnum() + a.getName());
            accessTypeHashMap.put(a.name(), value);
        }
        return accessTypeHashMap;
    }

    public static String getAccessTypeName(AccessTypeEnum accessTypeEnum) {
        return accessTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().accessTypeEnum() + accessTypeEnum.name()) : null;
    }

    public static List<String> getUrnsFromSelectedCodelists(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            CodelistRecord codelistRecord = (CodelistRecord) record;
            urns.add(codelistRecord.getUrn());
        }
        return urns;
    }

    public static List<String> getUrnsFromSelectedVariables(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            VariableRecord variableRecord = (VariableRecord) record;
            urns.add(variableRecord.getUrn());
        }
        return urns;
    }

    public static List<String> getUrnsFromSelectedVariableElements(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            VariableElementRecord variableRecord = (VariableElementRecord) record;
            urns.add(variableRecord.getUrn());
        }
        return urns;
    }

    public static List<String> getCodesFromSelectedVariableElementOperations(ListGridRecord[] records) {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : records) {
            VariableElementOperationRecord variableRecord = (VariableElementOperationRecord) record;
            codes.add(variableRecord.getCode());
        }
        return codes;
    }

    public static List<String> getUrnsFromSelectedCodelistVisualisations(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            CodelistVisualisationRecord codelistRecord = (CodelistVisualisationRecord) record;
            urns.add(codelistRecord.getUrn());
        }
        return urns;
    }

    public static List<String> getVisualisationCodesFromSelectedCodelistVisualisations(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            CodelistVisualisationRecord codelistRecord = (CodelistVisualisationRecord) record;
            urns.add(codelistRecord.getCode());
        }
        return urns;
    }

    public static String getCodelistVisualisationName(CodelistVisualisationDto codelistVisualisationDto) {
        return CommonWebUtils.getElementName(codelistVisualisationDto.getCode(), codelistVisualisationDto.getName());
    }

    public static LinkedHashMap<String, String> getCodelistVisualisationsHashMap(List<CodelistVisualisationDto> visualisations) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (CodelistVisualisationDto visualisation : visualisations) {
            hashMap.put(visualisation.getUrn(), getCodelistVisualisationName(visualisation));
        }
        return hashMap;
    }

    public static LinkedHashMap<String, String> getVariableElementShapeTypeHashMap() {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (VariableElementShapeTypeEnum value : VariableElementShapeTypeEnum.values()) {
            hashMap.put(value.name(), getVariableElementShapeTypeName(value));
        }
        return hashMap;
    }

    public static String getVariableElementShapeTypeName(VariableElementShapeTypeEnum variableElementShapeType) {
        return variableElementShapeType != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().variableElementShapeTypeEnum() + variableElementShapeType.name()) : null;
    }

    public static String getVariableElementOperationTypeName(VariableElementOperationTypeEnum operationTypeEnum) {
        return operationTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().variableElementOperationTypeEnum() + operationTypeEnum.name()) : null;
    }

    public static String getDefaultCodelistOrderUrn(CodelistMetamacDto codelistMetamacDto) {
        return codelistMetamacDto.getDefaultOrderVisualisation() != null ? codelistMetamacDto.getDefaultOrderVisualisation().getUrn() : null;
    }

    public static String getDefaultCodelistOpennessLevelUrn(CodelistMetamacDto codelistMetamacDto) {
        return codelistMetamacDto.getDefaultOpennessVisualisation() != null ? codelistMetamacDto.getDefaultOpennessVisualisation().getUrn() : null;
    }

    //
    // WORLD VARIABLE AND VARIABLE ELEMENT
    //

    public static String getVariableWorldUrn() {
        return variableWorldUrn;
    }

    public static void setVariableWorldUrn(String variableWorldUrn) {
        CommonUtils.variableWorldUrn = variableWorldUrn;
    }

    public static String getVariableElementWorldUrn() {
        return variableElementWorldUrn;
    }

    public static void setVariableElementWorldUrn(String variableElementWorldUrn) {
        CommonUtils.variableElementWorldUrn = variableElementWorldUrn;
    }

    public static boolean isVariableWorld(String variableUrn) {
        return StringUtils.equals(variableUrn, getVariableWorldUrn());
    }

    public static boolean isVariableElementWorld(String variableElementUrn) {
        return StringUtils.equals(variableElementUrn, getVariableElementWorldUrn());
    }
}
