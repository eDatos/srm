package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesAction;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class FindAllConceptTypesActionHandler extends SecurityActionHandler<FindAllConceptTypesAction, FindAllConceptTypesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindAllConceptTypesActionHandler() {
        super(FindAllConceptTypesAction.class);
    }

    @Override
    public FindAllConceptTypesResult executeSecurityAction(FindAllConceptTypesAction action) throws ActionException {
        try {
            List<ConceptTypeDto> conceptTypeDtos = srmCoreServiceFacade.findAllConceptTypes(ServiceContextHolder.getCurrentServiceContext());
            return new FindAllConceptTypesResult(conceptTypeDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
