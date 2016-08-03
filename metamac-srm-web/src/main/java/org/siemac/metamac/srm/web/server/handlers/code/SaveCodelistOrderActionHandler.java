package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodelistOrderActionHandler extends SecurityActionHandler<SaveCodelistOrderAction, SaveCodelistOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistOrderActionHandler() {
        super(SaveCodelistOrderAction.class);
    }

    @Override
    public SaveCodelistOrderResult executeSecurityAction(SaveCodelistOrderAction action) throws ActionException {
        try {
            CodelistVisualisationDto orderToSave = action.getCodelistOrderVisualisationDto();
            CodelistVisualisationDto orderSaved = null;

            if (orderToSave.getId() == null) {
                // Create
                RelatedResourceDto codelist = RelatedResourceUtils.createRelatedResourceDto(RelatedResourceTypeEnum.CODELIST, action.getCodelistUrn());
                orderToSave.setCodelist(codelist);
                orderSaved = srmCoreServiceFacade.createCodelistOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            } else {
                // Update
                orderSaved = srmCoreServiceFacade.updateCodelistOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), orderToSave);
            }
            return new SaveCodelistOrderResult(orderSaved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
