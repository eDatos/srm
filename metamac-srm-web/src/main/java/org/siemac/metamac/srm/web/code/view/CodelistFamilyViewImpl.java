package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.ds.CodelistFamilyDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistFamilyPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistFamilyUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomSectionStack;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistFamilyViewImpl extends ViewWithUiHandlers<CodelistFamilyUiHandlers> implements CodelistFamilyPresenter.CodelistFamilyView {

    private VLayout panel;
    private InternationalMainFormLayout mainFormLayout;

    // View forms
    private GroupDynamicForm identifiersForm;
    private GroupDynamicForm productionDescriptorsForm;

    // Edition forms
    private GroupDynamicForm identifiersEditionForm;
    private GroupDynamicForm productionDescriptorsEditionForm;

    private PaginatedCheckListGrid codelistListGrid;
    private ToolStripButton addCodelistToFamilyButton;
    private ToolStripButton removeCodelistToFamilyButton;
    private SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow codelistsWindow;
    private DeleteConfirmationWindow removeConfirmationWindow;

    private CodelistFamilyDto codelistFamilyDto;

    @Inject
    public CodelistFamilyViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CODELIST FAMILY
        //

        mainFormLayout = new InternationalMainFormLayout(CodesClientSecurityUtils.canUpdateCodelistFamily(), CodesClientSecurityUtils.canDeleteCodelistFamily());
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        // CODELISTS

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        addCodelistToFamilyButton = createAddCodelistToFamilyButton();
        removeCodelistToFamilyButton = createRemoveCodelistToFamilyButton();
        toolStrip.addButton(addCodelistToFamilyButton);
        toolStrip.addButton(removeCodelistToFamilyButton);

        // ListGrid

        codelistListGrid = new PaginatedCheckListGrid(CodelistFamilyPresenter.CODELIST_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelistsByFamily(firstResult, maxResults, null, codelistFamilyDto.getUrn());
            }
        });
        codelistListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        codelistListGrid.getListGrid().setDataSource(new CodelistDS());
        codelistListGrid.getListGrid().setUseAllDataSourceFields(false);
        codelistListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (codelistListGrid.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridRemoveButton();
                } else {
                    removeCodelistToFamilyButton.hide();
                }
            }
        });
        codelistListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((CodelistRecord) event.getRecord()).getAttribute(CodelistDS.URN);
                    getUiHandlers().goToCodelist(urn);
                }
            }
        });
        ListGridField fieldCode = new ListGridField(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(CodelistDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(CodelistDS.URN, getConstants().identifiableArtefactUrn());
        codelistListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        // Remove window

        removeConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistRemoveFromFamilyConfirmationTitle(), getConstants().codelistRemoveFromFamilyConfirmation());
        removeConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().removeCodelistsFromFamily(CommonUtils.getUrnsFromSelectedCodelists(codelistListGrid.getListGrid().getSelectedRecords()), codelistFamilyDto.getUrn());
                removeConfirmationWindow.hide();
            }
        });

        VLayout codelistsLayout = new VLayout();
        codelistsLayout.setMargin(15);
        CustomSectionStack sectionStack = new CustomSectionStack(getConstants().codelistFamilyCodelists());
        sectionStack.getDefaultSection().setItems(toolStrip, codelistListGrid);
        codelistsLayout.addMember(sectionStack);

        VLayout subpanel = new VLayout();
        subpanel.setOverflow(Overflow.SCROLL);
        subpanel.addMember(mainFormLayout);
        subpanel.addMember(codelistsLayout);

        panel.addMember(subpanel);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false)) {
                    // See: METAMAC-2516
                    // Two invokes to getXXXDto() is needed for Chrome, please don't remove this two call fix.
                    getCodelistFamilyDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getUiHandlers().saveCodelistFamily(getCodelistFamilyDto());
                        }
                    });

                }
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCodelistFamily(codelistFamilyDto.getUrn());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistFamilyPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setCodelistFamily(CodelistFamilyDto family) {
        this.codelistFamilyDto = family;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(family.getName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCodelistFamilyViewMode(family);
        setCodelistFamilyEditionMode(family);
    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        ViewTextItem urn = new ViewTextItem(CodelistFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, name, urn);

        // Production descriptors
        productionDescriptorsForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(CodelistFamilyDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(CodelistFamilyDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(CodelistFamilyDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(CodelistFamilyDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(productionDescriptorsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        MultiLanguageTextItem name = new MultiLanguageTextItem(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        name.setRequired(true);
        ViewTextItem urn = new ViewTextItem(CodelistFamilyDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, name, urn);

        // Production descriptors
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formProductionDescriptors());
        ViewTextItem creationDate = new ViewTextItem(CodelistFamilyDS.CREATION_DATE, getConstants().resourceCreationDate());
        ViewTextItem creationUser = new ViewTextItem(CodelistFamilyDS.CREATION_USER, getConstants().resourceCreationUser());
        ViewTextItem lastUpdateDate = new ViewTextItem(CodelistFamilyDS.LAST_UPDATE_DATE, getConstants().resourceLastUpdateDate());
        ViewTextItem lastUpdateUser = new ViewTextItem(CodelistFamilyDS.LAST_UPDATE_USER, getConstants().resourceLastUpdateUser());
        productionDescriptorsEditionForm.setFields(creationDate, creationUser, lastUpdateDate, lastUpdateUser);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setCodelistFamilyViewMode(CodelistFamilyDto codelistFamilyDto) {
        // Identifiers
        identifiersForm.setValue(CodelistFamilyDS.CODE, codelistFamilyDto.getCode());
        identifiersForm.setValue(CodelistFamilyDS.URN, codelistFamilyDto.getUrn());
        identifiersForm.setValue(CodelistFamilyDS.NAME, codelistFamilyDto.getName());

        // Production descriptors
        productionDescriptorsForm.setValue(CodelistFamilyDS.CREATION_DATE, codelistFamilyDto.getCreatedDate());
        productionDescriptorsForm.setValue(CodelistFamilyDS.CREATION_USER, codelistFamilyDto.getCreatedBy());
        productionDescriptorsForm.setValue(CodelistFamilyDS.LAST_UPDATE_DATE, codelistFamilyDto.getLastUpdated());
        productionDescriptorsForm.setValue(CodelistFamilyDS.LAST_UPDATE_USER, codelistFamilyDto.getLastUpdatedBy());
    }

    public void setCodelistFamilyEditionMode(CodelistFamilyDto codelistFamilyDto) {
        // Identifiers
        identifiersEditionForm.setValue(CodelistFamilyDS.CODE, codelistFamilyDto.getCode());
        identifiersEditionForm.setValue(CodelistFamilyDS.URN, codelistFamilyDto.getUrn());
        identifiersEditionForm.setValue(CodelistFamilyDS.NAME, codelistFamilyDto.getName());

        // Production descriptors
        productionDescriptorsEditionForm.setValue(CodelistFamilyDS.CREATION_DATE, codelistFamilyDto.getCreatedDate());
        productionDescriptorsEditionForm.setValue(CodelistFamilyDS.CREATION_USER, codelistFamilyDto.getCreatedBy());
        productionDescriptorsEditionForm.setValue(CodelistFamilyDS.LAST_UPDATE_DATE, codelistFamilyDto.getLastUpdated());
        productionDescriptorsEditionForm.setValue(CodelistFamilyDS.LAST_UPDATE_USER, codelistFamilyDto.getLastUpdatedBy());
    }

    public CodelistFamilyDto getCodelistFamilyDto() {
        // Identifiers
        codelistFamilyDto.setName(identifiersEditionForm.getValueAsInternationalStringDto(CodelistFamilyDS.NAME));

        return codelistFamilyDto;
    }

    @Override
    public void setCodelists(GetCodelistsResult result) {
        if (codelistsWindow != null) {
            codelistsWindow.setResources(RelatedResourceUtils.getCodelistBasicDtosAsRelatedResourceDtos(result.getCodelists()));
            codelistsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getCodelists().size(), result.getTotalResults());
        }
    }

    @Override
    public void setCodelistsOfFamily(GetCodelistsResult result) {
        setCodelistsOfFamily(result.getCodelists());
        codelistListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getCodelists().size(), result.getTotalResults());
    }

    private void setCodelistsOfFamily(List<CodelistMetamacBasicDto> codelistDtos) {
        CodelistRecord[] records = new CodelistRecord[codelistDtos.size()];
        int index = 0;
        for (CodelistMetamacBasicDto scheme : codelistDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getCodelistRecord(scheme);
        }
        codelistListGrid.getListGrid().setData(records);
    }

    private ToolStripButton createAddCodelistToFamilyButton() {

        ToolStripButton button = new ToolStripButton(MetamacWebCommon.getConstants().add(), RESOURCE.newListGrid().getURL());
        button.setVisible(CodesClientSecurityUtils.canAddCodelistToCodelistFamily());
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {

                codelistsWindow = new SearchMultipleRelatedResourceItemWithSchemeFilterPaginatedWindow(getConstants().variablesSelection(), SrmWebConstants.FORM_LIST_MAX_RESULTS, null,
                        new SearchPaginatedAction<RelatedResourceItemWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, RelatedResourceItemWebCriteria webCriteria) {
                                getUiHandlers().retrieveCodelists(firstResult, maxResults, webCriteria.getCriteria(), webCriteria.isItemSchemeLastVersion());
                            }
                        });

                codelistsWindow.getFilter().getItemSchemeFilterFacet().getFormItem().setVisible(false);

                codelistsWindow.retrieveItems();

                codelistsWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

                        List<RelatedResourceDto> selectedCodelists = codelistsWindow.getSelectedResources();
                        codelistsWindow.markForDestroy();
                        if (selectedCodelists != null && !selectedCodelists.isEmpty()) {
                            getUiHandlers().addCodelistsToFamily(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(selectedCodelists), codelistFamilyDto.getUrn());
                        }
                    }
                });
            }
        });
        return button;
    }

    private ToolStripButton createRemoveCodelistToFamilyButton() {
        ToolStripButton button = new ToolStripButton(getConstants().codelistRemoveFromFamily(), RESOURCE.deleteListGrid().getURL());
        button.setVisible(false);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                removeConfirmationWindow.show();
            }
        });
        return button;
    }

    private void showListGridRemoveButton() {
        if (CodesClientSecurityUtils.canRemoveCodelistFromCodelistFamily()) {
            removeCodelistToFamilyButton.show();
        }
    }
}
