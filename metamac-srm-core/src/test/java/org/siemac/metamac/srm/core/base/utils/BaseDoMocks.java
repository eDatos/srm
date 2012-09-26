package org.siemac.metamac.srm.core.base.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class BaseDoMocks {

    public static SrmLifeCycleMetadata mockLifeCycle() {
        SrmLifeCycleMetadata lifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT);
        lifeCycleMetadata.setProductionValidationDate(new DateTime(100));
        lifeCycleMetadata.setProductionValidationUser("production-user");
        lifeCycleMetadata.setDiffusionValidationDate(new DateTime(200));
        lifeCycleMetadata.setDiffusionValidationUser("diffusion-user");
        lifeCycleMetadata.setInternalPublicationDate(new DateTime(300));
        lifeCycleMetadata.setInternalPublicationUser("internal-publication-user");
        lifeCycleMetadata.setExternalPublicationDate(new DateTime(300));
        lifeCycleMetadata.setExternalPublicationUser("external-publication-user");
        return lifeCycleMetadata;
    }

}
