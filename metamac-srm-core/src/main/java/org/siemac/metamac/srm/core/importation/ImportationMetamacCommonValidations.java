package org.siemac.metamac.srm.core.importation;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public abstract class ImportationMetamacCommonValidations {

    @Autowired
    private SrmValidation srmValidation;

    public void validateRestrictionsGeneral(ServiceContext ctx, Object source) throws MetamacException {
    }

    public <T extends MaintainableArtefact> void validateRestrictionsMaintainableArtefact(ServiceContext ctx, T source, boolean canBeNotFinal) throws MetamacException {

        // Check: If maintainer is internally or externally published
        // Check: Unable to import artifacts with the same value of the Agency maintainer in data configuration.
        srmValidation.checkMaintainer(ctx, source, Boolean.TRUE);

        // Check: All artifacts imported in METAMAC must be FINAL unless AgencySchemes, DataProviderSchemes and DataConsumerSchemes.
        if (!source.getFinalLogic() && !canBeNotFinal) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
                    .build();
        }

        // Check: Unable to import artifacts with value of TRUE in the field isExternalReference.
        if (source.getIsExternalReference()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                    .withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE).build();
        }
    }
}
