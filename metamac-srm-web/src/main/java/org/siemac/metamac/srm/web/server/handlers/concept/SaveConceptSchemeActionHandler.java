package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptSchemeActionHandler extends SecurityActionHandler<SaveConceptSchemeAction, SaveConceptSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveConceptSchemeActionHandler() {
        super(SaveConceptSchemeAction.class);
    }

    @Override
    public SaveConceptSchemeResult executeSecurityAction(SaveConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto conceptSchemeToSave = action.getConceptSchemeDto();
            ConceptSchemeMetamacDto savedConceptSchemeDto = null;
            if (conceptSchemeToSave.getId() == null) {
                // Create
                savedConceptSchemeDto = srmCoreServiceFacade.createConceptScheme(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToSave);
                return new SaveConceptSchemeResult(savedConceptSchemeDto);
            } else {
                // Update
                savedConceptSchemeDto = srmCoreServiceFacade.updateConceptScheme(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToSave);
            }
            return new SaveConceptSchemeResult(savedConceptSchemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
