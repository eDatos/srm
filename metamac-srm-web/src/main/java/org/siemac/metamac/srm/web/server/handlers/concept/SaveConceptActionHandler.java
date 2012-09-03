package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptActionHandler extends SecurityActionHandler<SaveConceptAction, SaveConceptResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveConceptActionHandler() {
        super(SaveConceptAction.class);
    }

    @Override
    public SaveConceptResult executeSecurityAction(SaveConceptAction action) throws ActionException {
        try {
            ConceptMetamacDto conceptToSave = action.getConceptToSave();
            ConceptMetamacDto savedConcept = null;
            if (conceptToSave.getId() == null) {
                // Create
                savedConcept = srmCoreServiceFacade.createConcept(ServiceContextHolder.getCurrentServiceContext(), conceptToSave);
                return new SaveConceptResult(savedConcept);
            } else {
                // Update
                // TODO savedConcept = srmCoreServiceFacade.updateConcept(ServiceContextHolder.getCurrentServiceContext(), conceptToSave);
            }
            return new SaveConceptResult(savedConcept);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
