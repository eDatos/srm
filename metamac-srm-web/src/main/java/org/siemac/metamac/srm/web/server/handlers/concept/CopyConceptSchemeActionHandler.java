package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.CopyConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CopyConceptSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyConceptSchemeActionHandler extends SecurityActionHandler<CopyConceptSchemeAction, CopyConceptSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyConceptSchemeActionHandler() {
        super(CopyConceptSchemeAction.class);
    }

    @Override
    public CopyConceptSchemeResult executeSecurityAction(CopyConceptSchemeAction action) throws ActionException {

        try {
            TaskInfo taskInfo = srmCoreServiceFacade.copyConceptScheme(ServiceContextHolder.getCurrentServiceContext(), action.getConceptSchemeUrn(), action.getCode());
            // Concept scheme will never be copied in background
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), taskInfo.getUrnResult());
            return new CopyConceptSchemeResult(conceptSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
