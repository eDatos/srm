package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelOrganisationSchemeValidityActionHandler extends SecurityActionHandler<CancelOrganisationSchemeValidityAction, CancelOrganisationSchemeValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelOrganisationSchemeValidityActionHandler() {
        super(CancelOrganisationSchemeValidityAction.class);
    }

    @Override
    public CancelOrganisationSchemeValidityResult executeSecurityAction(CancelOrganisationSchemeValidityAction action) throws ActionException {
        List<OrganisationSchemeMetamacDto> conceptSchemeMetamacDtos = new ArrayList<OrganisationSchemeMetamacDto>();
        for (String urn : action.getUrns()) {
            try {
                OrganisationSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.cancelOrganisationSchemeValidity(ServiceContextHolder.getCurrentServiceContext(), urn);
                conceptSchemeMetamacDtos.add(conceptSchemeMetamacDto);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new CancelOrganisationSchemeValidityResult(conceptSchemeMetamacDtos);
    }

}
