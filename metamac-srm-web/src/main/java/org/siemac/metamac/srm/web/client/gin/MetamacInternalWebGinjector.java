package org.siemac.metamac.srm.web.client.gin;

import org.siemac.metamac.srm.web.client.MetamacInternalWebConstants;
import org.siemac.metamac.srm.web.client.MetamacInternalWebMessages;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemePresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdAttributesTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdDimensionsTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGeneralTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdGroupKeysTabPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPresenter;
import org.siemac.metamac.srm.web.dsd.presenter.DsdPrimaryMeasureTabPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface MetamacInternalWebGinjector extends Ginjector {

    EventBus getEventBus();
    PlaceManager getPlaceManager();

    // Constants and messages interfaces
    public MetamacInternalWebConstants getMetamacInternalWebConstants();
    public MetamacInternalWebMessages getMetamacInternalWebMessages();

    Provider<MainPagePresenter> getMainPagePresenter();
    AsyncProvider<StructuralResourcesPresenter> getStructuralResourcesPresenter();

    // DSD
    // TODO: dev
    AsyncProvider<DsdListPresenter> getDsdListPresenter();
    AsyncProvider<DsdPresenter> getDsdPresenter();
    AsyncProvider<DsdGeneralTabPresenter> getDsdGeneralTabPresenter();
    AsyncProvider<DsdPrimaryMeasureTabPresenter> getDsdPrimaryMeasureTabPresenter();
    AsyncProvider<DsdDimensionsTabPresenter> getDsdDimensionsTabPresenter();
    AsyncProvider<DsdAttributesTabPresenter> getDsdAttributesTabPresenter();
    AsyncProvider<DsdGroupKeysTabPresenter> getDsdGroupKeysTabPresenter();

    // Concept
    AsyncProvider<ConceptSchemeListPresenter> getConceptSchemeListPresenter();
    AsyncProvider<ConceptSchemePresenter> getConceptSchemePresenter();

}
