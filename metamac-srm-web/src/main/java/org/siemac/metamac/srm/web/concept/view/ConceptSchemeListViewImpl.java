package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptSchemeWindow;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeListViewImpl extends ViewImpl implements ConceptSchemeListPresenter.ConceptSchemeListView {

    private ConceptSchemeListUiHandlers uiHandlers;
    private VLayout                     panel;

    private ToolStripButton             newConceptSchemeButton;
    private ToolStripButton             deleteConceptSchemeButton;
    private ToolStripButton             cancelConceptSchemeValidityButton;

    private SearchSectionStack          searchSectionStack;

    private PaginatedCheckListGrid      conceptSchemesList;

    private NewConceptSchemeWindow      newConceptSchemeWindow;
    private DeleteConfirmationWindow    deleteConfirmationWindow;

    @Inject
    public ConceptSchemeListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newConceptSchemeButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newConceptSchemeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newConceptSchemeWindow = new NewConceptSchemeWindow(getConstants().conceptSchemeCreate());
                newConceptSchemeWindow.setUiHandlers(uiHandlers);
                newConceptSchemeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptSchemeWindow.validateForm()) {
                            uiHandlers.createConceptScheme(newConceptSchemeWindow.getNewConceptSchemeDto());
                            newConceptSchemeWindow.destroy();
                        }
                    }
                });
            }
        });
        newConceptSchemeButton.setVisibility(ConceptsClientSecurityUtils.canCreateConceptScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteConceptSchemeButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteConceptSchemeButton.setVisibility(Visibility.HIDDEN);
        deleteConceptSchemeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelConceptSchemeValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelConceptSchemeValidityButton.setVisibility(Visibility.HIDDEN);
        cancelConceptSchemeValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.cancelValidity(getUrnsFromSelectedConceptSchemes());
            }
        });

        toolStrip.addButton(newConceptSchemeButton);
        toolStrip.addButton(deleteConceptSchemeButton);
        toolStrip.addButton(cancelConceptSchemeValidityButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                uiHandlers.retrieveConceptSchemes(ConceptSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, searchSectionStack.getSearchCriteria());
            }
        });

        // Concepts scheme list

        conceptSchemesList = new PaginatedCheckListGrid(ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                uiHandlers.retrieveConceptSchemes(firstResult, maxResults, null);
            }
        });
        conceptSchemesList.getListGrid().setAutoFitMaxRecords(ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS);
        conceptSchemesList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        conceptSchemesList.getListGrid().setDataSource(new ConceptSchemeDS());
        conceptSchemesList.getListGrid().setUseAllDataSourceFields(false);
        conceptSchemesList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (conceptSchemesList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(conceptSchemesList.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showListGridCancelValidityDeleteButton(conceptSchemesList.getListGrid().getSelectedRecords());
                } else {
                    deleteConceptSchemeButton.hide();
                    cancelConceptSchemeValidityButton.hide();
                }
            }
        });

        conceptSchemesList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((ConceptSchemeRecord) event.getRecord()).getAttribute(ConceptSchemeDS.URN);
                    uiHandlers.goToConceptScheme(urn);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ListGridField status = new ListGridField(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        conceptSchemesList.getListGrid().setFields(fieldCode, fieldName, status);

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(conceptSchemesList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeDeleteConfirmationTitle(), getConstants().conceptSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteConceptSchemes(getUrnsFromSelectedConceptSchemes());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public void setConceptSchemePaginatedList(GetConceptSchemePaginatedListResult conceptSchemesPaginatedList) {
        setConceptSchemeList(conceptSchemesPaginatedList.getConceptSchemeList());
        conceptSchemesList.refreshPaginationInfo(conceptSchemesPaginatedList.getPageNumber(), conceptSchemesPaginatedList.getConceptSchemeList().size(), conceptSchemesPaginatedList.getTotalResults());
    }

    @Override
    public void goToConceptSchemeListLastPageAfterCreate() {
        conceptSchemesList.goToLastPageAfterCreate();
    }

    private void setConceptSchemeList(List<ConceptSchemeMetamacDto> conceptSchemesDtos) {
        ConceptSchemeRecord[] records = new ConceptSchemeRecord[conceptSchemesDtos.size()];
        int index = 0;
        for (ConceptSchemeMetamacDto scheme : conceptSchemesDtos) {
            records[index++] = RecordUtils.getConceptSchemeRecord(scheme);
        }
        conceptSchemesList.getListGrid().setData(records);
    }

    public List<String> getUrnsFromSelectedConceptSchemes() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : conceptSchemesList.getListGrid().getSelectedRecords()) {
            ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
            urns.add(schemeRecord.getUrn());
        }
        return urns;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemeListPresenter.TYPE_SetContextAreaContentConceptSchemeListToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CONCEPTS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                            ((ToolStripButton) canvas[i]).select();
                        }
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    @Override
    public void setOperations(List<ExternalItemDto> operations, int firstResult, int totalResults) {
        if (newConceptSchemeWindow != null) {
            newConceptSchemeWindow.setOperations(operations);
            newConceptSchemeWindow.refreshOperationsPaginationInfo(firstResult, operations.size(), totalResults);
        }
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeDto();
            if (ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getProcStatus())
                    || ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getProcStatus())
                    || !ConceptsClientSecurityUtils.canDeleteConceptScheme(conceptSchemeMetamacDto.getType(), CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto))) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted) {
            deleteConceptSchemeButton.show();
        } else {
            deleteConceptSchemeButton.hide();
        }
    }

    private void showListGridCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getProcStatus()) || conceptSchemeMetamacDto.getValidTo() != null
                    || !ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(conceptSchemeMetamacDto.getType(), CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto))) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled) {
            cancelConceptSchemeValidityButton.show();
        } else {
            cancelConceptSchemeValidityButton.hide();
        }
    }

}
