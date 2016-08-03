package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptsBySchemeActionHandler extends SecurityActionHandler<GetConceptsBySchemeAction, GetConceptsBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptsBySchemeActionHandler() {
        super(GetConceptsBySchemeAction.class);
    }

    @Override
    public GetConceptsBySchemeResult executeSecurityAction(GetConceptsBySchemeAction action) throws ActionException {
        try {
            List<ConceptMetamacVisualisationResult> itemHierarchyDtos = srmCoreServiceFacade.retrieveConceptsByConceptSchemeUrn(ServiceContextHolder.getCurrentServiceContext(),
                    action.getConceptSchemeUrn(), action.getLocale());
            return new GetConceptsBySchemeResult(itemHierarchyDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
