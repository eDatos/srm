package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.RequiredFieldUtils;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.client.widgets.CopyResourceWindow;
import org.siemac.metamac.srm.web.client.widgets.CustomTabSet;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesFormUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodelistCategorisationsPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistCodesPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistMainFormLayout;
import org.siemac.metamac.srm.web.code.widgets.CodelistOpennesssLevelsPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistOrdersPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistVersionsSectionStack;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultilanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class CodelistViewImpl extends ViewWithUiHandlers<CodelistUiHandlers> implements CodelistPresenter.CodelistView {

    private final TitleLabel                             titleLabel;
    private final InformationLabel                       informationLabel;
    private final VLayout                                panel;
    private final CodelistMainFormLayout                 mainFormLayout;

    private final CustomTabSet                           tabSet;
    private final Tab                                    codelistTab;

    // View forms
    private GroupDynamicForm                             identifiersForm;
    private GroupDynamicForm                             contentDescriptorsForm;
    private GroupDynamicForm                             productionDescriptorsForm;
    private GroupDynamicForm                             diffusionDescriptorsForm;
    private GroupDynamicForm                             versionResponsibilityForm;
    private GroupDynamicForm                             commentsForm;
    private AnnotationsPanel                             annotationsPanel;

    // Edition forms
    private GroupDynamicForm                             identifiersEditionForm;
    private GroupDynamicForm                             contentDescriptorsEditionForm;
    private GroupDynamicForm                             productionDescriptorsEditionForm;
    private GroupDynamicForm                             diffusionDescriptorsEditionForm;
    private GroupDynamicForm                             versionResponsibilityEditionForm;
    private GroupDynamicForm                             commentsEditionForm;
    private AnnotationsPanel                             annotationsEditionPanel;

    private SearchRelatedResourcePaginatedWindow         searchFamilyWindow;
    private SearchRelatedResourcePaginatedWindow         searchVariableWindow;
    private SearchMultipleRelatedResourcePaginatedWindow searchReplaceToCodelistsWindow;

    // Versions
    private final CodelistVersionsSectionStack           versionsSectionStack;

    // Codes
    private final CodelistCodesPanel                     codelistCodesPanel;

    // Variable elements assignment
    private final CodelistCodesVariableElementsPanel     codelistCodesVariableElementsPanel;

    // Orders
    private final CodelistOrdersPanel                    codelistOrdersPanel;

    // Openness levels
    private final CodelistOpennesssLevelsPanel           codelistOpennesssLevelsPanel;

    // Categorisations
    private final CodelistCategorisationsPanel           categorisationsPanel;

    private CodelistMetamacDto                           codelistDto;

    @Inject
    public CodelistViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CODELIST VERSIONS
        //

        versionsSectionStack = new CodelistVersionsSectionStack(getConstants().codelistVersions());
        versionsSectionStack.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((CodelistRecord) event.getRecord()).getUrn();
                getUiHandlers().goToCodelist(urn);
            }
        });

        //
        // CODELIST
        //

        mainFormLayout = new CodelistMainFormLayout();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        //
        // CODES
        //

        codelistCodesPanel = new CodelistCodesPanel();

        // VARIABLE ELEMENTS ASSIGNMENT

        codelistCodesVariableElementsPanel = new CodelistCodesVariableElementsPanel();

        //
        // ORDERS
        //

        codelistOrdersPanel = new CodelistOrdersPanel();

        //
        // OPENNESS LEVELS
        //

        codelistOpennesssLevelsPanel = new CodelistOpennesssLevelsPanel();

        //
        // CATEGORISATIONS
        //

        categorisationsPanel = new CodelistCategorisationsPanel();

        // PANEL LAYOUT

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);

        titleLabel = new TitleLabel();
        informationLabel = new InformationLabel(StringUtils.EMPTY);
        informationLabel.setVisible(false);

        tabSet = new CustomTabSet();

        // Codelist tab
        codelistTab = new Tab(getConstants().codelist());
        codelistTab.setPane(mainFormLayout);
        tabSet.addTab(codelistTab);

        // Codes tab
        Tab codesTab = new Tab(getConstants().codes());
        codesTab.setPane(codelistCodesPanel);
        tabSet.addTab(codesTab);

        // Variable elements assignment
        Tab variableElementsAssignment = new Tab(getConstants().codesVariableElementAssignment());
        variableElementsAssignment.setPane(codelistCodesVariableElementsPanel);
        tabSet.addTab(variableElementsAssignment);

        // Orders tab
        Tab ordersTab = new Tab(getConstants().codelistOrders());
        ordersTab.setPane(codelistOrdersPanel);
        tabSet.addTab(ordersTab);

        // Openness levels tab
        Tab opennessLevelsTab = new Tab(getConstants().codelistOpennessLevels());
        opennessLevelsTab.setPane(codelistOpennesssLevelsPanel);
        tabSet.addTab(opennessLevelsTab);

        // Categorisations tab
        Tab categorisationsTab = new Tab(getConstants().categorisations());
        categorisationsTab.setPane(categorisationsPanel);
        tabSet.addTab(categorisationsTab);

        VLayout tabSubPanel = new VLayout();
        tabSubPanel.addMember(titleLabel);
        tabSubPanel.addMember(informationLabel);
        tabSubPanel.addMember(tabSet);
        tabSubPanel.setMargin(15);
        subPanel.addMember(tabSubPanel);
        panel.addMember(subPanel);
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
                startCodelistEdition();
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCodelist(codelistDto.getUrn());
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && productionDescriptorsEditionForm.validate(false)
                        && diffusionDescriptorsEditionForm.validate(false)) {
                    getUiHandlers().saveCodelist(getCodelistDto());
                }
            }
        });

        // Life cycle
        mainFormLayout.getSendToProductionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToProductionValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getSendToDiffusionValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().sendToDiffusionValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getRejectValidation().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().rejectValidation(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishInternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                publishCodelistInternally();
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getCreateTemporalVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().createTemporalVersion(codelistDto.getUrn());
            }
        });
        mainFormLayout.getConsolidateVersion().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                versionCodelist();
            }
        });
        mainFormLayout.getCancelValidity().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(codelistDto.getUrn());
            }
        });
        mainFormLayout.getAddCodelistToFamily().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSearchFamilyWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedFamily = searchFamilyWindow.getSelectedRelatedResource();
                        searchFamilyWindow.markForDestroy();
                        getUiHandlers().addCodelistToFamily(codelistDto.getUrn(), selectedFamily.getUrn());
                    }
                });
            }
        });
        mainFormLayout.getExport().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().exportCodelist(codelistDto.getUrn());
            }
        });
        mainFormLayout.getCopy().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().copyCodelist(codelistDto.getUrn());
            }
        });
        mainFormLayout.getCopyKeepingMaintainer().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                copyCodelistAskingCode(codelistDto.getUrn());
            }
        });
    }
    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.codelistCodesPanel.setUiHandlers(uiHandlers);
        this.codelistCodesVariableElementsPanel.setUiHandlers(uiHandlers);
        this.codelistOrdersPanel.setUiHandlers(uiHandlers);
        this.codelistOpennesssLevelsPanel.setUiHandlers(uiHandlers);
        this.categorisationsPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public void selectCodelistTab() {
        tabSet.selectTab(codelistTab);
    }

    @Override
    public void setCodelist(CodelistMetamacDto codelist) {
        this.codelistDto = codelist;
        this.codelistOrdersPanel.getCodelistOrdersSectionStack().setCodelist(codelist);
        this.codelistOpennesssLevelsPanel.getCodelistOpennessLevelsSectionStack().setCodelist(codelist);

        // Set title
        titleLabel.setContents(org.siemac.metamac.srm.web.client.utils.CommonUtils.getResourceTitle(codelist));
        setInformationLabelContents(codelist);

        // Security
        mainFormLayout.setCodelist(codelist);
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(codelist);

        setCodelistViewMode(codelist);
        setCodelistEditionMode(codelist);

        // Update codelist in tree grid
        codelistCodesPanel.updateItemScheme(codelist);
        codelistCodesVariableElementsPanel.updateItemScheme(codelist);
        codelistOrdersPanel.updateItemScheme(codelist);
        codelistOpennesssLevelsPanel.updateItemScheme(codelist);
    }

    private void setInformationLabelContents(CodelistMetamacDto codelistMetamacDto) {
        if (BooleanUtils.isTrue(codelistMetamacDto.getIsTaskInBackground())) {
            String message = getMessages().codelistIsInProcessInBackground();
            informationLabel.setContents(message);
            informationLabel.show();
        } else {
            informationLabel.setContents(StringUtils.EMPTY);
            informationLabel.hide();
        }
    }

    @Override
    public void startCodelistEdition() {
        mainFormLayout.setEditionMode();
    }

    @Override
    public void setCodelistVersions(List<CodelistMetamacBasicDto> codelistDtos) {
        versionsSectionStack.setCodelists(codelistDtos);
        versionsSectionStack.selectCodelist(codelistDto);
    }

    @Override
    public void setCodes(List<CodeMetamacVisualisationResult> codes) {
        codelistCodesPanel.setCodes(codelistDto, codes);
        codelistCodesVariableElementsPanel.setCodes(codelistDto, codes);

        // Every time new codes are set, the visualization of codes order or openness levels should be reset (maybe there are new codes, or its structure has changed)
        codelistOrdersPanel.hideCodes();
        codelistOpennesssLevelsPanel.hideCodes();
    }

    //
    // CODES AND VARIABLE ELEMENTS ASSIGNMENTS
    //

    @Override
    public void setCodesVariableElementsNormalised(List<CodeVariableElementNormalisationResult> codeVariableElementNormalisationResults) {
        codelistCodesVariableElementsPanel.setCodesVariableElementsNormalised(codeVariableElementNormalisationResults);
    }

    @Override
    public void setVariableElementsForManualNormalisation(GetRelatedResourcesResult result) {
        codelistCodesVariableElementsPanel.setVariableElementsForManualNormalisation(result);
    }

    //
    // ORDERS
    //

    @Override
    public void setCodesWithOrder(List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOrderVisualisationDto) {
        codelistOrdersPanel.setCodes(codelistDto, codes, codelistOrderVisualisationDto);
    }

    @Override
    public void setCodelistOrders(List<CodelistVisualisationDto> orders) {
        codelistOrdersPanel.setOrders(orders);

        // Populate the form with the order list (to fill the default order)
        diffusionDescriptorsEditionForm.getItem(CodelistDS.DEFAULT_ORDER).setValueMap(CommonUtils.getCodelistVisualisationsHashMap(orders));
    }

    //
    // OPENNESS LEVELS
    //

    @Override
    public void setCodesWithOpennessLevel(List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOpennessVisualisationDto) {
        codelistOpennesssLevelsPanel.setCodes(codelistDto, codes, codelistOpennessVisualisationDto);
    }

    @Override
    public void setCodelistOpennessLevels(List<CodelistVisualisationDto> opennessLevels) {
        codelistOpennesssLevelsPanel.setOpennessLevels(opennessLevels);

        // Populate the form with the order list (to fill the default order)
        diffusionDescriptorsEditionForm.getItem(CodelistDS.DEFAULT_OPENNESS_LEVEL).setValueMap(CommonUtils.getCodelistVisualisationsHashMap(opennessLevels));
    }

    //
    // CATEGORISATIONS
    //

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

    //
    // COMPLEX CODELISTS
    //

    @Override
    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        codelistCodesPanel.setCodelistsToCreateComplexCodelist(result);
    }

    @Override
    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codelistCodesPanel.setCodesToCreateComplexCodelist(codelistMetamacDto, codes);
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void setFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults) {
        if (searchFamilyWindow != null) {
            searchFamilyWindow.setRelatedResources(families);
            searchFamilyWindow.refreshSourcePaginationInfo(firstResult, families.size(), totalResults);
        }
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (searchVariableWindow != null) {
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getVariableBasicDtosAsRelatedResourceDtos(result.getVariables()));
            searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCodelistsToReplace(List<RelatedResourceDto> codelists, int firstResult, int totalResults) {
        if (searchReplaceToCodelistsWindow != null) {
            searchReplaceToCodelistsWindow.setSourceRelatedResources(codelists);
            searchReplaceToCodelistsWindow.refreshSourcePaginationInfo(firstResult, codelists.size(), totalResults);
        }
    }

    private void createViewForm() {
        // IDENTIFIERS FORM
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(CodelistDS.SHORT_NAME, getConstants().codelistShortName());
        ViewTextItem uriProvider = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, shortName, uriProvider, urn, urnProvider, version);

        // CONTENT DESCRIPTORS
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewMultiLanguageTextItem descriptionSource = new ViewMultiLanguageTextItem(CodelistDS.DESCRIPTION_SOURCE, getConstants().codelistDescriptionSource());
        ViewTextItem partial = new ViewTextItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        ViewTextItem isRecommended = new ViewTextItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        RelatedResourceLinkItem family = new RelatedResourceLinkItem(CodelistDS.FAMILY, getConstants().codelistFamily(), getCustomLinkItemNavigationClickHandler());
        RelatedResourceLinkItem variable = new RelatedResourceLinkItem(CodelistDS.VARIABLE, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        contentDescriptorsForm.setFields(description, descriptionSource, partial, isExternalReference, isFinal, isRecommended, family, variable);

        // PRODUCTION DESCRIPTORS
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(CodelistDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem resourceCreationDate = new ViewTextItem(CodelistDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsForm.setFields(agency, procStatus, creationDate, resourceCreationDate);

        // DIFFUSION DESCRIPTORS
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = new RelatedResourceListItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists(), false,
                getListRecordNavigationClickHandler());
        RelatedResourceLinkItem replacedByCodelist = new RelatedResourceLinkItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist(),
                getCustomLinkItemNavigationClickHandler());
        ViewTextItem accessType = new ViewTextItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        ViewTextItem defaultOrder = new ViewTextItem(CodelistDS.DEFAULT_ORDER, getConstants().codelistDefaultOrder());
        ViewTextItem defaultOpennessLevel = new ViewTextItem(CodelistDS.DEFAULT_OPENNESS_LEVEL, getConstants().codelistDefaultOpennessLevel());
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsForm.setFields(replaceToCodelists, replacedByCodelist, accessType, defaultOrder, defaultOpennessLevel, replacedByVersion, replaceTo, validFrom, validTo);

        // VERSION RESPONSIBILITY
        versionResponsibilityForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser, internalPublicationDate,
                externalPublicationUser, externalPublicationDate);

        // COMMENTS
        commentsForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(CodelistDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsForm.setFields(comments);

        // ANNOTATIONS
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
        // IDENTIFIERS
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());

        RequiredTextItem code = new RequiredTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        code.setValidators(SemanticIdentifiersUtils.getCodelistIdentifierCustomValidator());
        code.setShowIfCondition(getCodeFormItemIfFunction());

        ViewTextItem staticCode = new ViewTextItem(CodelistDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        staticCode.setShowIfCondition(getStaticCodeFormItemIfFunction());

        MultiLanguageTextItem name = new MultiLanguageTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(CodelistDS.SHORT_NAME, getConstants().codelistShortName(), SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        ViewTextItem uriProvider = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUriProvider());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, shortName, uriProvider, urn, urnProvider, version);

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultilanguageRichTextEditorItem description = new MultilanguageRichTextEditorItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        MultilanguageRichTextEditorItem descriptionSource = new MultilanguageRichTextEditorItem(CodelistDS.DESCRIPTION_SOURCE, getConstants().codelistDescriptionSource());
        ViewTextItem partial = new ViewTextItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        BooleanSelectItem isRecommended = new BooleanSelectItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        SearchRelatedResourceLinkItem family = createFamilyItem(CodelistDS.FAMILY, getConstants().codelistFamily());
        SearchRelatedResourceLinkItem variable = createVariableItem(CodelistDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(getVariableFormItemIfFunction());
        RelatedResourceLinkItem staticVariable = new RelatedResourceLinkItem(CodelistDS.VARIABLE_VIEW, getConstants().variable(), getCustomLinkItemNavigationClickHandler());
        staticVariable.setShowIfCondition(getStaticVariableFormItemIfFunction());
        contentDescriptorsEditionForm.setFields(description, descriptionSource, partial, isExternalReference, isFinal, isRecommended, family, variable, staticVariable);

        // PRODUCTION DESCRIPTORS
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        RelatedResourceLinkItem agency = new RelatedResourceLinkItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer(), getCustomLinkItemNavigationClickHandler());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ViewTextItem creationDate = new ViewTextItem(CodelistDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        ViewTextItem resourceCreationDate = new ViewTextItem(CodelistDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        productionDescriptorsEditionForm.setFields(agency, procStatus, creationDate, resourceCreationDate);

        // DIFFUSION DESCRIPTORS
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = createReplaceToCodelistsItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists());
        RelatedResourceLinkItem replacedByCodelist = new RelatedResourceLinkItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist(),
                getCustomLinkItemNavigationClickHandler());
        CustomSelectItem accessType = new CustomSelectItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        accessType.setValueMap(CommonUtils.getAccessTypeHashMap());
        CustomSelectItem defaultOrder = new CustomSelectItem(CodelistDS.DEFAULT_ORDER, getConstants().codelistDefaultOrder()); // ValueMap is set in setCodelistOrders method
        CustomSelectItem defaultOpennessLevel = new CustomSelectItem(CodelistDS.DEFAULT_OPENNESS_LEVEL, getConstants().codelistDefaultOpennessLevel()); // ValueMap is set in setCodelistOpennessLevel
                                                                                                                                                        // method
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        diffusionDescriptorsEditionForm.setFields(replaceToCodelists, replacedByCodelist, accessType, defaultOrder, defaultOpennessLevel, replacedByVersion, replaceTo, validFrom, validTo);

        // VERSION RESPONSIBILITY
        versionResponsibilityEditionForm = new GroupDynamicForm(getConstants().lifeCycleVersionResponsibility());
        ViewTextItem productionValidationUser = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_USER, getConstants().lifeCycleProductionValidationUser());
        ViewTextItem productionValidationDate = new ViewTextItem(CodelistDS.PRODUCTION_VALIDATION_DATE, getConstants().lifeCycleProductionValidationDate());
        ViewTextItem diffusionValidationUser = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_USER, getConstants().lifeCycleDiffusionValidationUser());
        ViewTextItem diffusionValidationDate = new ViewTextItem(CodelistDS.DIFFUSION_VALIDATION_DATE, getConstants().lifeCycleDiffusionValidationDate());
        ViewTextItem internalPublicationUser = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        ViewTextItem internalPublicationDate = new ViewTextItem(CodelistDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        ViewTextItem externalPublicationUser = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        ViewTextItem externalPublicationDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        versionResponsibilityEditionForm.setFields(productionValidationUser, productionValidationDate, diffusionValidationUser, diffusionValidationDate, internalPublicationUser,
                internalPublicationDate, externalPublicationUser, externalPublicationDate);

        // COMMENTS
        commentsEditionForm = new GroupDynamicForm(getConstants().nameableArtefactComments());
        MultilanguageRichTextEditorItem comments = new MultilanguageRichTextEditorItem(CodelistDS.COMMENTS, getConstants().nameableArtefactComments());
        commentsEditionForm.setFields(comments);

        // ANNOTATIONS
        annotationsEditionPanel = new AnnotationsPanel(false);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(versionResponsibilityEditionForm);
        mainFormLayout.addEditionCanvas(commentsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionPanel);
    }

    private void setCodelistViewMode(CodelistMetamacDto codelistDto) {

        // IDENTIFIERS

        identifiersForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));

        // CONTENT DESCRIPTORS

        contentDescriptorsForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        contentDescriptorsForm.setValue(CodelistDS.DESCRIPTION_SOURCE, RecordUtils.getInternationalStringRecord(codelistDto.getDescriptionSource()));
        contentDescriptorsForm.setValue(CodelistDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(codelistDto.getIsPartial()));
        contentDescriptorsForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
                .no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.IS_RECOMMENDED, BooleanWebUtils.getBooleanLabel(codelistDto.getIsRecommended()));
        ((RelatedResourceLinkItem) contentDescriptorsForm.getItem(CodelistDS.FAMILY)).setRelatedResource(codelistDto.getFamily());
        ((RelatedResourceLinkItem) contentDescriptorsForm.getItem(CodelistDS.VARIABLE)).setRelatedResource(codelistDto.getVariable());

        // PRODUCTION DESCRIPTORS

        ((RelatedResourceLinkItem) productionDescriptorsForm.getItem(CodelistDS.MAINTAINER)).setRelatedResource(codelistDto.getMaintainer());
        productionDescriptorsForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));
        productionDescriptorsForm.setValue(CodelistDS.VERSION_CREATION_DATE, codelistDto.getCreatedDate());
        productionDescriptorsForm.setValue(CodelistDS.RESOURCE_CREATION_DATE, codelistDto.getResourceCreatedDate());

        // DIFFUSION DESCRIPTORS

        ((RelatedResourceLinkItem) diffusionDescriptorsForm.getItem(CodelistDS.REPLACED_BY_CODELIST)).setRelatedResource(codelistDto.getReplacedByCodelist());
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsForm.setValue(CodelistDS.ACCESS_TYPE, CommonUtils.getAccessTypeName(codelistDto.getAccessType()));
        diffusionDescriptorsForm.setValue(CodelistDS.DEFAULT_ORDER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getDefaultOrderVisualisation()));
        diffusionDescriptorsForm.setValue(CodelistDS.DEFAULT_OPENNESS_LEVEL,
                org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getDefaultOpennessVisualisation()));
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_FROM, codelistDto.getValidFrom());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_TO, codelistDto.getValidTo());

        // VERSION RESPONSIBILITY

        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());

        // COMMENTS

        commentsForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));

        // ANNOTATIONS

        annotationsPanel.setAnnotations(codelistDto.getAnnotations(), codelistDto);
    }

    private void setCodelistEditionMode(CodelistMetamacDto codelistDto) {

        String[] requiredFieldsToNextProcStatus = RequiredFieldUtils.getCodelistRequiredFieldsToNextProcStatus(codelistDto.getLifeCycle().getProcStatus());

        // IDENTIFIERS

        identifiersEditionForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.CODE_VIEW, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersEditionForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersEditionForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersEditionForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersEditionForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersEditionForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));
        identifiersEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        identifiersEditionForm.markForRedraw();

        // CONTENT DESCRIPTORS

        contentDescriptorsEditionForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        ((MultilanguageRichTextEditorItem) contentDescriptorsEditionForm.getItem(CodelistDS.DESCRIPTION_SOURCE)).setValueInternational(codelistDto.getDescriptionSource());
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_PARTIAL, BooleanWebUtils.getBooleanLabel(codelistDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference() ? MetamacWebCommon.getConstants()
                .yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        ((BooleanSelectItem) contentDescriptorsEditionForm.getItem(CodelistDS.IS_RECOMMENDED)).setBooleanValue(codelistDto.getIsRecommended());
        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.FAMILY)).setRelatedResource(codelistDto.getFamily());
        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.VARIABLE)).setRelatedResource(codelistDto.getVariable());
        ((RelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.VARIABLE_VIEW)).setRelatedResource(codelistDto.getVariable());
        contentDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        contentDescriptorsEditionForm.markForRedraw();

        // PRODUCTION DESCRIPTORS

        ((RelatedResourceLinkItem) productionDescriptorsEditionForm.getItem(CodelistDS.MAINTAINER)).setRelatedResource(codelistDto.getMaintainer());
        productionDescriptorsEditionForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.setValue(CodelistDS.VERSION_CREATION_DATE, codelistDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(CodelistDS.RESOURCE_CREATION_DATE, codelistDto.getResourceCreatedDate());
        productionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);
        productionDescriptorsEditionForm.markForRedraw();

        // DIFFUSION DESCRIPTORS

        ((RelatedResourceLinkItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACED_BY_CODELIST)).setRelatedResource(codelistDto.getReplacedByCodelist());
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.ACCESS_TYPE, codelistDto.getAccessType() != null ? codelistDto.getAccessType().name() : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm
                .setValue(CodelistDS.DEFAULT_ORDER, codelistDto.getDefaultOrderVisualisation() != null ? codelistDto.getDefaultOrderVisualisation().getUrn() : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm.setValue(CodelistDS.DEFAULT_OPENNESS_LEVEL, codelistDto.getDefaultOpennessVisualisation() != null
                ? codelistDto.getDefaultOpennessVisualisation().getUrn()
                : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_FROM, DateUtils.getFormattedDate(codelistDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_TO, DateUtils.getFormattedDate(codelistDto.getValidTo()));
        diffusionDescriptorsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // VERSION RESPONSIBILITY

        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());
        versionResponsibilityEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // COMMENTS

        commentsEditionForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));
        commentsEditionForm.setRequiredTitleSuffix(requiredFieldsToNextProcStatus);

        // ANNOTATIONS

        annotationsEditionPanel.setAnnotations(codelistDto.getAnnotations(), codelistDto);
    }

    public CodelistMetamacDto getCodelistDto() {
        // Identifiers
        codelistDto.setCode(identifiersEditionForm.getValueAsString(CodelistDS.CODE));
        codelistDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.NAME));
        codelistDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.SHORT_NAME));

        // Content descriptors
        codelistDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CodelistDS.DESCRIPTION));
        codelistDto.setDescriptionSource(((MultilanguageRichTextEditorItem) contentDescriptorsEditionForm.getItem(CodelistDS.DESCRIPTION_SOURCE)).getValue());
        codelistDto.setIsRecommended(((BooleanSelectItem) contentDescriptorsEditionForm.getItem(CodelistDS.IS_RECOMMENDED)).getBooleanValue());
        codelistDto.setFamily(((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.FAMILY)).getRelatedResourceDto());
        codelistDto.setVariable(((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.VARIABLE)).getRelatedResourceDto());

        // Diffusion descriptors
        codelistDto.getReplaceToCodelists().clear();
        codelistDto.getReplaceToCodelists().addAll(((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getSelectedRelatedResources());
        codelistDto.setAccessType(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(diffusionDescriptorsEditionForm
                .getValueAsString(CodelistDS.ACCESS_TYPE)) : null);
        codelistDto.setDefaultOrderVisualisation(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_ORDER)) ? RelatedResourceUtils
                .createRelatedResourceDto(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_ORDER)) : null);
        codelistDto.setDefaultOpennessVisualisation(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_OPENNESS_LEVEL)) ? RelatedResourceUtils
                .createRelatedResourceDto(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_OPENNESS_LEVEL)) : null);

        // Comments
        codelistDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CodelistDS.COMMENTS));

        // Annotations
        codelistDto.getAnnotations().clear();
        codelistDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return codelistDto;
    }

    private void publishCodelistInternally() {
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(codelistDto.getMaintainer())) {
            getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus(), null);
        } else {
            // If the codelist is imported, ask the user if this resource should be the latest one.
            // If there were another codelist marked as final, find it, and inform the user that the codelist to publish will replace the latest one.
            getUiHandlers().retrieveLatestCodelist(codelistDto); // Publication will be done in setLatestCodelistForInternalPublication method
        }
    }

    @Override
    public void setLatestCodelistForInternalPublication(GetCodelistsResult result) {
        if (result.getCodelists().isEmpty()) {
            getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus(), null);
        } else {
            // If there were other version marked as the latest, ask the user what to do
            CodelistMetamacBasicDto latest = result.getCodelists().get(0);
            ConfirmationWindow confirmationWindow = new ConfirmationWindow(getConstants().lifeCyclePublishInternally(), getMessages().codelistShouldBeMarkAsTheLatest(latest.getVersionLogic()));
            confirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Codelist will be the latest
                    getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus(), true);
                }
            });
            confirmationWindow.getNoButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // Codelist WON'T be the latest
                    getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus(), false);
                }
            });
        }
    }

    private void versionCodelist() {
        final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
        versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (versionWindow.validateForm()) {
                    final VersionTypeEnum versionType = versionWindow.getSelectedVersion();

                    ConfirmationWindow versionCodelistWindow = new ConfirmationWindow(getConstants().codelistVersioning(), getConstants().codelistVersioningConfirmationMessage());
                    versionCodelistWindow.getYesButton().addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            getUiHandlers().versioning(codelistDto.getUrn(), versionType, true);
                        }
                    });
                    versionCodelistWindow.getNoButton().addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            getUiHandlers().versioning(codelistDto.getUrn(), versionType, false);
                        }
                    });
                    versionWindow.destroy();
                }
            }
        });
    }

    private void copyCodelistAskingCode(final String urn) {
        final CopyResourceWindow copyResourceWindow = new CopyResourceWindow(getConstants().copyResource());
        copyResourceWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (copyResourceWindow.validateForm()) {
                    getUiHandlers().copyCodelist(urn, copyResourceWindow.getSelectedCode());
                    copyResourceWindow.destroy();
                }
            }
        });
    }

    @Override
    public void showInformationMessage(String title, String message) {
        InformationWindow informationWindow = new InformationWindow(title, message);
        informationWindow.show();
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEMS CREATION
    // ------------------------------------------------------------------------------------------------------------

    private SearchRelatedResourceLinkItem createFamilyItem(String name, String title) {

        SearchRelatedResourceLinkItem familyItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        familyItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                showSearchFamilyWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedFamily = searchFamilyWindow.getSelectedRelatedResource();
                        searchFamilyWindow.markForDestroy();
                        // Set selected family in form
                        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.FAMILY)).setRelatedResource(selectedFamily);
                    }
                });
            }
        });
        return familyItem;
    }

    private void showSearchFamilyWindow(com.smartgwt.client.widgets.form.fields.events.ClickHandler acceptButtonHandler) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        searchFamilyWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistFamilySelection(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveFamilies(firstResult, maxResults, searchFamilyWindow.getRelatedResourceCriteria());
            }
        });

        // Load families (to populate the selection window)
        getUiHandlers().retrieveFamilies(FIRST_RESULST, MAX_RESULTS, null);

        searchFamilyWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchFamilyWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveFamilies(firstResult, maxResults, criteria);
            }
        });
        searchFamilyWindow.getSave().addClickHandler(acceptButtonHandler);
    }

    private RelatedResourceListItem createReplaceToCodelistsItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(name, title, true, getListRecordNavigationClickHandler());
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchReplaceToCodelistsWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().codelistsSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, searchReplaceToCodelistsWindow.getRelatedResourceCriteria(), searchReplaceToCodelistsWindow.getIsLastVersionValue());
                    }
                });

                searchReplaceToCodelistsWindow.showIsLastVersionItem();
                searchReplaceToCodelistsWindow.getIsLastVersionItem().addChangedHandler(new ChangedHandler() {

                    @Override
                    public void onChanged(ChangedEvent event) {
                        retrieveCodelistsThatCanBeReplaced(FIRST_RESULST, MAX_RESULTS, searchReplaceToCodelistsWindow.getRelatedResourceCriteria(),
                                searchReplaceToCodelistsWindow.getIsLastVersionValue());
                    }
                });

                // Load the list codelists that can be replaced
                retrieveCodelistsThatCanBeReplaced(FIRST_RESULST, MAX_RESULTS, null, searchReplaceToCodelistsWindow.getIsLastVersionValue());

                // Set the selected codelists
                List<RelatedResourceDto> selectedCodelists = ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getRelatedResourceDtos();
                searchReplaceToCodelistsWindow.setTargetRelatedResources(selectedCodelists);

                searchReplaceToCodelistsWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, criteria, searchReplaceToCodelistsWindow.getIsLastVersionValue());
                    }
                });
                searchReplaceToCodelistsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        List<RelatedResourceDto> selectedCodelists = searchReplaceToCodelistsWindow.getSelectedRelatedResources();
                        searchReplaceToCodelistsWindow.markForDestroy();
                        // Set selected codelists in form
                        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(selectedCodelists);
                    }
                });
            }
        });
        return replaceToItem;
    }

    private void retrieveCodelistsThatCanBeReplaced(int firstResult, int maxResults, String criteria, boolean isLastVersion) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(criteria);
        codelistWebCriteria.setIsLastVersion(isLastVersion);
        getUiHandlers().retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, codelistWebCriteria);
    }

    private SearchRelatedResourceLinkItem createVariableItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem variableItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        variableItem.setRequired(true);
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchVariableWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, searchVariableWindow.getRelatedResourceCriteria());
                    }
                });

                searchVariableWindow.setInfoMessage(getConstants().codelistVariableEditionInfoMessage());
                searchVariableWindow.showInfoMessage();

                // Load variables (to populate the selection window)
                getUiHandlers().retrieveVariables(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariables(firstResult, maxResults, criteria);
                    }
                });
                searchVariableWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariable = searchVariableWindow.getSelectedRelatedResource();
                        searchVariableWindow.markForDestroy();
                        // Set selected variable in form
                        ((SearchRelatedResourceLinkItem) contentDescriptorsEditionForm.getItem(CodelistDS.VARIABLE)).setRelatedResource(selectedVariable);
                        contentDescriptorsEditionForm.validate(false);
                    }
                });
            }
        });
        return variableItem;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // CODE

    private FormItemIfFunction getCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CodesFormUtils.canCodelistCodeBeEdited(codelistDto);
            }
        };
    }

    private FormItemIfFunction getStaticCodeFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CodesFormUtils.canCodelistCodeBeEdited(codelistDto);
            }
        };
    }

    // VARIABLE

    private FormItemIfFunction getVariableFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return CodesFormUtils.canCodelistVariableBeEdited(codelistDto);
            }
        };
    }

    private FormItemIfFunction getStaticVariableFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !CodesFormUtils.canCodelistVariableBeEdited(codelistDto);
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

    private ListRecordNavigationClickHandler getListRecordNavigationClickHandler() {
        return new ListRecordNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
