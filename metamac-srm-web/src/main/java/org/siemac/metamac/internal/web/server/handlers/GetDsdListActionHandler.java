package org.siemac.metamac.internal.web.server.handlers;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.core_structure.domain.DataStructureDefinitionProperties.lastUpdated;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core_facades.serviceapi.SDMXStructureServiceFacade;
import org.siemac.metamac.core_structure.domain.DataStructureDefinition;
import org.siemac.metamac.domain_dto.DataStructureDefinitionDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.GetDsdListAction;
import org.siemac.metamac.internal.web.shared.GetDsdListResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDsdListActionHandler extends AbstractActionHandler<GetDsdListAction, GetDsdListResult> {

    private static Logger              logger = Logger.getLogger(GetDsdListActionHandler.class.getName());

    @Autowired
    private SDMXStructureServiceFacade sDMXStructureServiceFacade;

    public GetDsdListActionHandler() {
        super(GetDsdListAction.class);
    }

    @Override
    public GetDsdListResult execute(GetDsdListAction action, ExecutionContext context) throws ActionException {
        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).orderBy(lastUpdated()).descending().build();
        try {
            List<DataStructureDefinitionDto> dsdList = sDMXStructureServiceFacade.findDsdByCondition(ServiceContextHelper.getServiceContext(), conditions, PagingParameter.pageAccess(10)).getValues();
            logger.log(Level.INFO, "ACTION SUCCESSFULLY: getStatisticalOperationLastModifiedDsdList");
            return new GetDsdListResult((ArrayList<DataStructureDefinitionDto>) dsdList);
        }
        catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDsdByCondition =  " + conditions.toString() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDsdListAction action, GetDsdListResult result, ExecutionContext context) throws ActionException {
        // No undoable
    }

}
