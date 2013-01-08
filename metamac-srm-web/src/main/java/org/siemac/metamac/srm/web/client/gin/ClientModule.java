package org.siemac.metamac.srm.web.client.gin;

import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacPlaceManager;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.category.presenter.CategoryPresenter;
import org.siemac.metamac.srm.web.client.category.presenter.CategorySchemeListPresenter;
import org.siemac.metamac.srm.web.client.category.presenter.CategorySchemePresenter;
import org.siemac.metamac.srm.web.client.category.view.CategorySchemeListViewImpl;
import org.siemac.metamac.srm.web.client.category.view.CategorySchemeViewImpl;
import org.siemac.metamac.srm.web.client.category.view.CategoryViewImpl;
import org.siemac.metamac.srm.web.client.code.presenter.CodePresenter;
import org.siemac.metamac.srm.web.client.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.client.code.presenter.CodelistPresenter;
import org.siemac.metamac.srm.web.client.code.view.CodeViewImpl;
import org.siemac.metamac.srm.web.client.code.view.CodelistListViewImpl;
import org.siemac.metamac.srm.web.client.code.view.CodelistViewImpl;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.code.widgets.CodesToolStripViewImpl;
import org.siemac.metamac.srm.web.client.presenter.ErrorPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.client.presenter.UnauthorizedPagePresenter;
import org.siemac.metamac.srm.web.client.view.ErrorPageViewImpl;
import org.siemac.metamac.srm.web.client.view.MainPageViewImpl;
import org.siemac.metamac.srm.web.client.view.StructuralResourcesViewImpl;
import org.siemac.metamac.srm.web.client.view.UnauthorizedPageViewImpl;
import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.concept.view.ConceptSchemeListViewImpl;
import org.siemac.metamac.srm.web.concept.view.ConceptSchemeViewImpl;
import org.siemac.metamac.srm.web.concept.view.ConceptViewImpl;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter;
import org.siemac.metamac.srm.web.dsd.view.DsdAttributesTabViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdDimensionsTabViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdGeneralTabViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdGroupKeysTabViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdListViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdPrimaryMeasureTabViewImpl;
import org.siemac.metamac.srm.web.dsd.view.DsdViewImpl;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemeListPresenter;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemePresenter;
import org.siemac.metamac.srm.web.organisation.view.OrganisationSchemeListViewImpl;
import org.siemac.metamac.srm.web.organisation.view.OrganisationSchemeViewImpl;
import org.siemac.metamac.srm.web.organisation.view.OrganisationViewImpl;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;

