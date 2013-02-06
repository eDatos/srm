package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public interface BaseDo2RestMapperV10 {

    public LifeCycle toLifeCycle(SrmLifeCycleMetadata source);
    public InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus toProcStatus(ProcStatusEnum source);
    public Date toDate(DateTime source);
}
