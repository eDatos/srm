package org.siemac.metamac.srm.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

public class SrmServiceUtils {

    public static String[] procStatusEnumToString(ItemSchemeMetamacProcStatusEnum... procStatus) {
        String[] procStatusString = new String[procStatus.length];
        for (int i = 0; i < procStatus.length; i++) {
            procStatusString[i] = procStatus[i].name();
        }
        return procStatusString;
    }

    public static List<ItemSchemeMetamacProcStatusEnum> procStatusEnumToList(ItemSchemeMetamacProcStatusEnum[] procStatusArray) {
        List<ItemSchemeMetamacProcStatusEnum> procStatus = new ArrayList<ItemSchemeMetamacProcStatusEnum>();
        for (int i = 0; i < procStatusArray.length; i++) {
            procStatus.add(procStatusArray[i]);
        }
        return procStatus;
    }
}
