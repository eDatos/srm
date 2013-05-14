package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeSearchSectionStack;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptSchemeWindow;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeListViewImpl extends ViewWithUiHandlers<ConceptSchemeListUiHandlers> implements ConceptSchemeListPresenter.ConceptSchemeListView {

    private VLayout                         panel;

    private ToolStripButton                 newConceptSchemeButton;
    private ToolStripButton                 deleteConceptSchemeButton;
    private ToolStripButton                 cancelConceptSchemeValidityButton;

    private ConceptSchemeSearchSectionStack searchSectionStack;

    private PaginatedCheckListGrid          conceptSchemesList;

    private NewConceptSchemeWindow          newConceptSchemeWindow;
    private DeleteConfirmationWindow        deleteConfirmationWindow;

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
                newConceptSchemeWindow.setUiHandlers(getUiHandlers());
                newConceptSchemeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptSchemeWindow.validateForm()) {
                            getUiHandlers().createConceptScheme(newConceptSchemeWindow.getNewConceptSchemeDto());
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
                getUiHandlers().cancelValidity(getUrnsFromSelectedConceptSchemes());
            }
        });

        toolStrip.addButton(newConceptSchemeButton);
        toolStrip.addButton(deleteConceptSchemeButton);
        toolStrip.addButton(cancelConceptSchemeValidityButton);

        // Search

        searchSectionStack = new ConceptSchemeSearchSectionStack();

        // Concepts scheme list

        conceptSchemesList = new PaginatedCheckListGrid(ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveConceptSchemes(firstResult, maxResults, searchSectionStack.getConceptSchemeWebCriteria());
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
                    getUiHandlers().goToConceptScheme(urn);
                }
            }
        });

        conceptSchemesList.getListGrid().setFields(ResourceFieldUtils.getConceptSchemeListGridFields());

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeDeleteConfirmationTitle(), getConstants().conceptSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteConceptSchemes(getUrnsFromSelectedConceptSchemes());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.setHeight100();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(toolStrip);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(conceptSchemesList);

        panel.addMember(subPanel);
    }

    @Override
    public void setConceptSchemePaginatedList(GetConceptSchemesResult conceptSchemesPaginatedList) {
        setConceptSchemeList(conceptSchemesPaginatedList.getConceptSchemeList());
        conceptSchemesList.refreshPaginationInfo(conceptSchemesPaginatedList.getFirstResultOut(), conceptSchemesPaginatedList.getConceptSchemeList().size(),
                conceptSchemesPaginatedList.getTotalResults());
    }

    @Override
    public void goToConceptSchemeListLastPageAfterCreate() {
        conceptSchemesList.goToLastPageAfterCreate();
    }

    private void setConceptSchemeList(List<ConceptSchemeMetamacBasicDto> conceptSchemesDtos) {
        ConceptSchemeRecord[] records = new ConceptSchemeRecord[conceptSchemesDtos.size()];
        int index = 0;
        for (ConceptSchemeMetamacBasicDto scheme : conceptSchemesDtos) {
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
        if (slot == ConceptSchemeListPresenter.TYPE_SetContextAreaContentConceptsToolBar) {
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
    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        if (newConceptSchemeWindow != null) {
            newConceptSchemeWindow.setOperations(result);
        }
    }

    @Override
    public void setOperationsForSearchSection(GetStatisticalOperationsResult result) {
        searchSectionStack.setOperations(result);
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeBasicDto();
            if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getLifeCycle().getProcStatus())
                    || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getLifeCycle().getProcStatus())
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
            ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(conceptSchemeMetamacDto.getLifeCycle().getProcStatus()) || conceptSchemeMetamacDto.getValidTo() != null
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
