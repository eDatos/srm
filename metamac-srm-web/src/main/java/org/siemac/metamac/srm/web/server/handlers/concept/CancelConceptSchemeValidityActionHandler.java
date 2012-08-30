package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelConceptSchemeValidityActionHandler extends SecurityActionHandler<CancelConceptSchemeValidityAction, CancelConceptSchemeValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelConceptSchemeValidityActionHandler() {
        super(CancelConceptSchemeValidityAction.class);
    }

    @Override
    public CancelConceptSchemeValidityResult executeSecurityAction(CancelConceptSchemeValidityAction action) throws ActionException {
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = new ArrayList<ConceptSchemeMetamacDto>();
        for (String urn : action.getUrns()) {
            try {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.cancelConceptSchemeValidity(ServiceContextHolder.getCurrentServiceContext(), urn);
                conceptSchemeMetamacDtos.add(conceptSchemeMetamacDto);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new CancelConceptSchemeValidityResult(conceptSchemeMetamacDtos);

    }

}
