package org.siemac.metamac.srm.web.server;

import org.siemac.metamac.srm.web.server.handlers.FindCodeListsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.ValidateTicketActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.AnnounceConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.CancelConceptSchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptListBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeHistoryListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemePaginatedListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.UpdateConceptSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.VersionConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteAttributeListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDescriptorListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDimensionListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.ExportDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.FindDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdAndDescriptorsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.UpdateDsdProcStatusActionHandlder;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.AnnounceConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptListAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeHistoryListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.web.common.server.handlers.CloseSessionActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetEditionLanguagesActionHandlers;
import org.siemac.metamac.web.common.server.handlers.GetLoginPageUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetNavigationBarUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.MockCASUserActionHandler;
import org.siemac.metamac.web.common.shared.CloseSessionAction;
import org.siemac.metamac.web.common.shared.GetEditionLanguagesAction;
import org.siemac.metamac.web.common.shared.GetLoginPageUrlAction;
import org.siemac.metamac.web.common.shared.GetNavigationBarUrlAction;
import org.siemac.metamac.web.common.shared.MockCASUserAction;
import org.siemac.metamac.web.common.shared.ValidateTicketAction;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.spring.HandlerModule;

/**
 * Module which binds the handlers and configurations.
 */
@Component
public class ServerModule extends HandlerModule {

    public ServerModule() {
    }

    protected void configureHandlers() {
        // DSDs
        bindHandler(GetDsdListAction.class, GetDsdListActionHandler.class);
        bindHandler(GetDsdAction.class, GetDsdActionHandler.class);
        bindHandler(GetDsdAndDescriptorsAction.class, GetDsdAndDescriptorsActionHandler.class);
        bindHandler(FindDescriptorForDsdAction.class, FindDescriptorForDsdActionHandler.class);
        bindHandler(FindConceptSchemesAction.class, FindConceptSchemesActionHandler.class);
        bindHandler(FindConceptsAction.class, FindConceptsActionHandler.class);
        bindHandler(SaveDsdAction.class, SaveDsdActionHandler.class);
        bindHandler(SaveComponentForDsdAction.class, SaveComponentForDsdActionHandler.class);
        bindHandler(SaveDescriptorForDsdAction.class, SaveDescriptorForDsdActionHandler.class);
        bindHandler(DeleteDsdAction.class, DeleteDsdActionHandler.class);
        bindHandler(DeleteDsdListAction.class, DeleteDsdListActionHandler.class);
        bindHandler(DeleteComponentForDsdAction.class, DeleteComponentForDsdActionHandler.class);
        bindHandler(DeleteDimensionListForDsdAction.class, DeleteDimensionListForDsdActionHandler.class);
        bindHandler(DeleteAttributeListForDsdAction.class, DeleteAttributeListForDsdActionHandler.class);
        bindHandler(DeleteDescriptorForDsdAction.class, DeleteDescriptorForDsdActionHandler.class);
        bindHandler(DeleteDescriptorListForDsdAction.class, DeleteDescriptorListForDsdActionHandler.class);
        bindHandler(FindCodeListsAction.class, FindCodeListsActionHandler.class);
        bindHandler(ExportDsdAction.class, ExportDsdActionHandler.class);
        bindHandler(UpdateDsdProcStatusAction.class, UpdateDsdProcStatusActionHandlder.class);

        // Concept Schemes
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptSchemePaginatedListAction.class, GetConceptSchemePaginatedListActionHandler.class);
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptSchemeHistoryListAction.class, GetConceptSchemeHistoryListActionHandler.class);
        bindHandler(SaveConceptSchemeAction.class, SaveConceptSchemeActionHandler.class);
        bindHandler(DeleteConceptSchemeListAction.class, DeleteConceptSchemeListActionHandler.class);
        bindHandler(VersionConceptSchemeAction.class, VersionConceptSchemeActionHandler.class);
        bindHandler(GetConceptListBySchemeAction.class, GetConceptListBySchemeActionHandler.class);
        bindHandler(GetConceptAction.class, GetConceptActionHandler.class);
        bindHandler(SaveConceptAction.class, SaveConceptActionHandler.class);
        bindHandler(DeleteConceptListAction.class, DeleteConceptListActionHandler.class);
        bindHandler(UpdateConceptSchemeProcStatusAction.class, UpdateConceptSchemeProcStatusActionHandler.class);
        bindHandler(AnnounceConceptSchemeAction.class, AnnounceConceptSchemeActionHandler.class);
        bindHandler(CancelConceptSchemeValidityAction.class, CancelConceptSchemeValidityActionHandler.class);

        bindHandler(ValidateTicketAction.class, ValidateTicketActionHandler.class);
        bindHandler(GetLoginPageUrlAction.class, GetLoginPageUrlActionHandler.class);
        bindHandler(CloseSessionAction.class, CloseSessionActionHandler.class);
        bindHandler(GetNavigationBarUrlAction.class, GetNavigationBarUrlActionHandler.class);

        bindHandler(GetEditionLanguagesAction.class, GetEditionLanguagesActionHandlers.class);

        // This action should be removed to use CAS authentication
        bindHandler(MockCASUserAction.class, MockCASUserActionHandler.class);
    }

}
