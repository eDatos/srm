package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.CreateConceptSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.concept.CreateConceptSchemeTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateConceptSchemeTemporalVersionActionHandler extends SecurityActionHandler<CreateConceptSchemeTemporalVersionAction, CreateConceptSchemeTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateConceptSchemeTemporalVersionActionHandler() {
        super(CreateConceptSchemeTemporalVersionAction.class);
    }

    @Override
    public CreateConceptSchemeTemporalVersionResult executeSecurityAction(CreateConceptSchemeTemporalVersionAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.createTemporalVersionConceptScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new CreateConceptSchemeTemporalVersionResult(conceptSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
