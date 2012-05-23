package org.siemac.metamac.srm.web.server;

import org.siemac.metamac.srm.web.server.handlers.DeleteAttributeListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteConceptSchemeListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteDescriptorListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteDimensionListForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.DeleteDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.ExportDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindCodeListsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptSchemesActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindConceptsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.FindDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetConceptSchemeByIdLogicActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetConceptSchemePaginatedListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetDsdAndDescriptorsActionHandler;
import org.siemac.metamac.srm.web.server.handlers.GetDsdListActionHandler;
import org.siemac.metamac.srm.web.server.handlers.SaveComponentForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.SaveConceptSchemeActionHandler;
import org.siemac.metamac.srm.web.server.handlers.SaveDescriptorForDsdActionHandler;
import org.siemac.metamac.srm.web.server.handlers.SaveDsdActionHandler;
import org.siemac.metamac.srm.web.shared.DeleteAttributeListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDsdListAction;
import org.siemac.metamac.srm.web.shared.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemeByIdLogicAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemePaginatedListAction;
import org.siemac.metamac.srm.web.shared.GetDsdAction;
import org.siemac.metamac.srm.web.shared.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.SaveDsdAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.spring.HandlerModule;
import com.gwtplatform.dispatch.server.spring.actionvalidator.DefaultActionValidator;
import com.gwtplatform.dispatch.server.spring.configuration.DefaultModule;

/**
 * Module which binds the handlers and configurations.
 */
@Configuration
@Import(DefaultModule.class)
public class ServerModule extends HandlerModule {

    public ServerModule() {
    }

    @Bean
    public GetDsdListActionHandler getDsdListHandler() {
        return new GetDsdListActionHandler();
    }

    @Bean
    public GetDsdActionHandler getDsdHandler() {
        return new GetDsdActionHandler();
    }

    @Bean
    public GetDsdAndDescriptorsActionHandler getDsdAndDescriptorsActionHandler() {
        return new GetDsdAndDescriptorsActionHandler();
    }

    @Bean
    public FindDescriptorForDsdActionHandler getFindDescriptorForDsdActionHandler() {
        return new FindDescriptorForDsdActionHandler();
    }

    @Bean
    public FindConceptSchemesActionHandler getFindConceptSchemesActionHandler() {
        return new FindConceptSchemesActionHandler();
    }

    @Bean
    public FindConceptsActionHandler getFindConceptsActionHandler() {
        return new FindConceptsActionHandler();
    }

    @Bean
    public SaveDsdActionHandler getsSaveDsdActionHandler() {
        return new SaveDsdActionHandler();
    }

    @Bean
    public SaveComponentForDsdActionHandler getSaveComponentForDsdActionHandler() {
        return new SaveComponentForDsdActionHandler();
    }

    @Bean
    public SaveDescriptorForDsdActionHandler getSaveDescriptorForDsdActionHandler() {
        return new SaveDescriptorForDsdActionHandler();
    }

    @Bean
    public DeleteDsdActionHandler getDeleteDsdActionHandler() {
        return new DeleteDsdActionHandler();
    }

    @Bean
    public DeleteDsdListActionHandler getDeleteDsdListActionHandler() {
        return new DeleteDsdListActionHandler();
    }

    @Bean
    public DeleteComponentForDsdActionHandler getDeleteComponentForDsdActionHandler() {
        return new DeleteComponentForDsdActionHandler();
    }

    @Bean
    public DeleteDimensionListForDsdActionHandler getDeleteDimensionForDsdActionHandler() {
        return new DeleteDimensionListForDsdActionHandler();
    }

    @Bean
    public DeleteAttributeListForDsdActionHandler getDeleteAttributeListForDsdActionHandler() {
        return new DeleteAttributeListForDsdActionHandler();
    }

    @Bean
    public DeleteDescriptorForDsdActionHandler getDeleteDescriptorForDsdActionHandler() {
        return new DeleteDescriptorForDsdActionHandler();
    }

    @Bean
    public DeleteDescriptorListForDsdActionHandler getDeleteDescriptorListForDsdActionHandler() {
        return new DeleteDescriptorListForDsdActionHandler();
    }

    @Bean
    public FindCodeListsActionHandler getFindCodeListsActionHandler() {
        return new FindCodeListsActionHandler();
    }

    @Bean
    public GetConceptSchemePaginatedListActionHandler getConceptSchemePaginatedListActionHandler() {
        return new GetConceptSchemePaginatedListActionHandler();
    }

    @Bean
    public GetConceptSchemeByIdLogicActionHandler getConceptSchemeByIdLogicActionHandler() {
        return new GetConceptSchemeByIdLogicActionHandler();
    }

    @Bean
    public SaveConceptSchemeActionHandler getConceptSchemeActionHandler() {
        return new SaveConceptSchemeActionHandler();
    }

    @Bean
    public DeleteConceptSchemeListActionHandler getDeleteConceptSchemeListActionHandler() {
        return new DeleteConceptSchemeListActionHandler();
    }

    @Bean
    public ActionValidator getDefaultActionValidator() {
        return new DefaultActionValidator();
    }

    @Bean
    public ExportDsdActionHandler getExportDsdActionHandler() {
        return new ExportDsdActionHandler();
    }

    protected void configureHandlers() {
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
        bindHandler(GetConceptSchemePaginatedListAction.class, GetConceptSchemePaginatedListActionHandler.class);
        bindHandler(GetConceptSchemeByIdLogicAction.class, GetConceptSchemeByIdLogicActionHandler.class);
        bindHandler(SaveConceptSchemeAction.class, SaveConceptSchemeActionHandler.class);
        bindHandler(DeleteConceptSchemeListAction.class, DeleteConceptSchemeListActionHandler.class);

    }

}
