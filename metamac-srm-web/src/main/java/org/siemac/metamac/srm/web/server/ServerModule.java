package org.siemac.metamac.srm.web.server;

import org.siemac.metamac.srm.web.server.handlers.FindCodeListsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetUserGuideUrlActionHandler;
import org.siemac.metamac.srm.web.server.handlers.ValidateTicketActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.CancelCategorySchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.DeleteCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.DeleteCategorySchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategoryListBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemeVersionListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.SaveCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.SaveCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.UpdateCategorySchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.VersionCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.AnnounceConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.CancelConceptSchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.FindAllConceptTypesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptListBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemePaginatedListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemesWithConceptsCanBeExtendedActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemesWithConceptsCanBeRoleActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsCanBeExtendedActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsCanBeRoleActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetStatisticalOperationsPaginatedListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.UpdateConceptSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.VersionConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.CancelDsdValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteAttributeListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDescriptorListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDimensionListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.ExportDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.FindDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdAndDescriptorsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.UpdateDsdProcStatusActionHandlder;
import org.siemac.metamac.srm.web.server.handlers.dsd.VersionDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.CancelOrganisationSchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.DeleteOrganisationListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.DeleteOrganisationSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationListBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeVersionListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.UpdateOrganisationSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.VersionOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemeListAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryListBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeListAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.AnnounceConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeAction;
import org.siemac.metamac.web.common.server.handlers.CloseSessionActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetLoginPageUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.GetNavigationBarUrlActionHandler;
import org.siemac.metamac.web.common.server.handlers.LoadConfigurationPropertiesActionHandler;
import org.siemac.metamac.web.common.server.handlers.MockCASUserActionHandler;
import org.siemac.metamac.web.common.shared.CloseSessionAction;
import org.siemac.metamac.web.common.shared.GetLoginPageUrlAction;
import org.siemac.metamac.web.common.shared.GetNavigationBarUrlAction;
import org.siemac.metamac.web.common.shared.LoadConfigurationPropertiesAction;
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

    @Override
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
        bindHandler(DeleteDsdListAction.class, DeleteDsdListActionHandler.class);
        bindHandler(DeleteDimensionListForDsdAction.class, DeleteDimensionListForDsdActionHandler.class);
        bindHandler(DeleteAttributeListForDsdAction.class, DeleteAttributeListForDsdActionHandler.class);
        bindHandler(DeleteDescriptorListForDsdAction.class, DeleteDescriptorListForDsdActionHandler.class);
        bindHandler(FindCodeListsAction.class, FindCodeListsActionHandler.class);
        bindHandler(ExportDsdAction.class, ExportDsdActionHandler.class);
        bindHandler(UpdateDsdProcStatusAction.class, UpdateDsdProcStatusActionHandlder.class);
        bindHandler(VersionDsdAction.class, VersionDsdActionHandler.class);
        bindHandler(GetDsdVersionsAction.class, GetDsdVersionsActionHandler.class);
        bindHandler(CancelDsdValidityAction.class, CancelDsdValidityActionHandler.class);

        // Concepts
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptSchemePaginatedListAction.class, GetConceptSchemePaginatedListActionHandler.class);
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptSchemeVersionsAction.class, GetConceptSchemeVersionsActionHandler.class);
        bindHandler(SaveConceptSchemeAction.class, SaveConceptSchemeActionHandler.class);
        bindHandler(DeleteConceptSchemeListAction.class, DeleteConceptSchemeListActionHandler.class);
        bindHandler(VersionConceptSchemeAction.class, VersionConceptSchemeActionHandler.class);
        bindHandler(GetConceptListBySchemeAction.class, GetConceptListBySchemeActionHandler.class);
        bindHandler(GetConceptAction.class, GetConceptActionHandler.class);
        bindHandler(SaveConceptAction.class, SaveConceptActionHandler.class);
        bindHandler(DeleteConceptAction.class, DeleteConceptActionHandler.class);
        bindHandler(UpdateConceptSchemeProcStatusAction.class, UpdateConceptSchemeProcStatusActionHandler.class);
        bindHandler(AnnounceConceptSchemeAction.class, AnnounceConceptSchemeActionHandler.class);
        bindHandler(CancelConceptSchemeValidityAction.class, CancelConceptSchemeValidityActionHandler.class);
        bindHandler(FindAllConceptTypesAction.class, FindAllConceptTypesActionHandler.class);
        bindHandler(GetConceptSchemesWithConceptsCanBeRoleAction.class, GetConceptSchemesWithConceptsCanBeRoleActionHandler.class);
        bindHandler(GetConceptSchemesWithConceptsCanBeExtendedAction.class, GetConceptSchemesWithConceptsCanBeExtendedActionHandler.class);
        bindHandler(GetConceptsCanBeRoleAction.class, GetConceptsCanBeRoleActionHandler.class);
        bindHandler(GetConceptsCanBeExtendedAction.class, GetConceptsCanBeExtendedActionHandler.class);

        // Organisations
        bindHandler(GetOrganisationSchemeListAction.class, GetOrganisationSchemeListActionHandler.class);
        bindHandler(GetOrganisationSchemeAction.class, GetOrganisationSchemeActionHandler.class);
        bindHandler(UpdateOrganisationSchemeProcStatusAction.class, UpdateOrganisationSchemeProcStatusActionHandler.class);
        bindHandler(GetOrganisationSchemeVersionListAction.class, GetOrganisationSchemeVersionListActionHandler.class);
        bindHandler(SaveOrganisationSchemeAction.class, SaveOrganisationSchemeActionHandler.class);
        bindHandler(CancelOrganisationSchemeValidityAction.class, CancelOrganisationSchemeValidityActionHandler.class);
        bindHandler(VersionOrganisationSchemeAction.class, VersionOrganisationSchemeActionHandler.class);
        bindHandler(DeleteOrganisationSchemeListAction.class, DeleteOrganisationSchemeListActionHandler.class);
        bindHandler(GetOrganisationListBySchemeAction.class, GetOrganisationListBySchemeActionHandler.class);
        bindHandler(SaveOrganisationAction.class, SaveOrganisationActionHandler.class);
        bindHandler(DeleteOrganisationListAction.class, DeleteOrganisationListActionHandler.class);
        bindHandler(GetOrganisationAction.class, GetOrganisationActionHandler.class);

        // Categories
        bindHandler(GetCategorySchemeListAction.class, GetCategorySchemeListActionHandler.class);
        bindHandler(SaveCategorySchemeAction.class, SaveCategorySchemeActionHandler.class);
        bindHandler(CancelCategorySchemeValidityAction.class, CancelCategorySchemeValidityActionHandler.class);
        bindHandler(DeleteCategorySchemeListAction.class, DeleteCategorySchemeListActionHandler.class);
        bindHandler(UpdateCategorySchemeProcStatusAction.class, UpdateCategorySchemeProcStatusActionHandler.class);
        bindHandler(VersionCategorySchemeAction.class, VersionCategorySchemeActionHandler.class);
        bindHandler(GetCategorySchemeVersionListAction.class, GetCategorySchemeVersionListActionHandler.class);
        bindHandler(GetCategorySchemeAction.class, GetCategorySchemeActionHandler.class);
        bindHandler(GetCategoryListBySchemeAction.class, GetCategoryListBySchemeActionHandler.class);
        bindHandler(DeleteCategoryAction.class, DeleteCategoryActionHandler.class);
        bindHandler(SaveCategoryAction.class, SaveCategoryActionHandler.class);
        bindHandler(GetCategoryAction.class, GetCategoryActionHandler.class);

        // Common
        bindHandler(GetUserGuideUrlAction.class, GetUserGuideUrlActionHandler.class);

        bindHandler(ValidateTicketAction.class, ValidateTicketActionHandler.class);
        bindHandler(GetLoginPageUrlAction.class, GetLoginPageUrlActionHandler.class);
        bindHandler(CloseSessionAction.class, CloseSessionActionHandler.class);
        bindHandler(GetNavigationBarUrlAction.class, GetNavigationBarUrlActionHandler.class);

        // Rest
        bindHandler(GetStatisticalOperationsPaginatedListAction.class, GetStatisticalOperationsPaginatedListActionHandler.class);

        bindHandler(LoadConfigurationPropertiesAction.class, LoadConfigurationPropertiesActionHandler.class);

        // This action should be removed to use CAS authentication
        bindHandler(MockCASUserAction.class, MockCASUserActionHandler.class);
    }

}
