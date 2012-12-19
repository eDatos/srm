package org.siemac.metamac.srm.core.importation;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.importation.ImportationCallback;

public abstract class ImportationMetamacCallbackImplBase implements ImportationCallback {

    @Autowired
    private SrmConfiguration srmConfiguration;

    public void validateRestrictionsGeneral(Object source) throws MetamacException {
        // TODO validaciones generales
    }

    public <T extends MaintainableArtefact> void validateRestrictionsMaintainebleArtefact(T source) throws MetamacException {

        // TODO invocar a srmValidation para validaciones comunes? ej: maintainer

        // Check: All artifacts imported in Metamac must be FINAL unless AgencySchemes.
        if (!source.getFinalLogic()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
                    .build();
        }
        // TODO AgencyScheme, DataProvider.... no final

        // Check: Unable to import artifacts with the same value of the Agency maintainer.
        String dataOrganisationUrn = srmConfiguration.retrieveMaintainerUrnDefault();
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
