package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporal;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;

public class BaseReplaceFromTemporalMetamac extends BaseReplaceFromTemporal {

    public static SrmLifeCycleMetadata replaceProductionAndDifussionLifeCycleMetadataFromTemporal(SrmLifeCycleMetadata target, SrmLifeCycleMetadata temp) {

        target.setProductionValidationDate(temp.getProductionValidationDate());
        target.setProductionValidationUser(temp.getProductionValidationUser());

        target.setDiffusionValidationDate(temp.getDiffusionValidationDate());
        target.setDiffusionValidationUser(temp.getDiffusionValidationUser());

        return target;
    }

    public static CodelistOrderVisualisation replaceInternationalStringFromTemporalToCodelistOrderVisualisation(CodelistOrderVisualisation target, CodelistOrderVisualisation temp,
            InternationalStringRepository internationalStringRepository) {

        replaceInternationalStringFromTemporalToNameableArtefact(target.getNameableArtefact(), temp.getNameableArtefact(), internationalStringRepository);

        return target;
    }

    public static CodelistOpennessVisualisation replaceInternationalStringFromTemporalToCodelistOpennessVisualisation(CodelistOpennessVisualisation target, CodelistOpennessVisualisation temp,
            InternationalStringRepository internationalStringRepository) {

        replaceInternationalStringFromTemporalToNameableArtefact(target.getNameableArtefact(), temp.getNameableArtefact(), internationalStringRepository);

        return target;
    }
}
