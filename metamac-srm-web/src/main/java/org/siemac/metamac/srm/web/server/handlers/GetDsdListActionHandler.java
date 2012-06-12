package org.siemac.metamac.srm.web.server.handlers;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.ArrayList;
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
import org.siemac.metamac.srm.web.shared.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.GetDsdListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdListActionHandler extends SecurityActionHandler<GetDsdListAction, GetDsdListResult> {

    private static Logger        logger = Logger.getLogger(GetDsdListActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdListActionHandler() {
        super(GetDsdListAction.class);
    }

    @Override
    public GetDsdListResult executeSecurityAction(GetDsdListAction action) throws ActionException {
        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).orderBy(DataStructureDefinitionProperties.lastUpdated()).descending().build();
        try {
            List<DataStructureDefinitionDto> dsdList = srmCoreServiceFacade.findDsdByCondition(ServiceContextHolder.getCurrentServiceContext(), conditions, PagingParameter.pageAccess(10)).getValues();
            logger.log(Level.INFO, "ACTION SUCCESSFULLY: getStatisticalOperationLastModifiedDsdList");
            return new GetDsdListResult((ArrayList<DataStructureDefinitionDto>) dsdList);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDsdByCondition =  " + conditions.toString() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDsdListAction action, GetDsdListResult result, ExecutionContext context) throws ActionException {
        // No undoable
    }

}
