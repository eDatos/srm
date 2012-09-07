package org.siemac.metamac.srm.core.serviceimpl;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

public class SrmServiceUtils {

    public static String[] procStatusEnumToString(ItemSchemeMetamacProcStatusEnum... procStatus) {
        String[] procStatusString = new String[procStatus.length];
        for (int i = 0; i < procStatus.length; i++) {
            procStatusString[i] = procStatus[i].name();
        }
        return procStatusString;
    }
    
}

