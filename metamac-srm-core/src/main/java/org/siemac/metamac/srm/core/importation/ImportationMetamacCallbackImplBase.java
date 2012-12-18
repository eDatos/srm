package org.siemac.metamac.srm.core.importation;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.importation.ImportationCallback;

public abstract class ImportationMetamacCallbackImplBase implements ImportationCallback {

    @Autowired
    private final ConfigurationService configurationService = null;

    public void validateRestrictionsGeneral(Object source) throws MetamacException {

    }

    public <T extends MaintainableArtefact> void validateRestrictionsMaintainebleArtefact(T source) throws MetamacException {

        // Check: All artifacts imported in Metamac must be FINAL unless AgencySchemes.
        if (!source.getFinalLogic()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
                    .build();
        }

        // Check: Unable to import artifacts with the same value of the Agency maintainer.
        String dataOrganisationUrn = configurationService.getConfig().getString(SrmConstants.METAMAC_ORGANISATION_URN);
        if (StringUtils.isNotEmpty(dataOrganisationUrn) && !dataOrganisationUrn.equals(source.getMaintainer().getNameableArtefact().getUrn())) {
            // TODO lanzar excepcion
        }

        // Check: Unable to import artifacts with value of TRUE in the field isExternalReference.
        if (source.getIsExternalReference()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                    .withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE).build();
        }
    }
}
