package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService.ConceptSchemePage;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemePaginatedListActionHandler extends SecurityActionHandler<GetConceptSchemePaginatedListAction, GetConceptSchemePaginatedListResult> {

    public GetConceptSchemePaginatedListActionHandler() {
        super(GetConceptSchemePaginatedListAction.class);
    }

    @Override
    public GetConceptSchemePaginatedListResult executeSecurityAction(GetConceptSchemePaginatedListAction action) throws ActionException {
        // TODO: replace mock
        try {
            ConceptSchemePage page = ConceptSchemeService.findAllConceptSchemes(action.getFirstResult(), action.getMaxResults());
            int pageNumber = (action.getFirstResult() / action.getMaxResults()) + 1;
            return new GetConceptSchemePaginatedListResult(page.resultList, pageNumber, page.totalResults);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetConceptSchemePaginatedListAction action, GetConceptSchemePaginatedListResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }
}
