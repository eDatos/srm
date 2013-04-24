package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.ds.CategorisationDS;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public abstract class CategorisationsPanel extends VLayout {

    private static final int                  FIRST_RESULT = 0;
    private static final int                  MAX_RESULTS  = 8;

    protected ToolStripButton                 newCategorisationButton;
    protected ToolStripButton                 deleteCategorisationButton;
    protected CustomListGrid                  categorisationListGrid;

    private SearchCategoriesForCategorisation searchCategoriesWindow;
    private DeleteConfirmationWindow          deleteConfirmationWindow;

    private CategorisationUiHandlers          uiHandlers;

    protected ProcStatusEnum                  procStatus;

    public CategorisationsPanel() {
        setMargin(15);

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCategorisationButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSearchCategoriesWindow();
            }
        });

        deleteCategorisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCategorisationButton.setVisibility(Visibility.HIDDEN);
        deleteCategorisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newCategorisationButton);
        toolStrip.addButton(deleteCategorisationButton);

        // Deletion window

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categorisationDeleteConfirmationTitle(), getConstants().categorisationDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategorisations(getSelectedCategorisationUrns());
                deleteConfirmationWindow.hide();
            }
        });

        // ListGrid

        categorisationListGrid = new CustomListGrid();

        ListGridField codeField = new ListGridField(CategorisationDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField categoryField = new ListGridField(CategorisationDS.CATEGORY, getConstants().category());
        ListGridField urnField = new ListGridField(CategorisationDS.URN, getConstants().identifiableArtefactUrn());
        categorisationListGrid.setFields(codeField, categoryField, urnField);

        categorisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categorisationListGrid.getSelectedRecords().length > 0) {
                    showDeleteCategorisationButton(categorisationListGrid.getSelectedRecords());
                } else {
                    deleteCategorisationButton.hide();
                }
            }
        });

        addMember(toolStrip);
        addMember(categorisationListGrid);
    }

    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationListGrid.setData(RecordUtils.getCategorisationRecords(categorisationDtos));
    }

    public void setCategorySchemes(GetCategorySchemesResult result) {
        if (searchCategoriesWindow != null) {
            List<RelatedResourceDto> categorySchemes = RelatedResourceUtils.geCategorySchemeMetamacBasicDtosAsRelatedResourceDtos(result.getCategorySchemeList());
            searchCategoriesWindow.setFilterRelatedResources(categorySchemes);
            searchCategoriesWindow.refreshFilterListPaginationInfo(result.getFirstResultOut(), categorySchemes.size(), result.getTotalResults());
        }
    }

    public void setCategories(GetCategoriesResult result) {
        if (searchCategoriesWindow != null) {
            List<RelatedResourceDto> categories = RelatedResourceUtils.getCategoryMetamacBasicDtosAsRelatedResourceDtos(result.getCategoryMetamacDtos());
            searchCategoriesWindow.setSourceRelatedResources(categories);
            searchCategoriesWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), categories.size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(CategorisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategorisationUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public ProcStatusEnum getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(ProcStatusEnum procStatus) {
        this.procStatus = procStatus;
    }

    private List<String> getSelectedCategorisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categorisationListGrid.getSelectedRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) record;
            urns.add(categorisationRecord.getUrn());
        }
        return urns;
    }

    private void showSearchCategoriesWindow() {
        PaginatedAction filterListAction = new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategorySchemesForCategorisations(firstResult, maxResults, searchCategoriesWindow.getFilterListCriteria());
            }
        };
        PaginatedAction selectionListAction = new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, searchCategoriesWindow.getSelectionListCriteria(),
                        searchCategoriesWindow.getSelectedRelatedResourceUrnAsFilter());
            }
        };
        searchCategoriesWindow = new SearchCategoriesForCategorisation(MAX_RESULTS, filterListAction, selectionListAction);

        // Load the list of categories (to populate the selection window)
        getUiHandlers().retrieveCategorySchemesForCategorisations(FIRST_RESULT, MAX_RESULTS, null);
        getUiHandlers().retrieveCategoriesForCategorisations(FIRST_RESULT, MAX_RESULTS, null, null);

        // Filter categories when the category scheme filter changes
        searchCategoriesWindow.getFilterListItem().getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                getUiHandlers().retrieveCategoriesForCategorisations(FIRST_RESULT, MAX_RESULTS, searchCategoriesWindow.getSelectionListCriteria(),
                        searchCategoriesWindow.getSelectedRelatedResourceUrnAsFilter());
            }
        });
        searchCategoriesWindow.getClearButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                getUiHandlers().retrieveCategoriesForCategorisations(FIRST_RESULT, MAX_RESULTS, searchCategoriesWindow.getSelectionListCriteria(),
                        searchCategoriesWindow.getSelectedRelatedResourceUrnAsFilter());
            }
        });

        // Set the search actions
        searchCategoriesWindow.setSelectionListSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, criteria, searchCategoriesWindow.getSelectedRelatedResourceUrnAsFilter());
            }
        });
        searchCategoriesWindow.setFilterListSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCategorySchemesForCategorisations(firstResult, maxResults, criteria);
            }
        });

        searchCategoriesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                List<RelatedResourceDto> categories = searchCategoriesWindow.getSelectedRelatedResources();
                searchCategoriesWindow.markForDestroy();
                if (categories != null && !categories.isEmpty()) {
                    getUiHandlers().createCategorisations(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(categories));
                }
            }
        });
    }

    private void showDeleteCategorisationButton(ListGridRecord[] selectedRecords) {
        if (canAllCategorisationsBeDeleted(selectedRecords)) {
            deleteCategorisationButton.show();
        } else {
            deleteCategorisationButton.hide();
        }
    }

    public abstract void updateNewButtonVisibility();
    public abstract boolean canAllCategorisationsBeDeleted(ListGridRecord[] records);
}
