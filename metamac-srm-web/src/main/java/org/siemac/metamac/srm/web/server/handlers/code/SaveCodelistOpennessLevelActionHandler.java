package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOpennessLevelAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOpennessLevelResult;
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
public class SaveCodelistOpennessLevelActionHandler extends SecurityActionHandler<SaveCodelistOpennessLevelAction, SaveCodelistOpennessLevelResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistOpennessLevelActionHandler() {
        super(SaveCodelistOpennessLevelAction.class);
    }

    @Override
    public SaveCodelistOpennessLevelResult executeSecurityAction(SaveCodelistOpennessLevelAction action) throws ActionException {
        try {
            CodelistVisualisationDto opennessLevelToSave = action.getCodelistOpennessVisualisationDto();
            CodelistVisualisationDto opennessLevelSaved = null;

            if (opennessLevelToSave.getId() == null) {
                // Create
                RelatedResourceDto codelist = RelatedResourceUtils.createRelatedResourceDto(RelatedResourceTypeEnum.CODELIST, action.getCodelistUrn());
                opennessLevelToSave.setCodelist(codelist);
                opennessLevelSaved = srmCoreServiceFacade.createCodelistOpennessVisualisation(ServiceContextHolder.getCurrentServiceContext(), opennessLevelToSave);
            } else {
                // Update
                opennessLevelSaved = srmCoreServiceFacade.updateCodelistOpennessVisualisation(ServiceContextHolder.getCurrentServiceContext(), opennessLevelToSave);
            }
            return new SaveCodelistOpennessLevelResult(opennessLevelSaved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
