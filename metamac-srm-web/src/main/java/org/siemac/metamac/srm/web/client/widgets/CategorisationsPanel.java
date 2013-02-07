package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.ds.CategorisationDS;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategorisationsPanel extends VLayout {

    private static final int                             FIRST_RESULT = 0;
    private static final int                             MAX_RESULTS  = 8;

    private ToolStripButton                              newCategorisationButton;
    private ToolStripButton                              deleteCategorisationButton;
    private CustomListGrid                               categorisationListGrid;

    private SearchMultipleRelatedResourcePaginatedWindow searchCategoriesWindow;
    private DeleteConfirmationWindow                     deleteConfirmationWindow;

    private CategorisationUiHandlers                     uiHandlers;

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
                // searchCategoriesWindow = new NewCategorisationWindow(getConstants().categorisationCreate());
                // newCategorisationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
                //
                // @Override
                // public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                // if (newCategorisationWindow.validateForm()) {
                // getUiHandlers().createCategorisations(newCategorisationWindow.getSelectedCategoryUrns());
                // newCategorisationWindow.destroy();
                // }
                // }
                // });
            }
        });

        // TODO Security
        // newCategorisationButton.setVisibility(CategoriesClientSecurityUtils.canCreateCategorisation() ? Visibility.VISIBLE : Visibility.HIDDEN);

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
        categorisationListGrid.setAutoFitMaxRecords(10);
        categorisationListGrid.setAutoFitData(Autofit.VERTICAL);

        ListGridField codeField = new ListGridField(CategorisationDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField categoryField = new ListGridField(CategorisationDS.CATEGORY, getConstants().category());
        ListGridField urnField = new ListGridField(CategorisationDS.URN, getConstants().identifiableArtefactUrn());
        categorisationListGrid.setFields(codeField, categoryField, urnField);

        categorisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categorisationListGrid.getSelectedRecords().length > 0) {
                    showDeleteCategorisationButton();
                } else {
                    deleteCategorisationButton.hide();
                }
            }
        });

        addMember(new TitleLabel(getConstants().categorisations()));
        addMember(toolStrip);
        addMember(categorisationListGrid);
    }

    public void setCategorisations(List<CategorisationDto> categorisationDtos) {
        categorisationListGrid.setData(RecordUtils.getCategorisationRecords(categorisationDtos));
    }

    public void setCategorySchemes(GetCategorySchemesResult result) {
        if (searchCategoriesWindow != null) {
            // TODO
        }
    }

    public void setCategories(GetCategoriesResult result) {
        if (searchCategoriesWindow != null) {
            List<RelatedResourceDto> categories = RelatedResourceUtils.getRelatedResourceDtosFromCategoryMetamacDtos(result.getCategoryMetamacDtos());
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

    private List<String> getSelectedCategorisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categorisationListGrid.getSelectedRecords()) {
            CategorisationRecord categorisationRecord = (CategorisationRecord) record;
            urns.add(categorisationRecord.getUrn());
        }
        return urns;
    }

    private void showDeleteCategorisationButton() {
        // TODO Security
        deleteCategorisationButton.show();
    }

    private void showSearchCategoriesWindow() {
        SearchViewTextItem categorySchemeItem = new SearchViewTextItem(CategorySchemeDS.URN, getConstants().categorySchemeFilter());
        searchCategoriesWindow = new SearchMultipleRelatedResourcePaginatedWindow(getConstants().categorisationCreate(), MAX_RESULTS, categorySchemeItem, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, searchCategoriesWindow.getRelatedResourceCriteria(), searchCategoriesWindow.getInitialSelectionValue());
            }
        });

        // Load the list of categories (to populate the selection window)
        getUiHandlers().retrieveCategoriesForCategorisations(FIRST_RESULT, MAX_RESULTS, null, null);

        searchCategoriesWindow.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCategoriesForCategorisations(firstResult, maxResults, criteria, searchCategoriesWindow.getInitialSelectionValue());
            }
        });
        searchCategoriesWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                List<RelatedResourceDto> categories = searchCategoriesWindow.getSelectedRelatedResources();
                searchCategoriesWindow.markForDestroy();
                getUiHandlers().createCategorisations(RelatedResourceUtils.getUrnsFromRelatedResourceDtos(categories));
            }
        });
    }
}
