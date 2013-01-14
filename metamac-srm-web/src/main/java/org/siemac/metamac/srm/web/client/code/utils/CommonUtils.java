package org.siemac.metamac.srm.web.client.code.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.client.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.client.code.model.record.VariableRecord;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CommonUtils {

    private static LinkedHashMap<String, String> accessTypeHashMap = null;

    public static LinkedHashMap<String, String> getAccessTypeHashMap() {
        if (accessTypeHashMap == null) {
            accessTypeHashMap = new LinkedHashMap<String, String>();
            accessTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
            for (AccessTypeEnum a : AccessTypeEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().accessTypeEnum() + a.getName());
                accessTypeHashMap.put(a.name(), value);
            }
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
}
