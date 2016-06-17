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
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.utils.WaitingAsyncCallbackHandlingExportResult;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.presenter.OrganisationsToolStripPresenterWidget;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.StructuralResourcesRelationEnum;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorisationsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
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
import org.siemac.metamac.srm.web.shared.organisation.ExportOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.ExportOrganisationsResult;
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
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getOrganisationSchemeBreadCrumbTitle(placeRequest);
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
        void selectOrganisationSchemeTab();

        // Categorisations

        void setCategorisations(List<CategorisationDto> categorisationDtos);
        void setCategorySchemesForCategorisations(GetRelatedResourcesResult result);
        void setCategoriesForCategorisations(GetRelatedResourcesResult result);
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
        getView().selectOrganisationSchemeTab();
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
        dispatcher.execute(new GetOrganisationSchemeAction(urn), new WaitingAsyncCallbackHandlingError<GetOrganisationSchemeResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(OrganisationSchemePresenter.this, caught);
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
        dispatcher.execute(new GetOrganisationSchemeVersionsAction(organisationSchemeUrn), new WaitingAsyncCallbackHandlingError<GetOrganisationSchemeVersionsResult>(this) {

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
        dispatcher.execute(new GetOrganisationSchemesAction(0, 1, criteria), new WaitingAsyncCallbackHandlingError<GetOrganisationSchemesResult>(this) {

            @Override
            public void onWaitSuccess(GetOrganisationSchemesResult result) {
                getView().setLatestOrganisationSchemeForInternalPublication(result);
            }
        });
    }

    @Override
    public void saveOrganisationScheme(OrganisationSchemeMetamacDto organisationScheme) {
        dispatcher.execute(new SaveOrganisationSchemeAction(organisationScheme), new WaitingAsyncCallbackHandlingError<SaveOrganisationSchemeResult>(this) {

            @Override
            public void onWaitSuccess(SaveOrganisationSchemeResult result) {
                organisationSchemeMetamacDto = result.getOrganisationSchemeSaved();
                fireSuccessMessage(getMessages().organisationSchemeSaved());
                getView().setOrganisationScheme(organisationSchemeMetamacDto);

                updateUrl();
                retrieveOrganisationSchemeVersions(result.getOrganisationSchemeSaved().getUrn());
            }
        });
    }

    @Override
    public void deleteOrganisationScheme(String urn) {
        dispatcher.execute(new DeleteOrganisationSchemeListAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteOrganisationSchemeListResult>(this) {

            @Override
            public void onWaitSuccess(DeleteOrganisationSchemeListResult result) {
                fireSuccessMessage(getMessages().organisationSchemeDeleted());
                goTo(PlaceRequestUtils.buildAbsoluteOrganisationSchemesPlaceRequest());
            }
        });
    }

    @Override
    public void exportOrganisationScheme(String urn, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(Arrays.asList(urn), RelatedResourceTypeEnum.ORGANISATION_SCHEME, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void copyOrganisationScheme(String urn) {
        copyOrganisationScheme(urn, null);
    }

    @Override
    public void copyOrganisationScheme(String urn, String code) {
        dispatcher.execute(new CopyOrganisationSchemeAction(urn, code), new WaitingAsyncCallbackHandlingError<CopyOrganisationSchemeResult>(this) {

            @Override
            public void onWaitSuccess(CopyOrganisationSchemeResult result) {
                fireSuccessMessage(getMessages().maintainableArtefactCopied());
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
        dispatcher.execute(new CancelOrganisationSchemeValidityAction(urns), new WaitingAsyncCallbackHandlingError<CancelOrganisationSchemeValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelOrganisationSchemeValidityResult result) {
                fireSuccessMessage(getMessages().organisationSchemeCanceledValidity());
                retrieveOrganisationSchemeByUrn(urn);
            }
        });
    }

    @Override
    public void sendToProductionValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeType, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, organisationSchemeType, ProcStatusEnum.PRODUCTION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateOrganisationSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().organisationSchemeSentToProductionValidation());
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        retrieveOrganisationSchemeVersions(organisationSchemeMetamacDto.getUrn());
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeType, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, organisationSchemeType, ProcStatusEnum.DIFFUSION_VALIDATION, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateOrganisationSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().organisationSchemeSentToDiffusionValidation());
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        retrieveOrganisationSchemeVersions(organisationSchemeMetamacDto.getUrn());
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void rejectValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeType, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, organisationSchemeType, ProcStatusEnum.VALIDATION_REJECTED, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateOrganisationSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        fireSuccessMessage(getMessages().organisationSchemeRejected());
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        retrieveOrganisationSchemeVersions(organisationSchemeMetamacDto.getUrn());
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void publishInternally(final String urnToPublish, OrganisationSchemeTypeEnum organisationSchemeType, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urnToPublish, organisationSchemeType, ProcStatusEnum.INTERNALLY_PUBLISHED, currentProcStatus, forceLatestFinal),
                new WaitingAsyncCallbackHandlingError<UpdateOrganisationSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        firePublicationMessage(getMessages().organisationSchemePublishedInternally(), getMessages().organisationSchemePublishedInternallyWithNotificationError(),
                                result.getNotificationException());
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        retrieveOrganisationSchemeVersions(organisationSchemeMetamacDto.getUrn());
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
    public void publishExternally(String urn, OrganisationSchemeTypeEnum organisationSchemeType, ProcStatusEnum currentProcStatus) {
        dispatcher.execute(new UpdateOrganisationSchemeProcStatusAction(urn, organisationSchemeType, ProcStatusEnum.EXTERNALLY_PUBLISHED, currentProcStatus, null),
                new WaitingAsyncCallbackHandlingError<UpdateOrganisationSchemeProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateOrganisationSchemeProcStatusResult result) {
                        firePublicationMessage(getMessages().organisationSchemePublishedExternally(), getMessages().organisationSchemePublishedExternallyWithNotificationError(),
                                result.getNotificationException());
                        organisationSchemeMetamacDto = result.getOrganisationSchemeDto();
                        retrieveOrganisationSchemeVersions(organisationSchemeMetamacDto.getUrn());
                        getView().setOrganisationScheme(organisationSchemeMetamacDto);
                    }
                });
    }

    @Override
    public void versioning(String urn, VersionTypeEnum versionType) {
        dispatcher.execute(new VersionOrganisationSchemeAction(urn, versionType), new WaitingAsyncCallbackHandlingError<VersionOrganisationSchemeResult>(this) {

            @Override
            public void onWaitSuccess(VersionOrganisationSchemeResult result) {
                fireSuccessMessage(getMessages().organisationSchemeVersioned());
                organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                retrieveCompleteOrganisationSchemeByUrn(result.getOrganisationSchemeMetamacDto().getUrn());

                updateUrl();
            }
        });
    }

    @Override
    public void createTemporalVersion(String urn) {
        dispatcher.execute(new CreateOrganisationSchemeTemporalVersionAction(urn), new WaitingAsyncCallbackHandlingError<CreateOrganisationSchemeTemporalVersionResult>(this) {

            @Override
            public void onWaitSuccess(CreateOrganisationSchemeTemporalVersionResult result) {
                OrganisationSchemePresenter.this.organisationSchemeMetamacDto = result.getOrganisationSchemeMetamacDto();
                retrieveCompleteOrganisationSchemeByUrn(result.getOrganisationSchemeMetamacDto().getUrn(), false);
                updateUrl();
            }
        });
    }

    private void firePublicationMessage(String successMessage, String warningMessage, MetamacWebException notificationException) {
        if (notificationException == null) {
            ShowMessageEvent.fireSuccessMessage(this, successMessage);
        } else {
            ShowMessageEvent.fireWarningMessageWithError(this, warningMessage, notificationException);
        }
    }

    //
    // ORGANISATIONS
    //

    private void retrieveOrganisationsByScheme(String organisationSchemeUrn) {
        dispatcher.execute(new GetOrganisationsBySchemeAction(organisationSchemeUrn, ApplicationEditionLanguages.getCurrentLocale()),
                new WaitingAsyncCallbackHandlingError<GetOrganisationsBySchemeResult>(this) {

                    @Override
                    public void onWaitSuccess(GetOrganisationsBySchemeResult result) {
                        getView().setOrganisationList(result.getOrganisations());
                    }
                });
    }

    @Override
    public void createOrganisation(OrganisationMetamacDto organisationDto) {
        dispatcher.execute(new SaveOrganisationAction(organisationDto), new WaitingAsyncCallbackHandlingError<SaveOrganisationResult>(this) {

            @Override
            public void onWaitSuccess(SaveOrganisationResult result) {
                fireSuccessMessage(getMessages().organisationSaved());
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
        dispatcher.execute(new DeleteOrganisationsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteOrganisationsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteOrganisationsResult result) {
                fireSuccessMessage(getMessages().organisationDeleted());
                retrieveOrganisationsByScheme(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void exportOrganisations(String organisationSchemeUrn) {
        dispatcher.execute(new ExportOrganisationsAction(organisationSchemeUrn), new WaitingAsyncCallbackHandlingError<ExportOrganisationsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(OrganisationSchemePresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(ExportOrganisationsResult result) {
                org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
            }
        });
    }

    //
    // CATEGORISATIONS
    //

    @Override
    public void retrieveCategorisations(String artefactCategorisedUrn) {
        dispatcher.execute(new GetCategorisationsByArtefactAction(artefactCategorisedUrn), new WaitingAsyncCallbackHandlingError<GetCategorisationsByArtefactResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorisationsByArtefactResult result) {
                getView().setCategorisations(result.getCategorisationDtos());
            }
        });
    }

    @Override
    public void createCategorisations(List<String> categoryUrns) {
        dispatcher.execute(new CreateCategorisationAction(categoryUrns, organisationSchemeMetamacDto.getUrn(), RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto().getUrn()),
                new WaitingAsyncCallbackHandlingError<CreateCategorisationResult>(this) {

                    @Override
                    public void onWaitSuccess(CreateCategorisationResult result) {
                        fireSuccessMessage(getMessages().categorisationCreated());
                        retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
                    }
                });
    }

    @Override
    public void deleteCategorisations(List<String> urns) {
        dispatcher.execute(new DeleteCategorisationsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCategorisationsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategorisationsResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void exportCategorisations(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references) {
        dispatcher.execute(new ExportSDMXResourceAction(urns, RelatedResourceTypeEnum.CATEGORISATION, detail, references), new WaitingAsyncCallbackHandlingExportResult(this));
    }

    @Override
    public void cancelCategorisationValidity(List<String> urns, Date validTo) {
        dispatcher.execute(new CancelCategorisationValidityAction(urns, validTo), new WaitingAsyncCallbackHandlingError<CancelCategorisationValidityResult>(this) {

            @Override
            public void onWaitSuccess(CancelCategorisationValidityResult result) {
                fireSuccessMessage(getMessages().categorisationDeleted());
                retrieveCategorisations(organisationSchemeMetamacDto.getUrn());
            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, CategorySchemeWebCriteria categorySchemeWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CATEGORY_SCHEMES_FOR_CATEGORISATIONS, firstResult, maxResults, categorySchemeWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
                        getView().setCategorySchemesForCategorisations(result);
                    }
                });
    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, CategoryWebCriteria categoryWebCriteria) {
        dispatcher.execute(new GetRelatedResourcesAction(StructuralResourcesRelationEnum.CATEGORIES_FOR_CATEGORISATIONS, firstResult, maxResults, categoryWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetRelatedResourcesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetRelatedResourcesResult result) {
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

    //
    // IMPORTATION
    //

    @Override
    public void resourceImportationSucceed(String successMessage) {
        ShowMessageEvent.fireSuccessMessage(OrganisationSchemePresenter.this, successMessage);
        retrieveOrganisationSchemeByUrn(organisationSchemeMetamacDto.getUrn());
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(OrganisationSchemePresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }
}
