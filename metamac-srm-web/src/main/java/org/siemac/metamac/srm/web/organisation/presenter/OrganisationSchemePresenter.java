package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.NameTokens;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationListResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
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

public class OrganisationSchemePresenter extends Presenter<OrganisationSchemePresenter.OrganisationSchemeView, OrganisationSchemePresenter.OrganisationSchemeProxy>
        implements
            OrganisationSchemeUiHandlers {

    private final DispatchAsync          dispatcher;
    private final PlaceManager           placeManager;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbOrganisationScheme();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.organisationSchemePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OrganisationSchemeProxy extends Proxy<OrganisationSchemePresenter>, Place {
    }

    public interface OrganisationSchemeView extends View, HasUiHandlers<OrganisationSchemeUiHandlers> {

        void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto);
        void setOrganisationSchemeVersions(List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos);
        void setOrganisationList(List<ItemHierarchyDto> organisationDtos);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentOrganisationScheme = new Type<RevealContentHandler<?>>();

    @Inject
    public OrganisationSchemePresenter(EventBus eventBus, OrganisationSchemeView view, OrganisationSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SetTitleEvent.fire(this, getConstants().organisationScheme());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.ORGANISATIONS);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String urn = PlaceRequestUtils.getOrganisationSchemeIdParamFromUrl(placeManager);
        String typeParam = PlaceRequestUtils.getOrganisationSchemeTypeParamFromUrl(placeManager);
        try {
            OrganisationSchemeTypeEnum type = !StringUtils.isBlank(typeParam) ? OrganisationSchemeTypeEnum.valueOf(typeParam) : null;
            if (!StringUtils.isBlank(urn) && type != null) {
                retrieveOrganisationScheme(urn, type);
            } else {
                MetamacSrmWeb.showErrorPage();
            }
        } catch (Exception e) {
            MetamacSrmWeb.showErrorPage();
        }
    }

    @Override
    public void retrieveOrganisationScheme(String identifier, OrganisationSchemeTypeEnum type) {
        // Set title depending on organisation scheme
        switch (type) {
            case AGENCY_SCHEME:
                SetTitleEvent.fire(this, getCoreMessages().organisationSchemeTypeEnumAGENCY_SCHEME());
                break;
            case ORGANISATION_UNIT_SCHEME:
                SetTitleEvent.fire(this, getCoreMessages().organisationSchemeTypeEnumORGANISATION_UNIT_SCHEME());
                break;
            case DATA_PROVIDER_SCHEME:
                SetTitleEvent.fire(this, getCoreMessages().organisationSchemeTypeEnumDATA_PROVIDER_SCHEME());
                break;
            case DATA_CONSUMER_SCHEME:
                SetTitleEvent.fire(this, getCoreMessages().organisationSchemeTypeEnumDATA_CONSUMER_SCHEME());
                break;
            default:
                break;
        }
        // Retrieve organisation scheme by URN
        String urn = CommonUtils.generateOrganisationSchemeUrn(identifier, type);
        if (!StringUtils.isBlank(urn)) {
            retrieveOrganisationSchemeByUrn(urn);
        }
    }

    private void retrieveOrganisationSchemeByUrn(String urn) {
        dispatcher.execute(new GetOrganisationSchemeAction(urn), new WaitingAsyncCallback<GetOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeResult result) {
                organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                getView().setOrganisationScheme(organisationSchemeMetamacDto);
                retrieveOrganisationListByScheme(result.getOrganisationSchemeMetamacDto().getUrn());
                retrieveOrganisationSchemeVersions(result.getOrganisationSchemeMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveOrganisationListByScheme(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationListBySchemeAction(organisationSchemeUrn), new WaitingAsyncCallback<GetOrganisationListBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationListBySchemeResult result) {
                getView().setOrganisationList(result.getOrganisations());
            }
        });
    }

    @Override
    public void retrieveOrganisationSchemeVersions(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationSchemeVersionListAction(organisationSchemeUrn), new WaitingAsyncCallback<GetOrganisationSchemeVersionListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeVersionListResult result) {
                getView().setOrganisationSchemeVersions(result.getOrganisationSchemeMetamacDtos());
            }
        });
    }

    @Override
    public void saveOrganisationScheme(OrganisationSchemeMetamacDto organisationScheme) {
        dispatcher.execute(new SaveOrganisationSchemeAction(organisationScheme), new WaitingAsyncCallback<SaveOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveOrganisationSchemeResult result) {
                organisationSchemeMetamacDto = result.getOrganisationSchemeSaved();
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSaved()), MessageTypeEnum.SUCCESS);
                getView().setOrganisationScheme(organisationSchemeMetamacDto);

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(organisationSchemeMetamacDto.getUrn(), organisationSchemeMetamacDto.getType());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void cancelValidity(final String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new CancelOrganisationSchemeValidityAction(urns), new WaitingAsyncCallback<CancelOrganisationSchemeValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelOrganisationSchemeValidityResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeCanceledValidity()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationSchemeByUrn(urn);
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus),
                new WaitingAsyncCallback<UpdateOrganisationSchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorSendingToProductionValidation()),
                                MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus),
                new WaitingAsyncCallback<UpdateOrganisationSchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorSendingToDiffusionValidation()),
                                MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void rejectValidation(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus),
                new WaitingAsyncCallback<UpdateOrganisationSchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRejecting()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeRejected()), MessageTypeEnum.SUCCESS);
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void publishInternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus),
                new WaitingAsyncCallback<UpdateOrganisationSchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorPublishingInternally()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemePublishedInternally()), MessageTypeEnum.SUCCESS);
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus),
                new WaitingAsyncCallback<UpdateOrganisationSchemeProcStatusResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorPublishingExternally()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemePublishedExternally()), MessageTypeEnum.SUCCESS);
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionOrganisationSchemeAction(urn, versionType), new WaitingAsyncCallback<VersionOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorVersioning()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersionOrganisationSchemeResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeVersioned()), MessageTypeEnum.SUCCESS);
                organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                retrieveOrganisationSchemeByUrn(organisationSchemeMetamacDto.getUrn());

                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(organisationSchemeMetamacDto.getUrn(), organisationSchemeMetamacDto.getType());
                placeManager.updateHistory(placeRequest, true);
            }
        });
    }

    @Override
    public void createOrganisation(OrganisationMetamacDto organisationDto) {
        dispatcher.execute(new SaveOrganisationAction(organisationDto), new WaitingAsyncCallback<SaveOrganisationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveOrganisationResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSaved()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationSchemeByUrn(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void deleteOrganisation(ItemDto itemDto) {
        List<String> organisationsToDelete = new ArrayList<String>();
        organisationsToDelete.add(itemDto.getUrn());
        deleteOrganisations(organisationsToDelete);
    }

    @Override
    public void deleteOrganisations(List<String> urns) {
        dispatcher.execute(new DeleteOrganisationListAction(urns), new WaitingAsyncCallback<DeleteOrganisationListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteOrganisationListResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationListByScheme(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(urn, type), -1);
        }
    }

    @Override
    public void goToOrganisation(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationPlaceRequest(urn));
        }
    }

}
