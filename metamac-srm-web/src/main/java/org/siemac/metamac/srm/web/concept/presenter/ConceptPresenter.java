package org.siemac.metamac.srm.web.concept.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.UrnUtils;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ConceptPresenter extends Presenter<ConceptPresenter.ConceptView, ConceptPresenter.ConceptProxy> implements ConceptUiHandlers {

    private final DispatchAsync      dispatcher;
    private final PlaceManager       placeManager;
    private ToolStripPresenterWidget toolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbConcept();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.conceptPage)
    public interface ConceptProxy extends Proxy<ConceptPresenter>, Place {
    }

    public interface ConceptView extends View, HasUiHandlers<ConceptUiHandlers> {

        void setConcept(ConceptDto conceptDto);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentConcept        = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentConceptToolBar = new Object();

    @Inject
    public ConceptPresenter(EventBus eventBus, ConceptView view, ConceptProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentConceptToolBar, toolStripPresenterWidget);
        MainPagePresenter.getMasterHead().setTitleLabel(MetamacSrmWeb.getConstants().concept());
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String schemeParam = PlaceRequestUtils.getConceptSchemeParamFromUrl(placeManager);
        String conceptCode = PlaceRequestUtils.getConceptParamFromUrl(placeManager);
        if (!StringUtils.isBlank(schemeParam) && !StringUtils.isBlank(conceptCode)) {
            String urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_CONCEPT_PREFIX, schemeParam, conceptCode);
            retrieveConcept(urn);
        }
    }

    @Override
    public void retrieveConcept(String conceptUrn) {
        dispatcher.execute(new GetConceptAction(conceptUrn), new WaitingAsyncCallback<GetConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorRetrievingData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetConceptResult result) {
                getView().setConcept(result.getConceptDto());
            }
        });
    }

    @Override
    public void saveConcept(ConceptDto conceptDto) {
        dispatcher.execute(new SaveConceptAction(conceptDto), new WaitingAsyncCallback<SaveConceptResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(ConceptPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().conceptErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveConceptResult result) {
                getView().setConcept(result.getConceptDto());
            }
        });
    }

}
