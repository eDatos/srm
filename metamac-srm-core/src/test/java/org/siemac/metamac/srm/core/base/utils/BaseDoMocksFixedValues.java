package org.siemac.metamac.srm.core.base.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

public class BaseDoMocksFixedValues extends com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocksFixedValues {

    public static OrganisationMetamac mockMaintainer(String agencyID) {
        OrganisationMetamac target = new OrganisationMetamac();
        target.setIdAsMaintainer("idAsMaintainer" + agencyID);
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefact(target.getNameableArtefact(), agencyID, null);
        return target;
    }

    public static SrmLifeCycleMetadata mockLifecycleExternallyPublished() {
        SrmLifeCycleMetadata lifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        lifeCycleMetadata.setProductionValidationDate(new DateTime(2009, 9, 1, 1, 1, 1, 1));
        lifeCycleMetadata.setProductionValidationUser("production-user");
        lifeCycleMetadata.setDiffusionValidationDate(new DateTime(2010, 10, 2, 1, 1, 1, 1));
        lifeCycleMetadata.setDiffusionValidationUser("diffusion-user");
        lifeCycleMetadata.setInternalPublicationDate(new DateTime(2011, 11, 3, 1, 1, 1, 1));
        lifeCycleMetadata.setInternalPublicationUser("internal-publication-user");
        lifeCycleMetadata.setExternalPublicationDate(new DateTime(2012, 12, 4, 1, 1, 1, 1));
        lifeCycleMetadata.setExternalPublicationUser("external-publication-user");
        lifeCycleMetadata.setIsExternalPublicationFailed(Boolean.FALSE);
        lifeCycleMetadata.setExternalPublicationFailedDate(new DateTime(2013, 8, 2, 1, 1, 1, 1));
        return lifeCycleMetadata;
    }
}