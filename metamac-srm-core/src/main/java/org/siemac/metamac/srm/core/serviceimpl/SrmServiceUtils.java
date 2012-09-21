package org.siemac.metamac.srm.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class SrmServiceUtils {

    public static String[] procStatusEnumToString(ProcStatusEnum... procStatus) {
        String[] procStatusString = new String[procStatus.length];
        for (int i = 0; i < procStatus.length; i++) {
            procStatusString[i] = procStatus[i].name();
        }
        return procStatusString;
    }

    public static List<ProcStatusEnum> procStatusEnumToList(ProcStatusEnum[] procStatusArray) {
        List<ProcStatusEnum> procStatus = new ArrayList<ProcStatusEnum>();
        for (int i = 0; i < procStatusArray.length; i++) {
            procStatus.add(procStatusArray[i]);
        }
        return procStatus;
    }
}
