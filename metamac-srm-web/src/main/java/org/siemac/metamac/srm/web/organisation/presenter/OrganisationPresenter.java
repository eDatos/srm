package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.presenter.OrganisationsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationContactsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationContactsResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
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
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class OrganisationPresenter extends Presenter<OrganisationPresenter.OrganisationView, OrganisationPresenter.OrganisationProxy> implements OrganisationUiHandlers {

    private final DispatchAsync                   dispatcher;
    private final PlaceManager                    placeManager;

    private String                                organisationSchemeUrn;
    private OrganisationMetamacDto                organisationMetamacDto;

    private OrganisationsToolStripPresenterWidget organisationsToolStripPresenterWidget;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisation();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationProxy extends Proxy<OrganisationPresenter>, Place {
    }

    public interface OrganisationView extends View, HasUiHandlers<OrganisationUiHandlers> {

        void setOrganisation(OrganisationMetamacDto organisationDto, Long contactToShowId);
        void setOrganisation(OrganisationMetamacDto organisationDto, OrganisationSchemeMetamacDto organisationSchemeMetamacDto, Long contactToShowId);
        void setContacts(List<ContactDto> contactDtos, Long contactToShowId);
        void setOrganisationList(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, List<ItemVisualisationResult> itemVisualisationResults);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentOrganisation         = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationsToolBar = new Object();

    @Inject
    public OrganisationPresenter(EventBus eventBus, OrganisationView view, OrganisationProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            OrganisationsToolStripPresenterWidget organisationsToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.organisationsToolStripPresenterWidget = organisationsToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentOrganisationsToolBar, organisationsToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().organisation());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.ORGANISATIONS);
        organisationsToolStripPresenterWidget.selectOrganisationsMenuButton(OrganisationsToolStripButtonEnum.ORGANISATIONS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String schemeParam = PlaceRequestUtils.getOrganisationSchemeIdParamFromUrl(placeManager);
        String schemeType = PlaceRequestUtils.getOrganisationSchemeTypeParamFromUrl(placeManager);
        String organisationCode = PlaceRequestUtils.getOrganisationParamFromUrl(placeManager);
        try {
            OrganisationSchemeTypeEnum type = schemeType != null ? OrganisationSchemeTypeEnum.valueOf(schemeType) : null;
            if (!StringUtils.isBlank(schemeParam) && type != null && !StringUtils.isBlank(organisationCode)) {
                this.organisationSchemeUrn = CommonUtils.generateOrganisationSchemeUrn(schemeParam, type);
                String urn = CommonUtils.generateOrganisationUrn(schemeParam, type, organisationCode);
                retrieveOrganisation(urn);
            } else {
                MetamacSrmWeb.showErrorPage();
            }
        } catch (Exception e) {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveOrganisation(String organisationUrn) {
        dispatcher.execute(new GetOrganisationAction(organisationUrn), new WaitingAsyncCallback<GetOrganisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationResult result) {
                organisationMetamacDto = result.getOrganisationDto();
                getView().setOrganisation(result.getOrganisationDto(), result.getOrganisationSchemeMetamacDto(), null);
            }
        });
    }

    @Override
    public void updateOrganisation(OrganisationMetamacDto organisationDto) {
        // If we are saving an organisation, contacts should not be updated
        List<ContactDto> contacts = new ArrayList<ContactDto>();
        contacts.addAll(this.organisationMetamacDto.getContacts());
        organisationDto.getContacts().clear();
        organisationDto.getContacts().addAll(contacts);

        saveOrganisation(organisationDto, null);
    }

    @Override
    public void retrieveContacts(OrganisationContactWebCriteria criteria) {
        dispatcher.execute(new GetOrganisationContactsAction(criteria), new WaitingAsyncCallback<GetOrganisationContactsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationContactErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationContactsResult result) {
                getView().setContacts(result.getContactDtos(), null);
            }
        });
    }

    @Override
    public void updateContacts(List<ContactDto> contactDtos, Long contactToUpdateId) {
        // if we are saving the organisation contacts, organisation metadata should not be updated
        List<ContactDto> contacts = new ArrayList<ContactDto>();
        contacts.addAll(contactDtos);
        this.organisationMetamacDto.getContacts().clear();
        this.organisationMetamacDto.getContacts().addAll(contacts);

        saveOrganisation(this.organisationMetamacDto, contactToUpdateId);
    }

    private void saveOrganisation(OrganisationMetamacDto organisationDto, final Long contactToUpdateId) {
        dispatcher.execute(new SaveOrganisationAction(organisationDto), new WaitingAsyncCallback<SaveOrganisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveOrganisationResult result) {
                organisationMetamacDto = result.getOrganisationSaved();
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSaved()), MessageTypeEnum.SUCCESS);
                getView().setOrganisation(result.getOrganisationSaved(), contactToUpdateId);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeOrganisationPlaceRequest(organisationMetamacDto.getUrn());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void createOrganisation(OrganisationMetamacDto organisationDto) {
        saveOrganisation(organisationDto, null);
    }

    @Override
    public void deleteOrganisation(final ItemVisualisationResult itemVisualisationResult) {
        List<String> urns = new ArrayList<String>();
        urns.add(itemVisualisationResult.getUrn());
        dispatcher.execute(new DeleteOrganisationsAction(urns), new WaitingAsyncCallback<DeleteOrganisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteOrganisationsResult result) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getMessageList(getMessages().organisationDeleted()), MessageTypeEnum.SUCCESS);
                // If deleted organisation had a organisation parent, go to this organisation parent. If not, go to the organisation scheme.
                if (itemVisualisationResult.getParent() != null && itemVisualisationResult.getParent().getUrn() != null) {
                    goToOrganisation(itemVisualisationResult.getParent().getUrn());
                } else {
                    goToOrganisationScheme(organisationSchemeUrn);
                }
            }
        });
    }

    @Override
    public void retrieveOrganisationListByScheme(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationsBySchemeAction(organisationSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetOrganisationsBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrievingOrganisationList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationsBySchemeResult result) {
                final List<ItemVisualisationResult> itemVisualisationResults = result.getOrganisations();
                dispatcher.execute(new GetOrganisationSchemeAction(OrganisationPresenter.this.organisationSchemeUrn), new WaitingAsyncCallback<GetOrganisationSchemeResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetOrganisationSchemeResult result) {
                        getView().setOrganisationList(result.getOrganisationSchemeMetamacDto(), itemVisualisationResults);
                    }
                });
            }
        });
    }

    @Override
    public void goToOrganisation(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationPlaceRequest(urn), -1);
        }
    }

    private void goToOrganisationScheme(String urn) {
        placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(urn, CommonUtils.getOrganisationSchemeTypeEnum(organisationMetamacDto.getType())), -2);

    }

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        throw new UnsupportedOperationException();
    }
}