import com.google.inject.Singleton;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        // Default implementation of standard resources
        // |_ bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        // |_ bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        // |_ bind(RootPresenter.class).asEagerSingleton();
        // |_ bind(PlaceManager.class).to(MyPlaceManager.class).in(Singleton.class);
        // |_ bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
        install(new DefaultModule(MetamacPlaceManager.class));

        // Default place
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.structuralResourcesPage);

        // Security cookie to protect against XSRF attacks
        bindConstant().annotatedWith(SecurityCookie.class).to(SharedTokens.securityCookieName);

        // PresenterWidgets
        bindSingletonPresenterWidget(CodesToolStripPresenterWidget.class, CodesToolStripPresenterWidget.CodesToolStripView.class, CodesToolStripViewImpl.class);

        // Gate keeper
        bind(LoggedInGatekeeper.class).in(Singleton.class);

        // Main presenters
        bindPresenter(MainPagePresenter.class, MainPagePresenter.MainPageView.class, MainPageViewImpl.class, MainPagePresenter.MainPageProxy.class);
        bindPresenter(StructuralResourcesPresenter.class, StructuralResourcesPresenter.StructuralResourcesView.class, StructuralResourcesViewImpl.class,
                StructuralResourcesPresenter.StructuralResourcesProxy.class);

        // Error pages
        bindPresenter(ErrorPagePresenter.class, ErrorPagePresenter.ErrorPageView.class, ErrorPageViewImpl.class, ErrorPagePresenter.ErrorPageProxy.class);
        bindPresenter(UnauthorizedPagePresenter.class, UnauthorizedPagePresenter.UnauthorizedPageView.class, UnauthorizedPageViewImpl.class, UnauthorizedPagePresenter.UnauthorizedPageProxy.class);

        // DSDs
        bindPresenter(DsdListPresenter.class, DsdListPresenter.DsdListView.class, DsdListViewImpl.class, DsdListPresenter.DsdListProxy.class);
        bindPresenter(DsdPresenter.class, DsdPresenter.DsdView.class, DsdViewImpl.class, DsdPresenter.DsdProxy.class);
        bindPresenter(DsdGeneralTabPresenter.class, DsdGeneralTabPresenter.DsdGeneralTabView.class, DsdGeneralTabViewImpl.class, DsdGeneralTabPresenter.DsdGeneralTabProxy.class);
        bindPresenter(DsdPrimaryMeasureTabPresenter.class, DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabView.class, DsdPrimaryMeasureTabViewImpl.class,
                DsdPrimaryMeasureTabPresenter.DsdPrimaryMeasureTabProxy.class);
        bindPresenter(DsdDimensionsTabPresenter.class, DsdDimensionsTabPresenter.DsdDimensionsTabView.class, DsdDimensionsTabViewImpl.class, DsdDimensionsTabPresenter.DsdDimensionsTabProxy.class);
        bindPresenter(DsdAttributesTabPresenter.class, DsdAttributesTabPresenter.DsdAttributesTabView.class, DsdAttributesTabViewImpl.class, DsdAttributesTabPresenter.DsdAttributesTabProxy.class);
        bindPresenter(DsdGroupKeysTabPresenter.class, DsdGroupKeysTabPresenter.DsdGroupKeysTabView.class, DsdGroupKeysTabViewImpl.class, DsdGroupKeysTabPresenter.DsdGroupKeysTabProxy.class);

        // Concepts
        bindPresenter(ConceptSchemeListPresenter.class, ConceptSchemeListPresenter.ConceptSchemeListView.class, ConceptSchemeListViewImpl.class,
                ConceptSchemeListPresenter.ConceptSchemeListProxy.class);
        bindPresenter(ConceptSchemePresenter.class, ConceptSchemePresenter.ConceptSchemeView.class, ConceptSchemeViewImpl.class, ConceptSchemePresenter.ConceptSchemeProxy.class);
        bindPresenter(ConceptPresenter.class, ConceptPresenter.ConceptView.class, ConceptViewImpl.class, ConceptPresenter.ConceptProxy.class);

        // Organisations
        bindPresenter(OrganisationSchemeListPresenter.class, OrganisationSchemeListPresenter.OrganisationSchemeListView.class, OrganisationSchemeListViewImpl.class,
                OrganisationSchemeListPresenter.OrganisationSchemeListProxy.class);
        bindPresenter(OrganisationSchemePresenter.class, OrganisationSchemePresenter.OrganisationSchemeView.class, OrganisationSchemeViewImpl.class,
                OrganisationSchemePresenter.OrganisationSchemeProxy.class);
        bindPresenter(OrganisationPresenter.class, OrganisationPresenter.OrganisationView.class, OrganisationViewImpl.class, OrganisationPresenter.OrganisationProxy.class);

        // Categories
        bindPresenter(CategorySchemeListPresenter.class, CategorySchemeListPresenter.CategorySchemeListView.class, CategorySchemeListViewImpl.class,
                CategorySchemeListPresenter.CategorySchemeListProxy.class);
        bindPresenter(CategorySchemePresenter.class, CategorySchemePresenter.CategorySchemeView.class, CategorySchemeViewImpl.class, CategorySchemePresenter.CategorySchemeProxy.class);
        bindPresenter(CategoryPresenter.class, CategoryPresenter.CategoryView.class, CategoryViewImpl.class, CategoryPresenter.CategoryProxy.class);

        // Codes
        bindPresenter(CodelistListPresenter.class, CodelistListPresenter.CodelistListView.class, CodelistListViewImpl.class, CodelistListPresenter.CodelistListProxy.class);
        bindPresenter(CodelistPresenter.class, CodelistPresenter.CodelistView.class, CodelistViewImpl.class, CodelistPresenter.CodelistProxy.class);
        bindPresenter(CodePresenter.class, CodePresenter.CodeView.class, CodeViewImpl.class, CodePresenter.CodeProxy.class);
    }
}
