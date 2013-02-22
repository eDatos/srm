package org.siemac.metamac.srm.core.importation;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public abstract class ImportationMetamacCommonValidations {

    /**
     * Besides this, other validations are performed in the methods preCreate of services.
     * 
     * @param <T>
     * @param ctx
     * @param source
     * @param canBeNotFinal
     * @throws MetamacException
     */
    public <T extends MaintainableArtefact> void validateRestrictionsMaintainableArtefact(ServiceContext ctx, T source, boolean canBeNotFinal) throws MetamacException {

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
