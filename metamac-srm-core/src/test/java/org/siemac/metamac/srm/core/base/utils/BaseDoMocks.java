package org.siemac.metamac.srm.core.base.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class BaseDoMocks extends com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks {

    public static SrmLifeCycleMetadata mockLifeCycle() {
        SrmLifeCycleMetadata lifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT);
        lifeCycleMetadata.setProductionValidationDate(new DateTime(2009, 9, 1, 1, 1, 1, 1));
        lifeCycleMetadata.setProductionValidationUser("production-user");
        lifeCycleMetadata.setDiffusionValidationDate(new DateTime(2010, 10, 2, 1, 1, 1, 1));
        lifeCycleMetadata.setDiffusionValidationUser("diffusion-user");
        lifeCycleMetadata.setInternalPublicationDate(new DateTime(2011, 11, 3, 1, 1, 1, 1));
        lifeCycleMetadata.setInternalPublicationUser("internal-publication-user");
        lifeCycleMetadata.setExternalPublicationDate(new DateTime(2012, 12, 4, 1, 1, 1, 1));
        lifeCycleMetadata.setExternalPublicationUser("external-publication-user");
        return lifeCycleMetadata;
    }

}
