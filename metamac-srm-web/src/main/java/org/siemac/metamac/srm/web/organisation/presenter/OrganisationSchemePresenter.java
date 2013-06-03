package org.siemac.metamac.srm.web.organisation.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.ErrorUtils;
import org.siemac.metamac.srm.web.client.utils.MetamacWebCriteriaClientUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.presenter.OrganisationsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityResult;
import org.siemac.metamac.srm.web.shared.organisation.CopyOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.CopyOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.CreateOrganisationSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.organisation.CreateOrganisationSchemeTemporalVersionResult;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListResult;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionsResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationResult;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusResult;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
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

    private final DispatchAsync                   dispatcher;
    private final PlaceManager                    placeManager;

    private OrganisationSchemeMetamacDto          organisationSchemeMetamacDto;

    private OrganisationsToolStripPresenterWidget organisationsToolStripPresenterWidget;

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
        void setOrganisationSchemeVersions(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos);
        void setOrganisationList(List<OrganisationMetamacVisualisationResult> organisationDtos);
        void startOrganisationSchemeEdition();
        void setLatestOrganisationSchemeForInternalPublication(GetOrganisationSchemesResult result);

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetCategorySchemesResult result);
        void setCategoriesForCategorisations(GetCategoriesResult result);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentOrganisationScheme   = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentOrganisationsToolBar = new Object();

    @Inject
    public OrganisationSchemePresenter(EventBus eventBus, OrganisationSchemeView view, OrganisationSchemeProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            OrganisationsToolStripPresenterWidget organisationsToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.organisationsToolStripPresenterWidget = organisationsToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentOrganisationsToolBar, organisationsToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().organisationScheme());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.ORGANISATIONS);
        organisationsToolStripPresenterWidget.selectOrganisationsMenuButton(OrganisationsToolStripButtonEnum.ORGANISATION_SCHEMES);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String identifier = PlaceRequestUtils.getOrganisationSchemeIdParamFromUrl(placeManager);
        String typeParam = PlaceRequestUtils.getOrganisationSchemeTypeParamFromUrl(placeManager);
        try {
            OrganisationSchemeTypeEnum type = !StringUtils.isBlank(typeParam) ? OrganisationSchemeTypeEnum.valueOf(typeParam) : null;
            if (!StringUtils.isBlank(identifier) && type != null) {
                retrieveOrganisationScheme(identifier, type);
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
            retrieveCompleteOrganisationSchemeByUrn(urn);
        }
    }

    //
    // ORGANISATION SCHEME
    //

    private void retrieveCompleteOrganisationSchemeByUrn(String urn) {
        retrieveCompleteOrganisationSchemeByUrn(urn, false);
    }

    private void retrieveCompleteOrganisationSchemeByUrn(String urn, boolean startEdition) {
        retrieveOrganisationSchemeByUrn(urn, startEdition);
        retrieveCategorisations(urn);
    }

    private void retrieveOrganisationSchemeByUrn(String urn) {
        retrieveOrganisationSchemeByUrn(urn, false);
    }

    private void retrieveOrganisationSchemeByUrn(String urn, final boolean startEdition) {
        dispatcher.execute(new GetOrganisationSchemeAction(urn), new WaitingAsyncCallback<GetOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeResult result) {
                organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();

                getView().setOrganisationScheme(organisationSchemeMetamacDto);
                if (startEdition) {
                    getView().startOrganisationSchemeEdition();
                }

                retrieveOrganisationsByScheme(result.getOrganisationSchemeMetamacDto().getUrn());
                retrieveOrganisationSchemeVersions(result.getOrganisationSchemeMetamacDto().getUrn());
            }
        });
    }

    @Override
    public void retrieveOrganisationSchemeVersions(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationSchemeVersionsAction(organisationSchemeUrn), new WaitingAsyncCallback<GetOrganisationSchemeVersionsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrievingVersions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemeVersionsResult result) {
                getView().setOrganisationSchemeVersions(result.getOrganisationSchemeMetamacDtos());
            }
        });
    }

    @Override
    public void retrieveLatestOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        OrganisationSchemeWebCriteria criteria = new OrganisationSchemeWebCriteria();
        criteria.setCodeEQ(organisationSchemeMetamacDto.getCode());
        criteria.setMaintainerUrn(organisationSchemeMetamacDto.getMaintainer().getUrn());
        criteria.setIsLatestFinal(true);
        dispatcher.execute(new GetOrganisationSchemesAction(0, 1, criteria), new WaitingAsyncCallback<GetOrganisationSchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationSchemesResult result) {
                getView().setLatestOrganisationSchemeForInternalPublication(result);
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

                updateUrl();
            }
        });
    }

    @Override
    public void deleteOrganisationScheme(String urn) {
        dispatcher.execute(new DeleteOrganisationSchemeListAction(Arrays.asList(urn)), new WaitingAsyncCallback<DeleteOrganisationSchemeListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationSchemeErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteOrganisationSchemeListResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationSchemeDeleted()), MessageTypeEnum.SUCCESS);
                goTo(PlaceRequestUtils.buildAbsoluteOrganisationSchemesPlaceRequest());
            }
        });
    }

    @Override
    public void exportOrganisationScheme(String urn) {
        dispatcher.execute(new ExportSDMXResourceAction(urn), new WaitingAsyncCallback<ExportSDMXResourceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().resourceErrorExport()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(ExportSDMXResourceResult result) {
                org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
            }
        });
    }

    @Override
    public void copyOrganisationScheme(String urn) {
        dispatcher.execute(new CopyOrganisationSchemeAction(urn), new WaitingAsyncCallback<CopyOrganisationSchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().maintainableArtefactErrorCopy()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CopyOrganisationSchemeResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().maintainableArtefactCopied()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    //
    // ORGANISATION SCHEME LIFECYCLE
    //

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
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus, null),
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
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus, null),
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
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus, null),
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
    public void publishInternally(final String urnToPublish, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urnToPublish, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus, forceLatestFinal),
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

                        // If the version published was a temporal version, reload the complete organisation scheme and the URL. When a temporal version is published, is automatically converted into a
                        // normal version (the URN changes!).
                        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(urnToPublish)) {
                            retrieveCompleteOrganisationSchemeByUrn(organisationSchemeMetamacDto.getUrn());
                            updateUrl();
                        }
                    }
                });
    }

    @Override
    public void publishExternally(String urn, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus, null),
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
                retrieveCompleteOrganisationSchemeByUrn(result.getOrganisationSchemeMetamacDto().getUrn());

                updateUrl();
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateOrganisationSchemeTemporalVersionAction(urn), new WaitingAsyncCallback<CreateOrganisationSchemeTemporalVersionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().resourceErrorEditingPublishedResource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateOrganisationSchemeTemporalVersionResult result) {
                OrganisationSchemePresenter.this.organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                retrieveCompleteOrganisationSchemeByUrn(result.getOrganisationSchemeMetamacDto().getUrn(), true);
                updateUrl();
            }
        });
    }

    //
    // ORGANISATIONS
    //

    private void retrieveOrganisationsByScheme(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationsBySchemeAction(organisationSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()), new WaitingAsyncCallback<GetOrganisationsBySchemeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetOrganisationsBySchemeResult result) {
                getView().setOrganisationList(result.getOrganisations());
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
    public void deleteOrganisation(ItemVisualisationResult itemVisualisationResult) {
        List<String> organisationsToDelete = new ArrayList<String>();
        organisationsToDelete.add(itemVisualisationResult.getUrn());
        deleteOrganisations(organisationsToDelete);
    }

    @Override
    public void deleteOrganisations(List<String> urns) {
        dispatcher.execute(new DeleteOrganisationsAction(urns), new WaitingAsyncCallback<DeleteOrganisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().organisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteOrganisationsResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().organisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveOrganisationsByScheme(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    //
    // CATEGORISATIONS
    //

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallback<GetCategorisationsByArtefactResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, organisationSchemeMetamacDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallback<CreateCategorisationResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorCreate()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationCreated()), MessageTypeEnum.SUCCESS);
                        retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallback<DeleteCategorisationsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void cancelCategorisationValidity(String urn, Date validTo) {
        dispatcher.execute(new CancelCategorisationValidityAction(urn, validTo), new WaitingAsyncCallback<CancelCategorisationValidityResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorisationErrorCancelValidity()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CancelCategorisationValidityResult result) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getMessageList(getMessages().categorisationDeleted()), MessageTypeEnum.SUCCESS);
                retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, String criteria) {
        // The categories must be externally published
        CategorySchemeWebCriteria categorySchemeWebCriteria = new CategorySchemeWebCriteria(criteria);
        categorySchemeWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategorySchemeWebCriteria(categorySchemeWebCriteria);
        dispatcher.execute(new GetCategorySchemesAction(firstResult, maxResults, categorySchemeWebCriteria), new WaitingAsyncCallback<GetCategorySchemesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categorySchemeErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategorySchemesResult result) {
                getView().setCategorySchemesForCategorisations(result);
            }
        });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, String criteria, String categorySchemeUrn) {
        // The categories must be externally published
        CategoryWebCriteria categoryWebCriteria = new CategoryWebCriteria(criteria);
        categoryWebCriteria.setItemSchemeUrn(categorySchemeUrn);
        categoryWebCriteria = MetamacWebCriteriaClientUtils.addCategorisationConditionToCategoryWebCriteria(categoryWebCriteria);
        dispatcher.execute(new GetCategoriesAction(firstResult, maxResults, categoryWebCriteria), new WaitingAsyncCallback<GetCategoriesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(OrganisationSchemePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().categoryErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetCategoriesResult result) {
                getView().setCategoriesForCategorisations(result);
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }

    @Override
    public void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(urn, type), -1);
        }
    }

    @Override
    public void goToOrganisationScheme(String urn) {
        goTo(PlaceRequestUtils.buildAbsoluteOrganisationSchemePlaceRequest(urn, organisationSchemeMetamacDto.getType()));
    }

    @Override
    public void goToOrganisation(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOrganisationPlaceRequest(urn));
        }
    }

    private void updateUrl() {
        PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeOrganisationSchemePlaceRequest(organisationSchemeMetamacDto.getUrn(), organisationSchemeMetamacDto.getType());
        placeManager.updateHistory(placeRequest, true);
    }
}
