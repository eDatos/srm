package org.siemac.metamac.srm.core.base.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

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

    public static SrmLifeCycleMetadata mockLifeCycleExternallyPublished() {
        SrmLifeCycleMetadata lifeCycleMetadata = new SrmLifeCycleMetadata(ProcStatusEnum.EXTERNALLY_PUBLISHED);
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

    public static OrganisationMetamac mockMaintainer(String agencyID) {
        OrganisationMetamac target = new OrganisationMetamac();
        target.setIdAsMaintainer("idAsMaintainer" + agencyID);
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), agencyID, null);
        return target;
    }
}
