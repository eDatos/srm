package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.utils.RequiredFieldUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.client.widgets.CopyResourceWindow;
import org.siemac.metamac.srm.web.client.widgets.CustomTabSet;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.dsd.widgets.ExportSdmxResourceWindow;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemePresenter;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsFormUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeCategorisationsPanel;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeMainFormLayout;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeOrganisationsPanel;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrganisationSchemeViewImpl extends ViewWithUiHandlers<OrganisationSchemeUiHandlers> implements OrganisationSchemePresenter.OrganisationSchemeView {

    private final TitleLabel titleLabel;
    private final VLayout panel;
    private final OrganisationSchemeMainFormLayout mainFormLayout;

    private final CustomTabSet tabSet;
    private final Tab organisationSchemeTab;

    // View forms
    private GroupDynamicForm identifiersForm;
    private GroupDynamicForm contentDescriptorsForm;
    private GroupDynamicForm productionDescriptorsForm;
    private GroupDynamicForm diffusionDescriptorsForm;
    private GroupDynamicForm versionResponsibilityForm;
    private GroupDynamicForm commentsForm;
    private AnnotationsPanel annotationsPanel;

    // Edition forms
    private GroupDynamicForm identifiersEditionForm;
    private GroupDynamicForm contentDescriptorsEditionForm;
    private GroupDynamicForm productionDescriptorsEditionForm;
    private GroupDynamicForm diffusionDescriptorsEditionForm;
    private GroupDynamicForm versionResponsibilityEditionForm;
    private GroupDynamicForm commentsEditionForm;
    private AnnotationsPanel annotationsEditionPanel;

    // Versions
    private final OrganisationSchemeVersionsSectionStack versionsSectionStack;

    // Organisation
    private final OrganisationSchemeOrganisationsPanel organisationSchemeOrganisationsPanel;

    // Categorisations
    private final OrganisationSchemeCategorisationsPanel categorisationsPanel;

    private OrganisationSchemeMetamacDto organisationSchemeDto;

    @Inject
    public OrganisationSchemeViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        //
        // ORGANISATION SCHEME VERSIONS
        //

        versionsSectionStack = new OrganisationSchemeVersionsSectionStack(getConstants().organisationSchemeVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((OrganisationSchemeRecord) event.getRecord()).getUrn();
                OrganisationSchemeTypeEnum type = ((OrganisationSchemeRecord) event.getRecord()).getOrganisationSchemeBasicDto().getType();
                getUiHandlers().goToOrganisationScheme(urn, type);
            }
        });

        //
        // ORGANISATION SCHEME
        //

        mainFormLayout = new OrganisationSchemeMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // ORGANISATIONS
        //

        organisationSchemeOrganisationsPanel = new OrganisationSchemeOrganisationsPanel();

        // CATEGORISATIONS

        categorisationsPanel = new OrganisationSchemeCategorisationsPanel();

        // PANEL LAYOUT

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        titleLabel = new TitleLabel();

        tabSet = new CustomTabSet();

        // OrganisationScheme tab
        organisationSchemeTab = new Tab(getConstants().organisationScheme());
        organisationSchemeTab.setPane(mainFormLayout);
        tabSet.addTab(organisationSchemeTab);

        // Organisations tab
        Tab organisationsTab = new Tab(getConstants().organisations());
        organisationsTab.setPane(organisationSchemeOrganisationsPanel);
        tabSet.addTab(organisationsTab);

        // Categorisations tab
        Tab categorisationsTab = new Tab(getConstants().categorisations());
        categorisationsTab.setPane(categorisationsPanel);
        tabSet.addTab(categorisationsTab);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(titleLabel);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);
        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(OrganisationSchemeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationSchemeOrganisationsPanel.setUiHandlers(uiHandlers);
        categorisationsPanel.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                contentDescriptorsForm.setTranslationsShowed(translationsShowed);
                contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                productionDescriptorsForm.setTranslationsShowed(translationsShowed);
                productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                versionResponsibilityForm.setTranslationsShowed(translationsShowed);
                versionResponsibilityEditionForm.setTranslationsShowed(translationsShowed);

                commentsForm.setTranslationsShowed(translationsShowed);
                commentsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsPanel.setTranslationsShowed(translationsShowed);
                annotationsEditionPanel.setTranslationsShowed(translationsShowed);
            }
        });

        // Edit
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                startOrganisationSchemeEdition();
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteOrganisationScheme(organisationSchemeDto.getUrn());
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && productionDescriptorsEditionForm.validate(false)
                        && diffusionDescriptorsEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getOrganisationSchemeDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveOrganisationScheme(getOrganisationSchemeDto());
                        }
                    });
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                publishOrganisationSchemeInternally();
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getCreateTemporalVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().createTemporalVersion(organisationSchemeDto.getUrn());
            }
        });
        mainFormLayout.getConsolidateVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                versionOrganisationScheme();
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(organisationSchemeDto.getUrn());
            }
        });
        mainFormLayout.getExport().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showExportationWindow(organisationSchemeDto.getUrn());
            }

            protected void showExportationWindow(final String urn) {
                new ExportSdmxResourceWindow() {

                    @Override
                    protected void startExportation(ExportDetailEnum infoAmount, ExportReferencesEnum references) {
                        getUiHandlers().exportOrganisationScheme(urn, infoAmount, references);
                    }
                };
            }
        });
        mainFormLayout.getCopy().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().copyOrganisationScheme(organisationSchemeDto.getUrn());
            }
        });
        mainFormLayout.getCopyKeepingMaintainer().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                copyOrganisationSchemeAskingCode(organisationSchemeDto.getUrn());
            }
        });
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem uriProvider = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, uriProvider, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(OrganisationSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsForm.setFields(type, description, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(OrganisationSchemeDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem creationUser = new ViewTextItem(OrganisationSchemeDS.VERSION_CREATION_USER, getConstants().maintainableArtefactVersionCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(OrganisationSchemeDS.VERSION_LAST_UPDATE_DATE, getConstants().maintainableArtefactVersionLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(OrganisationSchemeDS.VERSION_LAST_UPDATE_USER, getConstants().maintainableArtefactVersionLastUpdateUser());
        ViewTextItem resourceCreationDate = new ViewTextItem(OrganisationSchemeDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsForm.setFields(agency, procStatus, creationDate, creationUser, lastUpdateDate, lastUpdateUser, resourceCreationDate);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(OrganisationSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(OrganisationSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsForm.setFields(replacedBy, replaceTo, validFrom, validTo);

        // Version responsibility
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // Comments
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(OrganisationSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // Annotations
        annotationsPanel = new AnnotationsPanel(true);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(contentDescriptorsForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(versionResponsibilityForm);
        mainFormLayout.addViewCanvas(commentsForm);
        mainFormLayout.addViewCanvas(annotationsPanel);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getOrganisationSchemeIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(OrganisationSchemeDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem uriProvider = new ViewTextItem(OrganisationSchemeDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(OrganisationSchemeDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(OrganisationSchemeDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, uriProvider, urn, urnProvider, version);

        // Content descriptors
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewTextItem type = new ViewTextItem(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        MultiLanguageRichTextEditorItem description = new MultiLanguageRichTextEditorItem(OrganisationSchemeDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(OrganisationSchemeDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(OrganisationSchemeDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        contentDescriptorsEditionForm.setFields(type, description, partial, isExternalReference, isFinal);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(OrganisationSchemeDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(OrganisationSchemeDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem creationUser = new ViewTextItem(OrganisationSchemeDS.VERSION_CREATION_USER, getConstants().maintainableArtefactVersionCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(OrganisationSchemeDS.VERSION_LAST_UPDATE_DATE, getConstants().maintainableArtefactVersionLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(OrganisationSchemeDS.VERSION_LAST_UPDATE_USER, getConstants().maintainableArtefactVersionLastUpdateUser());
        ViewTextItem resourceCreationDate = new ViewTextItem(OrganisationSchemeDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsEditionForm.setFields(agency, procStatus, creationDate, creationUser, lastUpdateDate, lastUpdateUser, resourceCreationDate);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem replacedBy = new ViewTextItem(OrganisationSchemeDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(OrganisationSchemeDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(OrganisationSchemeDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(OrganisationSchemeDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsEditionForm.setFields(replacedBy, replaceTo, validFrom, validTo);

        // Version responsibility
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // Comments
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultiLanguageRichTextEditorItem comments = new MultiLanguageRichTextEditorItem(OrganisationSchemeDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // Annotations
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationSchemePresenter.TYPE_SetContextAreaContentOrganisationsToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void selectOrganisationSchemeTab() {
        tabSet.selectTab(organisationSchemeTab);
    }

    @Override
    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        this.organisationSchemeDto = organisationSchemeMetamacDto;

        // Set title
        titleLabel.setContents(org.siemac.metamac.srm.web.client.utils.CommonUtils.getResourceTitle(organisationSchemeMetamacDto));

        // Security
        mainFormLayout.setOrganisationScheme(organisationSchemeMetamacDto);
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(organisationSchemeMetamacDto);

        setOrganisationSchemeViewMode(organisationSchemeMetamacDto);
        setOrganisationSchemeEditionMode(organisationSchemeMetamacDto);

        organisationSchemeOrganisationsPanel.updateItemScheme(organisationSchemeMetamacDto);
    }

    @Override
    public void setOrganisationSchemeVersions(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos) {
        versionsSectionStack.setOrganisationSchemes(organisationSchemeMetamacDtos);
        versionsSectionStack.selectOrganisationScheme(organisationSchemeDto);
    }

    @Override
    public void setOrganisationList(List<OrganisationMetamacVisualisationResult> organisations) {
        organisationSchemeOrganisationsPanel.setOrganisationList(organisationSchemeDto, organisations);
    }

    @Override
    public void startOrganisationSchemeEdition() {
        mainFormLayout.setEditionMode();
    }

    @Override
    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetRelatedResourcesResult result) {
        categorisationsPanel.setCategories(result);
    }

    public void setOrganisationSchemeViewMode(OrganisationSchemeMetamacDto organisationSchemeDto) {
        // Identifiers
        identifiersForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeDto.getCode());
        identifiersForm.setValue(OrganisationSchemeDS.URI, organisationSchemeDto.getUriProvider());
        identifiersForm.setValue(OrganisationSchemeDS.URN, organisationSchemeDto.getUrn());
        identifiersForm.setValue(OrganisationSchemeDS.URN_PROVIDER, organisationSchemeDto.getUrnProvider());
        identifiersForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeDto.getVersionLogic());
        identifiersForm.setValue(OrganisationSchemeDS.NAME, organisationSchemeDto.getName());

        // Content descriptors
        contentDescriptorsForm.setValue(OrganisationSchemeDS.TYPE, CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.DESCRIPTION, organisationSchemeDto.getDescription());
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(organisationSchemeDto.getIsPartial()));
        contentDescriptorsForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, organisationSchemeDto.getIsExternalReference() != null
                ? (organisationSchemeDto.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no())
                : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeDto.getFinalLogic() != null
                ? (organisationSchemeDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no())
                : StringUtils.EMPTY);

        // Production descriptors
        ((RelatedResourceLinkItem) productionDescriptorsForm.getItem(OrganisationSchemeDS.MAINTAINER)).setRelatedResource(organisationSchemeDto.getMaintainer());
        productionDescriptorsForm.setValue(OrganisationSchemeDS.PROC_STATUS,
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()));
        productionDescriptorsForm.setValue(OrganisationSchemeDS.VERSION_CREATION_DATE, organisationSchemeDto.getCreatedDate());
        productionDescriptorsForm.setValue(OrganisationSchemeDS.VERSION_CREATION_USER, organisationSchemeDto.getCreatedBy());
        productionDescriptorsForm.setValue(OrganisationSchemeDS.VERSION_LAST_UPDATE_DATE, organisationSchemeDto.getLastUpdated());
        productionDescriptorsForm.setValue(OrganisationSchemeDS.VERSION_LAST_UPDATE_USER, organisationSchemeDto.getLastUpdatedBy());
        productionDescriptorsForm.setValue(OrganisationSchemeDS.RESOURCE_CREATION_DATE, organisationSchemeDto.getResourceCreatedDate());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACED_BY_VERSION, organisationSchemeDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.REPLACE_TO_VERSION, organisationSchemeDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_FROM, organisationSchemeDto.getValidFrom());
        diffusionDescriptorsForm.setValue(OrganisationSchemeDS.VALID_TO, organisationSchemeDto.getValidTo());

        // Version responsibility
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(OrganisationSchemeDS.COMMENTS, organisationSchemeDto.getComment());

        // Annotations
        annotationsPanel.setAnnotations(organisationSchemeDto.getAnnotations(), organisationSchemeDto);
    }

    public void setOrganisationSchemeEditionMode(OrganisationSchemeMetamacDto organisationSchemeDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getOrganisationSchemeRequiredFieldsToNextProcStatus(organisationSchemeDto.getLifeCycle().getProcStatus());

        // IDENTIFIERS

        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE, organisationSchemeDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.CODE_VIEW, organisationSchemeDto.getCode());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URI, organisationSchemeDto.getUriProvider());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URN, organisationSchemeDto.getUrn());
        identifiersEditionForm.setValue(OrganisationSchemeDS.URN_PROVIDER, organisationSchemeDto.getUrnProvider());
        identifiersEditionForm.setValue(OrganisationSchemeDS.VERSION_LOGIC, organisationSchemeDto.getVersionLogic());
        identifiersEditionForm.setValue(OrganisationSchemeDS.NAME, organisationSchemeDto.getName());
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        identifiersEditionForm.markForRedraw();

        // CONTENT DESCRIPTORS

        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.TYPE, CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.DESCRIPTION, organisationSchemeDto.getDescription());
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(organisationSchemeDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.IS_EXTERNAL_REFERENCE, organisationSchemeDto.getIsExternalReference() != null
                ? (organisationSchemeDto.getIsExternalReference() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no())
                : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(OrganisationSchemeDS.FINAL, organisationSchemeDto.getFinalLogic() != null
                ? (organisationSchemeDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no())
                : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        contentDescriptorsEditionForm.markForRedraw();

        // PRODUCTION DESCRIPTORS

        ((RelatedResourceLinkItem) productionDescriptorsEditionForm.getItem(OrganisationSchemeDS.MAINTAINER)).setRelatedResource(organisationSchemeDto.getMaintainer());
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.PROC_STATUS,
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VERSION_CREATION_DATE, organisationSchemeDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VERSION_CREATION_USER, organisationSchemeDto.getCreatedBy());
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VERSION_LAST_UPDATE_DATE, organisationSchemeDto.getLastUpdated());
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VERSION_LAST_UPDATE_USER, organisationSchemeDto.getLastUpdatedBy());
        productionDescriptorsEditionForm.setValue(OrganisationSchemeDS.RESOURCE_CREATION_DATE, organisationSchemeDto.getResourceCreatedDate());
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // DIFFUSION DESCRIPTORS

        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACED_BY_VERSION, organisationSchemeDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.REPLACE_TO_VERSION, organisationSchemeDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_FROM, DateUtils.getFormattedDate(organisationSchemeDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(OrganisationSchemeDS.VALID_TO, DateUtils.getFormattedDate(organisationSchemeDto.getValidTo()));
        diffusionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // VERSION RESPONSIBILITY

        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.PRODUCTION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_USER, organisationSchemeDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.DIFFUSION_VALIDATION_DATE, organisationSchemeDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.INTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_USER, organisationSchemeDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(OrganisationSchemeDS.EXTERNAL_PUBLICATION_DATE, organisationSchemeDto.getLifeCycle().getExternalPublicationDate());
        versionResponsibilityEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // COMMENTS

        commentsEditionForm.setValue(OrganisationSchemeDS.COMMENTS, organisationSchemeDto.getComment());
        commentsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // Annotations
        annotationsEditionPanel.setAnnotations(organisationSchemeDto.getAnnotations(), organisationSchemeDto);
    }

    public OrganisationSchemeMetamacDto getOrganisationSchemeDto() {

        // Identifiers
        organisationSchemeDto.setCode(identifiersEditionForm.getValueAsString(OrganisationSchemeDS.CODE));
        organisationSchemeDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(OrganisationSchemeDS.NAME));

        // Content descriptors
        organisationSchemeDto.setDescription(contentDescriptorsEditionForm.getValueAsInternationalStringDto(OrganisationSchemeDS.DESCRIPTION));

        // Comments
        organisationSchemeDto.setComment(commentsEditionForm.getValueAsInternationalStringDto(OrganisationSchemeDS.COMMENTS));

        // Annotations
        organisationSchemeDto.getAnnotations().clear();
        organisationSchemeDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return organisationSchemeDto;
    }

    private void publishOrganisationSchemeInternally() {
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.hasDefaultMaintainerOrIsAgencySchemeSdmxResource(organisationSchemeDto) || CommonUtils.isAgencyScheme(organisationSchemeDto)
                || CommonUtils.isDataProviderScheme(organisationSchemeDto) || CommonUtils.isDataConsumenScheme(organisationSchemeDto)) {
            getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus(), null);
        } else {
            // If the organisation scheme is imported, ask the user if this resource should be the latest one.
            // If there were another organisation scheme marked as final, find it, and inform the user that the organisation scheme to publish will replace the latest one.
            getUiHandlers().retrieveLatestOrganisationScheme(organisationSchemeDto); // Publication will be done in setLatestOrganisationSchemeForInternalPublication method
        }
    }

    @Override
    public void setLatestOrganisationSchemeForInternalPublication(GetOrganisationSchemesResult result) {
        if (result.getOrganisationSchemeMetamacDtos().isEmpty()) {
            getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus(), null);
        } else {
            // If there were other version marked as the latest, ask the user what to do
            OrganisationSchemeMetamacBasicDto latest = result.getOrganisationSchemeMetamacDtos().get(0);
            ConfirmationWindow confirmationWindow = new ConfirmationWindow(getConstants().lifeCyclePublishInternally(),
                    getMessages().organisationSchemeShouldBeMarkAsTheLatest(latest.getVersionLogic()));
            confirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Organisation scheme will be the latest
                    getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus(), true);
                }
            });
            confirmationWindow.getNoButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Organisation scheme WON'T be the latest
                    getUiHandlers().publishInternally(organisationSchemeDto.getUrn(), organisationSchemeDto.getType(), organisationSchemeDto.getLifeCycle().getProcStatus(), false);
                }
            });
        }
    }

    private void versionOrganisationScheme() {
        final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
        versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (versionWindow.validateForm()) {
                    getUiHandlers().versioning(organisationSchemeDto.getUrn(), versionWindow.getSelectedVersion());
                    versionWindow.destroy();
                }
            }
        });
    }

    private void copyOrganisationSchemeAskingCode(final String urn) {
        final CopyResourceWindow copyResourceWindow = new CopyResourceWindow(getConstants().copyResource());
        copyResourceWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (copyResourceWindow.validateForm()) {
                    getUiHandlers().copyOrganisationScheme(urn, copyResourceWindow.getSelectedCode());
                    copyResourceWindow.destroy();
                }
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return OrganisationsFormUtils.canOrganisationSchemeCodeBeEdited(organisationSchemeDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !OrganisationsFormUtils.canOrganisationSchemeCodeBeEdited(organisationSchemeDto);
            }
        };
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
