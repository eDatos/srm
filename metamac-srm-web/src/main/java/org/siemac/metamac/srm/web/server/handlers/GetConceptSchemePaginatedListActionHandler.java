package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService.ConceptSchemePage;
import org.siemac.metamac.srm.web.shared.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetConceptSchemePaginatedListActionHandler extends AbstractActionHandler<GetConceptSchemePaginatedListAction, GetConceptSchemePaginatedListResult> {

    public GetConceptSchemePaginatedListActionHandler() {
        super(GetConceptSchemePaginatedListAction.class);
    }

    @Override
    public GetConceptSchemePaginatedListResult execute(GetConceptSchemePaginatedListAction action, ExecutionContext context) throws ActionException {
        // TODO: replace mock
        try {
            ConceptSchemePage page = ConceptSchemeService.findAllConceptSchemes(action.getFirstResult(), action.getMaxResults());
            return new GetConceptSchemePaginatedListResult(page.resultList, page.totalResults);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetConceptSchemePaginatedListAction action, GetConceptSchemePaginatedListResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }
}
