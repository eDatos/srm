package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdVersionsActionHandler extends SecurityActionHandler<GetDsdVersionsAction, GetDsdVersionsResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdVersionsActionHandler() {
        super(GetDsdVersionsAction.class);
    }

    @Override
    public GetDsdVersionsResult executeSecurityAction(GetDsdVersionsAction action) throws ActionException {
        // try {
        // List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos = srmCoreServiceFacade.retrieveDataStructureDefinitionVersions(ServiceContextHolder.getCurrentServiceContext(),
        // action.getUrn());
        // return new GetDsdVersionsResult(dataStructureDefinitionMetamacDtos);
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
        return null;
    }

}
