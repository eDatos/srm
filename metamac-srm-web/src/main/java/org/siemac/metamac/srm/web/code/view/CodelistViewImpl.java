package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOpennessVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.AnnotationsPanel;
import org.siemac.metamac.srm.web.client.widgets.BooleanSelectItem;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CodesFormUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodelistCategorisationsPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistMainFormLayout;
import org.siemac.metamac.srm.web.code.widgets.CodelistOrdersPanel;
import org.siemac.metamac.srm.web.code.widgets.CodelistVersionsSectionStack;
import org.siemac.metamac.srm.web.code.widgets.CodesTreeGrid;
import org.siemac.metamac.srm.web.code.widgets.VersionCodelistWindow;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAreaItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class CodelistViewImpl extends ViewWithUiHandlers<CodelistUiHandlers> implements CodelistPresenter.CodelistView {

    private TitleLabel                                   titleLabel;
    private VLayout                                      panel;
    private CodelistMainFormLayout                       mainFormLayout;

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
    private CodelistVersionsSectionStack                 versionsSectionStack;

    // Codes
    private VLayout                                      codesLayout;
    private CodesTreeGrid                                codesTreeGrid;

    // Orders
    private CodelistOrdersPanel                          codelistOrdersPanel;

    // Categorisations
    private CodelistCategorisationsPanel                 categorisationsPanel;

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

        codesTreeGrid = new CodesTreeGrid();

        codesLayout = new VLayout();
        codesLayout.setMargin(15);
        codesLayout.addMember(codesTreeGrid);

        //
        // ORDERS
        //

        codelistOrdersPanel = new CodelistOrdersPanel();

        //
        // CATEGORISATIONS
        //

        categorisationsPanel = new CodelistCategorisationsPanel();

        // PANEL LAYOUT

        VLayout subPanel = new VLayout();
        subPanel.setMembersMargin(5);
        subPanel.addMember(versionsSectionStack);
        titleLabel = new TitleLabel();
        TabSet tabSet = new TabSet();
        tabSet.setStyleName("marginTop15");

        // Codelist tab
        Tab codelistTab = new Tab(getConstants().codelist());
        codelistTab.setPane(mainFormLayout);
        tabSet.addTab(codelistTab);

        // Codes tab
        Tab codesTab = new Tab(getConstants().codes());
        codesTab.setPane(codesLayout);
        tabSet.addTab(codesTab);

        // Orders tab
        Tab ordersTab = new Tab(getConstants().codelistOrders());
        ordersTab.setPane(codelistOrdersPanel);
        tabSet.addTab(ordersTab);

        // Openness levels tab
        Tab opennessLevelsTab = new Tab(getConstants().codelistOpennessLevels());
        // TODO add content
        tabSet.addTab(opennessLevelsTab);

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

        // Edit: Add a custom handler to check scheme status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ProcStatusEnum status = codelistDto.getLifeCycle().getProcStatus();
                if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
                    // Create a new version
                    final InformationWindow window = new InformationWindow(getMessages().codelistEditionInfo(), getMessages().codelistEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    setEditionMode();
                }
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
                getUiHandlers().publishInternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getPublishExternally().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().publishExternally(codelistDto.getUrn(), codelistDto.getLifeCycle().getProcStatus());
            }
        });
        mainFormLayout.getVersioning().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final VersionWindow versionWindow = new VersionWindow(getConstants().lifeCycleVersioning());
                versionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (versionWindow.validateForm()) {
                            final VersionTypeEnum versionType = versionWindow.getSelectedVersion();

                            VersionCodelistWindow versionCodelistWindow = new VersionCodelistWindow();
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
        this.codesTreeGrid.setUiHandlers(uiHandlers);
        this.codelistOrdersPanel.setUiHandlers(uiHandlers);
        // TODO openness level
        this.categorisationsPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public void setCodelist(CodelistMetamacDto codelist) {
        this.codelistDto = codelist;
        this.codelistOrdersPanel.getcodelistCodelistOrdersSectionStack().setCodelistProcStatus(codelist.getLifeCycle().getProcStatus());

        // Set title
        titleLabel.setContents(org.siemac.metamac.srm.web.client.utils.CommonUtils.getResourceTitle(codelist));

        // Security
        ProcStatusEnum procStatus = codelist.getLifeCycle().getProcStatus();
        mainFormLayout.setCanEdit(CodesClientSecurityUtils.canUpdateCodelist(procStatus));
        mainFormLayout.updatePublishSection(procStatus, codelist.getValidTo());
        mainFormLayout.setViewMode();
        categorisationsPanel.updateVisibility(procStatus);

        setCodelistViewMode(codelist);
        setCodelistEditionMode(codelist);

        // Update codelist in tree grid
        codesTreeGrid.updateItemScheme(codelist);
        codelistOrdersPanel.updateItemScheme(codelist);
        // TODO update item scheme in openness level tree?
    }

    @Override
    public void setCodelistVersions(List<CodelistMetamacDto> codelistDtos) {
        versionsSectionStack.setCodelists(codelistDtos);
        versionsSectionStack.selectCodelist(codelistDto);
    }

    @Override
    public void setCodes(List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setItems(codelistDto, codes);

        // Every time new codes are set, the visualization of codes order or openness levels should be reset (maybe there are new codes, or its structure has changed)
        codelistOrdersPanel.hideCodes();
        // TODO hide openness levels
    }

    //
    // ORDERS
    //

    @Override
    public void setCodesWithOrder(List<CodeMetamacVisualisationResult> codes, CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        codelistOrdersPanel.setCodes(codelistDto, codes, codelistOrderVisualisationDto);
    }

    @Override
    public void setCodelistOrders(List<CodelistOrderVisualisationDto> orders) {
        codelistOrdersPanel.setOrders(orders);

        // Populate the form with the order list (to fill the default order)
        diffusionDescriptorsEditionForm.getItem(CodelistDS.DEFAULT_ORDER).setValueMap(CommonUtils.getCodelistOrdersHashMap(orders));
    }

    //
    // OPENNESS LEVELS
    //

    @Override
    public void setCodesWithOpennessLevel(List<CodeMetamacVisualisationResult> codes, CodelistOpennessVisualisationDto codelistOpennessVisualisationDto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCodelistOpennessLevels(List<CodelistOpennessVisualisationDto> opennessLevels) {
        // TODO Auto-generated method stub

    }

    //
    // CATEGORISATIONS
    //

    @Override
    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationsPanel.setCategorisations(categorisationDtos);
    }

    @Override
    public void setCategorySchemesForCategorisations(GetCategorySchemesResult result) {
        categorisationsPanel.setCategorySchemes(result);
    }

    @Override
    public void setCategoriesForCategorisations(GetCategoriesResult result) {
        categorisationsPanel.setCategories(result);
    }

    //
    // COMPLEX CODELISTS
    //

    @Override
    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        codesTreeGrid.setCodelistsToCreateComplexCodelist(result);
    }

    @Override
    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setCodesToCreateComplexCodelist(codelistMetamacDto, codes);
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
            searchVariableWindow.setRelatedResources(RelatedResourceUtils.getVariableDtosAsRelatedResourceDtos(result.getVariables()));
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
        ViewTextItem uri = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersForm.setFields(code, name, shortName, uri, urn, urnProvider, version);

        // CONTENT DESCRIPTORS
        contentDescriptorsForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        ViewMultiLanguageTextItem description = new ViewMultiLanguageTextItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        ViewTextItem isRecommended = new ViewTextItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        ViewTextItem family = new ViewTextItem(CodelistDS.FAMILY_VIEW, getConstants().codelistFamily());
        ViewTextItem variable = new ViewTextItem(CodelistDS.VARIABLE_VIEW, getConstants().variable());
        contentDescriptorsForm.setFields(description, partial, isExternalReference, isFinal, isRecommended, family, variable);

        // PRODUCTION DESCRIPTORS
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsForm.setFields(agency, procStatus);

        // DIFFUSION DESCRIPTORS
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = new RelatedResourceListItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists(), false);
        ViewTextItem replacedByCodelist = new ViewTextItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist());
        ViewTextItem accessType = new ViewTextItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        ViewTextItem defaultOrder = new ViewTextItem(CodelistDS.DEFAULT_ORDER, getConstants().codelistDefaultOrder());
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsForm.setFields(replaceToCodelists, replacedByCodelist, accessType, defaultOrder, replacedByVersion, replaceTo, validFrom, validTo, externalPublicationFailed,
                externalPublicationFailedDate);

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
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(CodelistDS.SHORT_NAME, getConstants().codelistShortName());
        ViewTextItem uri = new ViewTextItem(CodelistDS.URI, getConstants().identifiableArtefactUri());
        ViewTextItem urn = new ViewTextItem(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        ViewTextItem urnProvider = new ViewTextItem(CodelistDS.URN_PROVIDER, getConstants().identifiableArtefactUrnProvider());
        ViewTextItem version = new ViewTextItem(CodelistDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        identifiersEditionForm.setFields(code, staticCode, name, shortName, uri, urn, urnProvider, version);

        // CONTENT DESCRIPTORS
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().formContentDescriptors());
        MultiLanguageTextAreaItem description = new MultiLanguageTextAreaItem(CodelistDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        ViewTextItem partial = new ViewTextItem(CodelistDS.IS_PARTIAL, getConstants().itemSchemeIsPartial());
        ViewTextItem isExternalReference = new ViewTextItem(CodelistDS.IS_EXTERNAL_REFERENCE, getConstants().maintainableArtefactIsExternalReference());
        ViewTextItem isFinal = new ViewTextItem(CodelistDS.FINAL, getConstants().maintainableArtefactFinalLogic());
        BooleanSelectItem isRecommended = new BooleanSelectItem(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());
        ViewTextItem family = new ViewTextItem(CodelistDS.FAMILY, getConstants().codelistFamily());
        family.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem familyView = createFamilyItem(CodelistDS.FAMILY_VIEW, getConstants().codelistFamily());
        ViewTextItem variable = new ViewTextItem(CodelistDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem variableView = createVariableItem(CodelistDS.VARIABLE_VIEW, getConstants().variable());
        contentDescriptorsEditionForm.setFields(description, partial, isExternalReference, isFinal, isRecommended, family, familyView, variable, variableView);

        // PRODUCTION DESCRIPTORS
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem agency = new ViewTextItem(CodelistDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        ViewTextItem procStatus = new ViewTextItem(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        productionDescriptorsEditionForm.setFields(agency, procStatus);

        // DIFFUSION DESCRIPTORS
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        RelatedResourceListItem replaceToCodelists = createReplaceToCodelistsItem(CodelistDS.REPLACE_TO_CODELISTS, getConstants().codelistReplaceToCodelists());
        ViewTextItem replacedByCodelist = new ViewTextItem(CodelistDS.REPLACED_BY_CODELIST, getConstants().codelistReplacedByCodelist());
        CustomSelectItem accessType = new CustomSelectItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        accessType.setValueMap(CommonUtils.getAccessTypeHashMap());
        CustomSelectItem defaultOrder = new CustomSelectItem(CodelistDS.DEFAULT_ORDER, getConstants().codelistDefaultOrder()); // ValueMap is set in setCodelistOrders method
        ViewTextItem replacedByVersion = new ViewTextItem(CodelistDS.REPLACED_BY_VERSION, getConstants().maintainableArtefactReplacedByVersion());
        ViewTextItem replaceTo = new ViewTextItem(CodelistDS.REPLACE_TO_VERSION, getConstants().maintainableArtefactReplaceToVersion());
        ViewTextItem validFrom = new ViewTextItem(CodelistDS.VALID_FROM, getConstants().maintainableArtefactValidFrom());
        ViewTextItem validTo = new ViewTextItem(CodelistDS.VALID_TO, getConstants().maintainableArtefactValidTo());
        ViewTextItem externalPublicationFailed = new ViewTextItem(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, getConstants().lifeCycleExternalPublicationFailed());
        ViewTextItem externalPublicationFailedDate = new ViewTextItem(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, getConstants().lifeCycleExternalPublicationFailedDate());
        diffusionDescriptorsEditionForm.setFields(replaceToCodelists, replacedByCodelist, accessType, defaultOrder, replacedByVersion, replaceTo, validFrom, validTo, externalPublicationFailed,
                externalPublicationFailedDate);

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
        MultiLanguageTextAreaItem comments = new MultiLanguageTextAreaItem(CodelistDS.COMMENTS, getConstants().nameableArtefactComments());
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

    private void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    private void setCodelistViewMode(CodelistMetamacDto codelistDto) {
        // Identifiers
        identifiersForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));

        // Content descriptors
        contentDescriptorsForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        contentDescriptorsForm.setValue(CodelistDS.IS_PARTIAL, org.siemac.metamac.srm.web.client.utils.CommonUtils.getBooleanName(codelistDto.getIsPartial()));
        contentDescriptorsForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
                .no()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.IS_RECOMMENDED, org.siemac.metamac.srm.web.client.utils.CommonUtils.getBooleanName(codelistDto.getIsRecommended()));
        contentDescriptorsForm.setValue(CodelistDS.FAMILY_VIEW,
                codelistDto.getFamily() != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getFamily()) : StringUtils.EMPTY);
        contentDescriptorsForm.setValue(CodelistDS.VARIABLE_VIEW,
                codelistDto.getVariable() != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getVariable()) : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsForm.setValue(CodelistDS.MAINTAINER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getMaintainer()));
        productionDescriptorsForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACED_BY_CODELIST, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getReplacedByCodelist()));
        ((RelatedResourceListItem) diffusionDescriptorsForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsForm.setValue(CodelistDS.ACCESS_TYPE, CommonUtils.getAccessTypeName(codelistDto.getAccessType()));
        diffusionDescriptorsForm.setValue(CodelistDS.DEFAULT_ORDER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getDefaultOrderVisualisation()));
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_FROM, codelistDto.getValidFrom());
        diffusionDescriptorsForm.setValue(CodelistDS.VALID_TO, codelistDto.getValidTo());
        diffusionDescriptorsForm.setValue(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(codelistDto.getLifeCycle().getIsExternalPublicationFailed()) ? MetamacWebCommon.getConstants()
                .yes() : StringUtils.EMPTY);
        diffusionDescriptorsForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(codelistDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));

        // Annotations
        annotationsPanel.setAnnotations(codelistDto.getAnnotations(), codelistDto.getMaintainer());
    }

    private void setCodelistEditionMode(CodelistMetamacDto codelistDto) {
        // Identifiers
        identifiersEditionForm.setValue(CodelistDS.CODE, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.CODE_VIEW, codelistDto.getCode());
        identifiersEditionForm.setValue(CodelistDS.URI, codelistDto.getUriProvider());
        identifiersEditionForm.setValue(CodelistDS.URN, codelistDto.getUrn());
        identifiersEditionForm.setValue(CodelistDS.URN_PROVIDER, codelistDto.getUrnProvider());
        identifiersEditionForm.setValue(CodelistDS.VERSION_LOGIC, codelistDto.getVersionLogic());
        identifiersEditionForm.setValue(CodelistDS.NAME, RecordUtils.getInternationalStringRecord(codelistDto.getName()));
        identifiersEditionForm.setValue(CodelistDS.SHORT_NAME, RecordUtils.getInternationalStringRecord(codelistDto.getShortName()));
        identifiersEditionForm.markForRedraw();

        // Content descriptors
        contentDescriptorsEditionForm.setValue(CodelistDS.DESCRIPTION, RecordUtils.getInternationalStringRecord(codelistDto.getDescription()));
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_PARTIAL, org.siemac.metamac.srm.web.client.utils.CommonUtils.getBooleanName(codelistDto.getIsPartial()));
        contentDescriptorsEditionForm.setValue(CodelistDS.IS_EXTERNAL_REFERENCE, codelistDto.getIsExternalReference() != null ? (codelistDto.getIsExternalReference() ? MetamacWebCommon.getConstants()
                .yes() : MetamacWebCommon.getConstants().no()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.FINAL, codelistDto.getFinalLogic() != null ? (codelistDto.getFinalLogic() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                .getConstants().no()) : StringUtils.EMPTY);
        ((BooleanSelectItem) contentDescriptorsEditionForm.getItem(CodelistDS.IS_RECOMMENDED)).setBooleanValue(codelistDto.getIsRecommended());
        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY_VIEW,
                codelistDto.getFamily() != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getFamily()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY, codelistDto.getFamily() != null ? codelistDto.getFamily().getUrn() : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.VARIABLE_VIEW,
                codelistDto.getVariable() != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getVariable()) : StringUtils.EMPTY);
        contentDescriptorsEditionForm.setValue(CodelistDS.VARIABLE, codelistDto.getVariable() != null ? codelistDto.getVariable().getUrn() : StringUtils.EMPTY);

        // Production descriptors
        productionDescriptorsEditionForm.setValue(CodelistDS.MAINTAINER, org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getMaintainer()));
        productionDescriptorsEditionForm.setValue(CodelistDS.PROC_STATUS, org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()));
        productionDescriptorsEditionForm.markForRedraw();

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACED_BY_CODELIST,
                org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(codelistDto.getReplacedByCodelist()));
        ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).setRelatedResources(codelistDto.getReplaceToCodelists());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.ACCESS_TYPE, codelistDto.getAccessType() != null ? codelistDto.getAccessType().name() : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm
                .setValue(CodelistDS.DEFAULT_ORDER, codelistDto.getDefaultOrderVisualisation() != null ? codelistDto.getDefaultOrderVisualisation().getUrn() : StringUtils.EMPTY);
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACED_BY_VERSION, codelistDto.getReplacedByVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.REPLACE_TO_VERSION, codelistDto.getReplaceToVersion());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_FROM, DateUtils.getFormattedDate(codelistDto.getValidFrom()));
        diffusionDescriptorsEditionForm.setValue(CodelistDS.VALID_TO, DateUtils.getFormattedDate(codelistDto.getValidTo()));
        diffusionDescriptorsEditionForm.setValue(CodelistDS.IS_EXTERNAL_PUBLICATION_FAILED, BooleanUtils.isTrue(codelistDto.getLifeCycle().getIsExternalPublicationFailed()) ? MetamacWebCommon
                .getConstants().yes() : MetamacWebCommon.getConstants().no());
        diffusionDescriptorsEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_FAILED_DATE, DateUtils.getFormattedDate(codelistDto.getLifeCycle().getExternalPublicationFailedDate()));

        // Version responsibility
        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_USER, codelistDto.getLifeCycle().getProductionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.PRODUCTION_VALIDATION_DATE, codelistDto.getLifeCycle().getProductionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_USER, codelistDto.getLifeCycle().getDiffusionValidationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.DIFFUSION_VALIDATION_DATE, codelistDto.getLifeCycle().getDiffusionValidationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getInternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.INTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getInternalPublicationDate());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_USER, codelistDto.getLifeCycle().getExternalPublicationUser());
        versionResponsibilityEditionForm.setValue(CodelistDS.EXTERNAL_PUBLICATION_DATE, codelistDto.getLifeCycle().getExternalPublicationDate());

        // Comments
        commentsEditionForm.setValue(CodelistDS.COMMENTS, RecordUtils.getInternationalStringRecord(codelistDto.getComment()));

        // Annotations
        annotationsEditionPanel.setAnnotations(codelistDto.getAnnotations(), codelistDto.getMaintainer());
    }

    public CodelistMetamacDto getCodelistDto() {
        // Identifiers
        codelistDto.setCode(identifiersEditionForm.getValueAsString(CodelistDS.CODE));
        codelistDto.setName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.NAME));
        codelistDto.setShortName((InternationalStringDto) identifiersEditionForm.getValue(CodelistDS.SHORT_NAME));

        // Content descriptors
        codelistDto.setDescription((InternationalStringDto) contentDescriptorsEditionForm.getValue(CodelistDS.DESCRIPTION));
        codelistDto.setIsRecommended(((BooleanSelectItem) contentDescriptorsEditionForm.getItem(CodelistDS.IS_RECOMMENDED)).getBooleanValue());
        codelistDto.setFamily(!StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(CodelistDS.FAMILY)) ? RelatedResourceUtils.createRelatedResourceDto(contentDescriptorsEditionForm
                .getValueAsString(CodelistDS.FAMILY)) : null);
        codelistDto.setVariable(!StringUtils.isBlank(contentDescriptorsEditionForm.getValueAsString(CodelistDS.VARIABLE)) ? RelatedResourceUtils.createRelatedResourceDto(contentDescriptorsEditionForm
                .getValueAsString(CodelistDS.VARIABLE)) : null);

        // Diffusion descriptors
        codelistDto.getReplaceToCodelists().clear();
        codelistDto.getReplaceToCodelists().addAll(((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getSelectedRelatedResources());
        codelistDto.setAccessType(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(diffusionDescriptorsEditionForm
                .getValueAsString(CodelistDS.ACCESS_TYPE)) : null);
        codelistDto.setDefaultOrderVisualisation(!StringUtils.isBlank(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_ORDER)) ? RelatedResourceUtils
                .createRelatedResourceDto(diffusionDescriptorsEditionForm.getValueAsString(CodelistDS.DEFAULT_ORDER)) : null);

        // Comments
        codelistDto.setComment((InternationalStringDto) commentsEditionForm.getValue(CodelistDS.COMMENTS));

        // Annotations
        codelistDto.getAnnotations().clear();
        codelistDto.getAnnotations().addAll(annotationsEditionPanel.getAnnotations());

        return codelistDto;
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEMS CREATION
    // ------------------------------------------------------------------------------------------------------------

    private SearchViewTextItem createFamilyItem(String name, String title) {

        SearchViewTextItem familyItem = new SearchViewTextItem(name, title);
        familyItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                showSearchFamilyWindow(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedFamily = searchFamilyWindow.getSelectedRelatedResource();
                        searchFamilyWindow.markForDestroy();
                        // Set selected family in form
                        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY, selectedFamily != null ? selectedFamily.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(CodelistDS.FAMILY_VIEW,
                                selectedFamily != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedFamily) : null);
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

        RelatedResourceListItem replaceToItem = new RelatedResourceListItem(name, title, true);
        replaceToItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent arg0) {
                searchReplaceToCodelistsWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().codelistsSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, searchReplaceToCodelistsWindow.getRelatedResourceCriteria());
                    }
                });

                // Load the list codelists that can be replaced
                getUiHandlers().retrieveCodelistsThatCanBeReplaced(FIRST_RESULST, MAX_RESULTS, null);

                // Set the selected codelists
                List<RelatedResourceDto> selectedCodelists = ((RelatedResourceListItem) diffusionDescriptorsEditionForm.getItem(CodelistDS.REPLACE_TO_CODELISTS)).getRelatedResourceDtos();
                searchReplaceToCodelistsWindow.setTargetRelatedResources(selectedCodelists);

                searchReplaceToCodelistsWindow.setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodelistsThatCanBeReplaced(firstResult, maxResults, criteria);
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

    private SearchViewTextItem createVariableItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem variableItem = new SearchViewTextItem(name, title);
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
                        contentDescriptorsEditionForm.setValue(CodelistDS.VARIABLE, selectedVariable != null ? selectedVariable.getUrn() : null);
                        contentDescriptorsEditionForm.setValue(CodelistDS.VARIABLE_VIEW,
                                selectedVariable != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedVariable) : null);
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
}
