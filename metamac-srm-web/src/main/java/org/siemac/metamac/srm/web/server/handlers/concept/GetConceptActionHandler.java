package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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
            // Retrieve concept
            ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());

            // Retrieve related concepts
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());

            // Retrieve roles
            List<RelatedResourceDto> roles = RelatedResourceUtils.getConceptMetamacDtosAsRelatedResourceDtos(srmCoreServiceFacade.retrieveRoleConcepts(ServiceContextHolder.getCurrentServiceContext(),
                    action.getUrn()));

            // Retrieve conceptScheme
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ServiceContextHolder.getCurrentServiceContext(),
                    conceptMetamacDto.getItemSchemeVersionUrn());

            return new GetConceptResult(conceptMetamacDto, roles, relatedConcepts, conceptSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
