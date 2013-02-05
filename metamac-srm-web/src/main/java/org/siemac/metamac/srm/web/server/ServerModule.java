package org.siemac.metamac.srm.web.server;

import org.siemac.metamac.srm.web.server.handlers.GetRelatedResourcesActionHandler;
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
import org.siemac.metamac.srm.web.server.handlers.code.AddCodelistsToCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.AddVariableElementsToVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.AddVariablesToVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CancelCodelistValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CreateVariableElementOperationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistOrdersActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableElementOperationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableElementsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariablesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistOrdersActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodesByCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementOperationsByVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementOperationsByVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariablesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.RemoveCodelistsFromCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.RemoveVariablesFromVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistOrderActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodeInOrderActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodeParentActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodelistProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.VersionCodelistActionHandler;
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
import org.siemac.metamac.srm.web.server.handlers.organisation.GetDefaultMaintainerActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationListBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeVersionListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.UpdateOrganisationSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.VersionOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
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
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddVariableElementsToVariableAction;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistListAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodeAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
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
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerAction;
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
        bindHandler(SaveDsdAction.class, SaveDsdActionHandler.class);
        bindHandler(SaveComponentForDsdAction.class, SaveComponentForDsdActionHandler.class);
        bindHandler(SaveDescriptorForDsdAction.class, SaveDescriptorForDsdActionHandler.class);
        bindHandler(DeleteDsdListAction.class, DeleteDsdListActionHandler.class);
        bindHandler(DeleteDimensionListForDsdAction.class, DeleteDimensionListForDsdActionHandler.class);
        bindHandler(DeleteAttributeListForDsdAction.class, DeleteAttributeListForDsdActionHandler.class);
        bindHandler(DeleteDescriptorListForDsdAction.class, DeleteDescriptorListForDsdActionHandler.class);
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
        bindHandler(GetDefaultMaintainerAction.class, GetDefaultMaintainerActionHandler.class);

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

        // Codes
        bindHandler(DeleteCodelistListAction.class, DeleteCodelistListActionHandler.class);
        bindHandler(GetCodelistAction.class, GetCodelistActionHandler.class);
        bindHandler(GetCodelistsAction.class, GetCodelistsActionHandler.class);
        bindHandler(GetCodelistVersionsAction.class, GetCodelistVersionsActionHandler.class);
        bindHandler(SaveCodelistAction.class, SaveCodelistActionHandler.class);
        bindHandler(UpdateCodelistProcStatusAction.class, UpdateCodelistProcStatusActionHandler.class);
        bindHandler(VersionCodelistAction.class, VersionCodelistActionHandler.class);
        bindHandler(CancelCodelistValidityAction.class, CancelCodelistValidityActionHandler.class);
        bindHandler(DeleteCodeAction.class, DeleteCodeActionHandler.class);
        bindHandler(GetCodeAction.class, GetCodeActionHandler.class);
        bindHandler(SaveCodeAction.class, SaveCodeActionHandler.class);
        bindHandler(GetCodesByCodelistAction.class, GetCodesByCodelistActionHandler.class);
        bindHandler(GetCodelistFamiliesAction.class, GetCodelistFamiliesActionHandler.class);
        bindHandler(SaveCodelistFamilyAction.class, SaveCodelistFamilyActionHandler.class);
        bindHandler(DeleteCodelistFamiliesAction.class, DeleteCodelistFamiliesActionHandler.class);
        bindHandler(GetCodelistFamilyAction.class, GetCodelistFamilyActionHandler.class);
        bindHandler(AddCodelistsToCodelistFamilyAction.class, AddCodelistsToCodelistFamilyActionHandler.class);
        bindHandler(RemoveCodelistsFromCodelistFamilyAction.class, RemoveCodelistsFromCodelistFamilyActionHandler.class);
        bindHandler(GetVariablesAction.class, GetVariablesActionHandler.class);
        bindHandler(GetVariableFamiliesAction.class, GetVariableFamiliesActionHandler.class);
        bindHandler(GetVariableFamilyAction.class, GetVariableFamilyActionHandler.class);
        bindHandler(GetVariableAction.class, GetVariableActionHandler.class);
        bindHandler(AddVariablesToVariableFamilyAction.class, AddVariablesToVariableFamilyActionHandler.class);
        bindHandler(RemoveVariablesFromVariableFamilyAction.class, RemoveVariablesFromVariableFamilyActionHandler.class);
        bindHandler(DeleteVariableFamiliesAction.class, DeleteVariableFamiliesActionHandler.class);
        bindHandler(DeleteVariablesAction.class, DeleteVariablesActionHandler.class);
        bindHandler(SaveVariableFamilyAction.class, SaveVariableFamilyActionHandler.class);
        bindHandler(SaveVariableAction.class, SaveVariableActionHandler.class);
        bindHandler(GetVariableElementAction.class, GetVariableElementActionHandler.class);
        bindHandler(SaveVariableElementAction.class, SaveVariableElementActionHandler.class);
        bindHandler(DeleteVariableElementsAction.class, DeleteVariableElementsActionHandler.class);
        bindHandler(GetVariableElementsAction.class, GetVariableElementsActionHandler.class);
        bindHandler(AddVariableElementsToVariableAction.class, AddVariableElementsToVariableActionHandler.class);
        bindHandler(GetCodelistOrdersAction.class, GetCodelistOrdersActionHandler.class);
        bindHandler(DeleteCodelistOrdersAction.class, DeleteCodelistOrdersActionHandler.class);
        bindHandler(SaveCodelistOrderAction.class, SaveCodelistOrderActionHandler.class);
        bindHandler(UpdateCodeInOrderAction.class, UpdateCodeInOrderActionHandler.class);
        bindHandler(UpdateCodeParentAction.class, UpdateCodeParentActionHandler.class);
        bindHandler(DeleteVariableElementOperationsAction.class, DeleteVariableElementOperationsActionHandler.class);
        bindHandler(CreateVariableElementOperationAction.class, CreateVariableElementOperationActionHandler.class);
        bindHandler(GetVariableElementOperationsByVariableAction.class, GetVariableElementOperationsByVariableActionHandler.class);
        bindHandler(GetVariableElementOperationsByVariableElementAction.class, GetVariableElementOperationsByVariableElementActionHandler.class);

        // Common
        bindHandler(GetUserGuideUrlAction.class, GetUserGuideUrlActionHandler.class);
        bindHandler(GetRelatedResourcesAction.class, GetRelatedResourcesActionHandler.class);

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
