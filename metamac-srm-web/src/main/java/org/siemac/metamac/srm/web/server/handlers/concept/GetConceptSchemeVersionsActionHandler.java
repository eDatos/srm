package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeVersionsActionHandler extends SecurityActionHandler<GetConceptSchemeVersionsAction, GetConceptSchemeVersionsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptSchemeVersionsActionHandler() {
        super(GetConceptSchemeVersionsAction.class);
    }

    @Override
    public GetConceptSchemeVersionsResult executeSecurityAction(GetConceptSchemeVersionsAction action) throws ActionException {
        try {
            List<ConceptSchemeMetamacBasicDto> conceptSchemeMetamacDtos = srmCoreServiceFacade.retrieveConceptSchemeVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetConceptSchemeVersionsResult(conceptSchemeMetamacDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
