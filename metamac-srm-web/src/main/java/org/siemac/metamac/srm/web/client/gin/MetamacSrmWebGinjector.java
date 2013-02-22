package org.siemac.metamac.srm.web.client.gin;

import org.siemac.metamac.srm.web.category.presenter.CategoriesPresenter;
import org.siemac.metamac.srm.web.category.presenter.CategoryPresenter;
import org.siemac.metamac.srm.web.category.presenter.CategorySchemeListPresenter;
import org.siemac.metamac.srm.web.category.presenter.CategorySchemePresenter;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.presenter.ErrorPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.client.presenter.UnauthorizedPagePresenter;
import org.siemac.metamac.srm.web.code.presenter.CodePresenter;
import org.siemac.metamac.srm.web.code.presenter.CodelistFamilyListPresenter;
import org.siemac.metamac.srm.web.code.presenter.CodelistFamilyPresenter;
import org.siemac.metamac.srm.web.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.code.presenter.CodelistPresenter;
import org.siemac.metamac.srm.web.code.presenter.CodesPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariableElementPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariableElementsPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariableFamilyListPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariableFamilyPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariableListPresenter;
import org.siemac.metamac.srm.web.code.presenter.VariablePresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptsPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemeListPresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemePresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationsPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface MetamacSrmWebGinjector extends Ginjector {

    EventBus getEventBus();
    PlaceManager getPlaceManager();
    DispatchAsync getDispatcher();

    LoggedInGatekeeper getLoggedInGatekeeper();

    Provider<MainPagePresenter> getMainPagePresenter();
    AsyncProvider<StructuralResourcesPresenter> getStructuralResourcesPresenter();

    AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();
    AsyncProvider<UnauthorizedPagePresenter> getUnauthorizedPagePresenter();

    // DSDs
    AsyncProvider<DsdListPresenter> getDsdListPresenter();
    AsyncProvider<DsdPresenter> getDsdPresenter();
    AsyncProvider<DsdGeneralTabPresenter> getDsdGeneralTabPresenter();
    AsyncProvider<DsdPrimaryMeasureTabPresenter> getDsdPrimaryMeasureTabPresenter();
    AsyncProvider<DsdDimensionsTabPresenter> getDsdDimensionsTabPresenter();
    AsyncProvider<DsdAttributesTabPresenter> getDsdAttributesTabPresenter();
    AsyncProvider<DsdGroupKeysTabPresenter> getDsdGroupKeysTabPresenter();

    // Concepts
    AsyncProvider<ConceptSchemeListPresenter> getConceptSchemeListPresenter();
    AsyncProvider<ConceptSchemePresenter> getConceptSchemePresenter();
    AsyncProvider<ConceptsPresenter> getConceptsPresenter();
    AsyncProvider<ConceptPresenter> getConceptPresenter();

    // Organisations
    AsyncProvider<OrganisationSchemeListPresenter> getOrganisationSchemeListPresenter();
    AsyncProvider<OrganisationSchemePresenter> getOrganisationSchemePresenter();
    AsyncProvider<OrganisationsPresenter> getOrganisationsPresenter();
    AsyncProvider<OrganisationPresenter> getOrganisationPresenter();

    // Categories
    AsyncProvider<CategorySchemeListPresenter> getCategorySchemeListPresenter();
    AsyncProvider<CategorySchemePresenter> getCategorySchemePresenter();
    AsyncProvider<CategoryPresenter> getCategoryPresenter();
    AsyncProvider<CategoriesPresenter> getCategoriesPresenter();

    // Codes
    AsyncProvider<CodelistListPresenter> getCodelistListPresenter();
    AsyncProvider<CodelistPresenter> getCodelistPresenter();
    AsyncProvider<CodePresenter> getCodePresenter();
    AsyncProvider<CodesPresenter> getCodesPresenter();
    AsyncProvider<CodelistFamilyListPresenter> getCodelistFamilyListPresenter();
    AsyncProvider<CodelistFamilyPresenter> getCodelistFamilyPresenter();
    AsyncProvider<VariableFamilyListPresenter> getVariableFamilyListPresenter();
    AsyncProvider<VariableListPresenter> getVariableListPresenter();
    AsyncProvider<VariableFamilyPresenter> getVariableFamilyPresenter();
    AsyncProvider<VariablePresenter> getVariablePresenter();
    AsyncProvider<VariableElementPresenter> getVariableElementPresenter();
    AsyncProvider<VariableElementsPresenter> getVariableElementsPresenter();
}
