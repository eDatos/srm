package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporal;

public class BaseReplaceFromTemporalMetamac extends BaseReplaceFromTemporal {

    public static SrmLifeCycleMetadata replaceProductionAndDifussionLifeCycleMetadataFromTemporal(SrmLifeCycleMetadata target, SrmLifeCycleMetadata temp) {

        target.setProductionValidationDate(temp.getProductionValidationDate());
        target.setProductionValidationUser(temp.getProductionValidationUser());

        target.setDiffusionValidationDate(temp.getDiffusionValidationDate());
        target.setDiffusionValidationUser(temp.getDiffusionValidationUser());

        return target;
    }
}
