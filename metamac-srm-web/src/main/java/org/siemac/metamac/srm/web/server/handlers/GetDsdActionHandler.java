package org.siemac.metamac.srm.web.server.handlers;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties;
import org.siemac.metamac.srm.web.shared.GetDsdAction;
import org.siemac.metamac.srm.web.shared.GetDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdActionHandler extends AbstractActionHandler<GetDsdAction, GetDsdResult> {

    private static Logger        logger = Logger.getLogger(GetDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdActionHandler() {
        super(GetDsdAction.class);
    }

    @Override
    public GetDsdResult execute(GetDsdAction action, ExecutionContext context) throws ActionException {
        try {
            List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(DataStructureDefinitionProperties.id()).eq(action.getIdDsd()).build();
            List<DataStructureDefinitionDto> dsdList = srmCoreServiceFacade.findDsdByCondition(ServiceContextHolder.getCurrentServiceContext(), conditions, PagingParameter.pageAccess(10)).getValues();
            logger.log(Level.INFO, "ACTION SUCCESSFULLY: findDsdById");
            if (!dsdList.isEmpty()) {
                return new GetDsdResult(dsdList.get(0));
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
        return null;
    }

    @Override
    public void undo(GetDsdAction action, GetDsdResult result, ExecutionContext context) throws ActionException {

    }

}
