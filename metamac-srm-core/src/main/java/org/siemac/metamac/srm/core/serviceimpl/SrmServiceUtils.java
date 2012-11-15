package org.siemac.metamac.srm.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
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

    public static void checkProcStatus(SrmLifeCycleMetadata lifeCycle, String urn, ProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, lifeCycle.getProcStatus())) {
            String[] procStatusString = procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
    }
}