package org.siemac.metamac.srm.web.server;

import org.siemac.metamac.srm.web.server.handlers.ExportSDMXResourceActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetRelatedResourcesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetUserGuideUrlActionHandler;
import org.siemac.metamac.srm.web.server.handlers.ValidateTicketActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.CancelCategorySchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.CopyCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.CreateCategorisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.CreateCategorySchemeTemporalVersionActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.DeleteCategorisationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.DeleteCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.DeleteCategorySchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategoriesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategoriesBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorisationsByArtefactActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemeVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.GetCategorySchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.SaveCategoryActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.SaveCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.UpdateCategorySchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.category.VersionCategorySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.AddCodelistsToCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.AddVariableElementsToVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.AddVariablesToVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CancelCodelistValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CopyCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CopyCodesInCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CreateCodelistTemporalVersionActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.CreateVariableElementOperationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistOpennessLevelsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistOrdersActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteCodelistsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableElementOperationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableElementsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariableFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.DeleteVariablesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.ExportCodesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.ExportCodesOrderActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistOpennessLevelsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistOrdersActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodelistsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetCodesByCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementOperationsByVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementOperationsByVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableElementsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableFamiliesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.GetVariablesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.NormaliseVariableElementsToCodesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.RemoveCodelistsFromCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.RemoveVariablesFromVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistOpennessLevelActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveCodelistOrderActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.SaveVariableFamilyActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodeInOrderActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodeParentActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodeVariableElementActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodelistProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodesInOpennessVisualisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.UpdateCodesVariableElementsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.code.VersionCodelistActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.AnnounceConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.CancelConceptSchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.CopyConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.CreateConceptSchemeTemporalVersionActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.DeleteConceptSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.FindAllConceptTypesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemeVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemesWithConceptsCanBeExtendedActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptSchemesWithConceptsCanBeRoleActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsCanBeExtendedActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetConceptsCanBeRoleActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.GetStatisticalOperationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.SaveConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.UpdateConceptSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.concept.VersionConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.CancelDsdValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.CopyDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.CreateDsdTemporalVersionActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteAttributesForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDescriptorListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDimensionListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.DeleteDsdsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDefaultDimensionForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdAndDescriptorsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdDimensionsAndCandidateVisualisationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.GetDsdsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.SaveDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.dsd.UpdateDsdProcStatusActionHandlder;
import org.siemac.metamac.srm.web.server.handlers.dsd.VersionDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.CancelOrganisationSchemeValidityActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.CopyOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.CreateOrganisationSchemeTemporalVersionActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.DeleteOrganisationSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.DeleteOrganisationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetDefaultMaintainerActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationContactsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemeVersionsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.GetOrganisationsBySchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.SaveOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.UpdateOrganisationSchemeProcStatusActionHandler;
import org.siemac.metamac.srm.web.server.handlers.organisation.VersionOrganisationSchemeActionHandler;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorySchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddVariableElementsToVariableAction;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CopyCodelistAction;
import org.siemac.metamac.srm.web.shared.code.CopyCodesInCodelistAction;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesOrderAction;
import org.siemac.metamac.srm.web.shared.code.GetCodeAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.NormaliseVariableElementsToCodesAction;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOpennessLevelAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInOpennessVisualisationAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
import org.siemac.metamac.srm.web.shared.concept.AnnounceConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CopyCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CopyConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CreateConceptSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.FindAllConceptTypesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeExtendedAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CopyDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.CreateDsdTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributesForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultDimensionForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdDimensionsAndCandidateVisualisationsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdVersionsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CopyOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.CreateOrganisationSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationContactsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeAction;
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
        bindHandler(GetDsdsAction.class, GetDsdsActionHandler.class);
        bindHandler(GetDsdAction.class, GetDsdActionHandler.class);
        bindHandler(GetDsdAndDescriptorsAction.class, GetDsdAndDescriptorsActionHandler.class);
        bindHandler(SaveDsdAction.class, SaveDsdActionHandler.class);
        bindHandler(SaveComponentForDsdAction.class, SaveComponentForDsdActionHandler.class);
        bindHandler(SaveDescriptorForDsdAction.class, SaveDescriptorForDsdActionHandler.class);
        bindHandler(DeleteDsdsAction.class, DeleteDsdsActionHandler.class);
        bindHandler(DeleteDimensionListForDsdAction.class, DeleteDimensionListForDsdActionHandler.class);
        bindHandler(DeleteAttributesForDsdAction.class, DeleteAttributesForDsdActionHandler.class);
        bindHandler(DeleteDescriptorListForDsdAction.class, DeleteDescriptorListForDsdActionHandler.class);
        bindHandler(ExportSDMXResourceAction.class, ExportSDMXResourceActionHandler.class);
        bindHandler(UpdateDsdProcStatusAction.class, UpdateDsdProcStatusActionHandlder.class);
        bindHandler(VersionDsdAction.class, VersionDsdActionHandler.class);
        bindHandler(GetDsdVersionsAction.class, GetDsdVersionsActionHandler.class);
        bindHandler(CancelDsdValidityAction.class, CancelDsdValidityActionHandler.class);
        bindHandler(CreateDsdTemporalVersionAction.class, CreateDsdTemporalVersionActionHandler.class);
        bindHandler(GetDefaultDimensionForDsdAction.class, GetDefaultDimensionForDsdActionHandler.class);
        bindHandler(GetDsdDimensionsAndCandidateVisualisationsAction.class, GetDsdDimensionsAndCandidateVisualisationsActionHandler.class);
        bindHandler(CopyDsdAction.class, CopyDsdActionHandler.class);

        // Concepts
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptSchemesAction.class, GetConceptSchemesActionHandler.class);
        bindHandler(GetConceptSchemeAction.class, GetConceptSchemeActionHandler.class);
        bindHandler(GetConceptsAction.class, GetConceptsActionHandler.class);
        bindHandler(GetConceptSchemeVersionsAction.class, GetConceptSchemeVersionsActionHandler.class);
        bindHandler(SaveConceptSchemeAction.class, SaveConceptSchemeActionHandler.class);
        bindHandler(DeleteConceptSchemesAction.class, DeleteConceptSchemesActionHandler.class);
        bindHandler(VersionConceptSchemeAction.class, VersionConceptSchemeActionHandler.class);
        bindHandler(GetConceptsBySchemeAction.class, GetConceptsBySchemeActionHandler.class);
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
        bindHandler(CreateConceptSchemeTemporalVersionAction.class, CreateConceptSchemeTemporalVersionActionHandler.class);
        bindHandler(CopyConceptSchemeAction.class, CopyConceptSchemeActionHandler.class);

        // Organisations
        bindHandler(GetOrganisationSchemesAction.class, GetOrganisationSchemesActionHandler.class);
        bindHandler(GetOrganisationSchemeAction.class, GetOrganisationSchemeActionHandler.class);
        bindHandler(UpdateOrganisationSchemeProcStatusAction.class, UpdateOrganisationSchemeProcStatusActionHandler.class);
        bindHandler(GetOrganisationSchemeVersionsAction.class, GetOrganisationSchemeVersionsActionHandler.class);
        bindHandler(SaveOrganisationSchemeAction.class, SaveOrganisationSchemeActionHandler.class);
        bindHandler(CancelOrganisationSchemeValidityAction.class, CancelOrganisationSchemeValidityActionHandler.class);
        bindHandler(VersionOrganisationSchemeAction.class, VersionOrganisationSchemeActionHandler.class);
        bindHandler(DeleteOrganisationSchemeListAction.class, DeleteOrganisationSchemeListActionHandler.class);
        bindHandler(GetOrganisationsBySchemeAction.class, GetOrganisationsBySchemeActionHandler.class);
        bindHandler(GetOrganisationsAction.class, GetOrganisationsActionHandler.class);
        bindHandler(SaveOrganisationAction.class, SaveOrganisationActionHandler.class);
        bindHandler(DeleteOrganisationsAction.class, DeleteOrganisationsActionHandler.class);
        bindHandler(GetOrganisationAction.class, GetOrganisationActionHandler.class);
        bindHandler(GetDefaultMaintainerAction.class, GetDefaultMaintainerActionHandler.class);
        bindHandler(GetOrganisationContactsAction.class, GetOrganisationContactsActionHandler.class);
        bindHandler(CreateOrganisationSchemeTemporalVersionAction.class, CreateOrganisationSchemeTemporalVersionActionHandler.class);
        bindHandler(CopyOrganisationSchemeAction.class, CopyOrganisationSchemeActionHandler.class);

        // Categories
        bindHandler(GetCategorySchemesAction.class, GetCategorySchemesActionHandler.class);
        bindHandler(SaveCategorySchemeAction.class, SaveCategorySchemeActionHandler.class);
        bindHandler(CancelCategorySchemeValidityAction.class, CancelCategorySchemeValidityActionHandler.class);
        bindHandler(DeleteCategorySchemesAction.class, DeleteCategorySchemesActionHandler.class);
        bindHandler(UpdateCategorySchemeProcStatusAction.class, UpdateCategorySchemeProcStatusActionHandler.class);
        bindHandler(VersionCategorySchemeAction.class, VersionCategorySchemeActionHandler.class);
        bindHandler(GetCategorySchemeVersionsAction.class, GetCategorySchemeVersionsActionHandler.class);
        bindHandler(GetCategorySchemeAction.class, GetCategorySchemeActionHandler.class);
        bindHandler(GetCategoriesBySchemeAction.class, GetCategoriesBySchemeActionHandler.class);
        bindHandler(DeleteCategoryAction.class, DeleteCategoryActionHandler.class);
        bindHandler(SaveCategoryAction.class, SaveCategoryActionHandler.class);
        bindHandler(GetCategoryAction.class, GetCategoryActionHandler.class);
        bindHandler(CreateCategorisationAction.class, CreateCategorisationActionHandler.class);
        bindHandler(GetCategorisationAction.class, GetCategorisationActionHandler.class);
        bindHandler(DeleteCategorisationsAction.class, DeleteCategorisationsActionHandler.class);
        bindHandler(GetCategorisationsByArtefactAction.class, GetCategorisationsByArtefactActionHandler.class);
        bindHandler(GetCategoriesAction.class, GetCategoriesActionHandler.class);
        bindHandler(CreateCategorySchemeTemporalVersionAction.class, CreateCategorySchemeTemporalVersionActionHandler.class);
        bindHandler(CopyCategorySchemeAction.class, CopyCategorySchemeActionHandler.class);

        // Codes
        bindHandler(DeleteCodelistsAction.class, DeleteCodelistsActionHandler.class);
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
        bindHandler(DeleteCodelistOpennessLevelsAction.class, DeleteCodelistOpennessLevelsActionHandler.class);
        bindHandler(SaveCodelistOrderAction.class, SaveCodelistOrderActionHandler.class);
        bindHandler(SaveCodelistOpennessLevelAction.class, SaveCodelistOpennessLevelActionHandler.class);
        bindHandler(UpdateCodeInOrderAction.class, UpdateCodeInOrderActionHandler.class);
        bindHandler(UpdateCodeParentAction.class, UpdateCodeParentActionHandler.class);
        bindHandler(DeleteVariableElementOperationsAction.class, DeleteVariableElementOperationsActionHandler.class);
        bindHandler(CreateVariableElementOperationAction.class, CreateVariableElementOperationActionHandler.class);
        bindHandler(GetVariableElementOperationsByVariableAction.class, GetVariableElementOperationsByVariableActionHandler.class);
        bindHandler(GetVariableElementOperationsByVariableElementAction.class, GetVariableElementOperationsByVariableElementActionHandler.class);
        bindHandler(UpdateCodeVariableElementAction.class, UpdateCodeVariableElementActionHandler.class);
        bindHandler(GetCodesAction.class, GetCodesActionHandler.class);
        bindHandler(CopyCodesInCodelistAction.class, CopyCodesInCodelistActionHandler.class);
        bindHandler(GetCodelistOpennessLevelsAction.class, GetCodelistOpennessLevelsActionHandler.class);
        bindHandler(UpdateCodesInOpennessVisualisationAction.class, UpdateCodesInOpennessVisualisationActionHandler.class);
        bindHandler(CreateCodelistTemporalVersionAction.class, CreateCodelistTemporalVersionActionHandler.class);
        bindHandler(NormaliseVariableElementsToCodesAction.class, NormaliseVariableElementsToCodesActionHandler.class);
        bindHandler(UpdateCodesVariableElementsAction.class, UpdateCodesVariableElementsActionHandler.class);
        bindHandler(UpdateCodesVariableElementsAction.class, UpdateCodesVariableElementsActionHandler.class);
        bindHandler(CopyCodelistAction.class, CopyCodelistActionHandler.class);
        bindHandler(ExportCodesAction.class, ExportCodesActionHandler.class);
        bindHandler(ExportCodesOrderAction.class, ExportCodesOrderActionHandler.class);

        // Common
        bindHandler(GetUserGuideUrlAction.class, GetUserGuideUrlActionHandler.class);
        bindHandler(GetRelatedResourcesAction.class, GetRelatedResourcesActionHandler.class);

        bindHandler(ValidateTicketAction.class, ValidateTicketActionHandler.class);
        bindHandler(GetLoginPageUrlAction.class, GetLoginPageUrlActionHandler.class);
        bindHandler(CloseSessionAction.class, CloseSessionActionHandler.class);
        bindHandler(GetNavigationBarUrlAction.class, GetNavigationBarUrlActionHandler.class);

        // Rest
        bindHandler(GetStatisticalOperationsAction.class, GetStatisticalOperationsActionHandler.class);

        bindHandler(LoadConfigurationPropertiesAction.class, LoadConfigurationPropertiesActionHandler.class);

        // This action should be removed to use CAS authentication
        bindHandler(MockCASUserAction.class, MockCASUserActionHandler.class);
    }
}
