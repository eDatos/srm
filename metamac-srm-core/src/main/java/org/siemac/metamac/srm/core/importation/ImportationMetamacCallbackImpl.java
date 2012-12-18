package org.siemac.metamac.srm.core.importation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.importation.ImportationCallbackImplBase;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;

@Component("importationMetamacCallback")
public class ImportationMetamacCallbackImpl extends ImportationCallbackImplBase {

    /**************************************************************************
     * VALIDATES
     *************************************************************************/

    @Override
    public void validateRestrictions(OrganisationSchemeVersion source) throws MetamacException {
        validateRestrictionsGeneral(source);
    }

    /**************************************************************************
     * FILLS
     *************************************************************************/

    @Override
    public void fillOrganisationSchemeVersion(OrganisationSchemeVersion source, OrganisationSchemeVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillOrganisation(Organisation source, Organisation target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void validateRestrictions(CodelistVersion source) throws MetamacException {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillCodelistVersion(CodelistVersion source, CodelistVersion target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fillCode(Code source, Code target) {
        // TODO Auto-generated method stub

    }

}
