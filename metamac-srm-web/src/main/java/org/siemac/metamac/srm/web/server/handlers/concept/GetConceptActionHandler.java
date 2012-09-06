package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptActionHandler extends SecurityActionHandler<GetConceptAction, GetConceptResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptActionHandler() {
        super(GetConceptAction.class);
    }

    @Override
    public GetConceptResult executeSecurityAction(GetConceptAction action) throws ActionException {
        try {
            ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            List<ConceptMetamacDto> relatedRoleConcepts = srmCoreServiceFacade.retrieveRelatedConceptsRoles(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetConceptResult(conceptMetamacDto, relatedConcepts, relatedRoleConcepts);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
