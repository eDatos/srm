package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.category.presenter.CategorySchemeListPresenter;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeListUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategorySchemeSearchSectionStack;
import org.siemac.metamac.srm.web.category.widgets.NewCategorySchemeWindow;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourcePaginatedCheckListGrid;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
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

public class CategorySchemeListViewImpl extends ViewWithUiHandlers<CategorySchemeListUiHandlers> implements CategorySchemeListPresenter.CategorySchemeListView {

    private VLayout                                   panel;

    private ToolStripButton                           newCategorySchemeButton;
    private ToolStripButton                           deleteCategorySchemeButton;
    private ToolStripButton                           cancelCategorySchemeValidityButton;

    private CategorySchemeSearchSectionStack          searchSectionStack;

    private VersionableResourcePaginatedCheckListGrid categorySchemesList;

    private NewCategorySchemeWindow                   newCategorySchemeWindow;
    private DeleteConfirmationWindow                  deleteConfirmationWindow;

    @Inject
    public CategorySchemeListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCategorySchemeButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCategorySchemeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newCategorySchemeWindow = new NewCategorySchemeWindow(getConstants().categorySchemeCreate());
                newCategorySchemeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCategorySchemeWindow.validateForm()) {
                            getUiHandlers().createCategoryScheme(newCategorySchemeWindow.getNewCategorySchemeDto());
                            newCategorySchemeWindow.destroy();
                        }
                    }
                });
            }
        });
        newCategorySchemeButton.setVisible(CategoriesClientSecurityUtils.canCreateCategoryScheme());

        deleteCategorySchemeButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCategorySchemeButton.setVisible(false);
        deleteCategorySchemeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelCategorySchemeValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelCategorySchemeValidityButton.setVisible(false);
        cancelCategorySchemeValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(getUrnsFromSelectedCategorySchemes());
            }
        });

        toolStrip.addButton(newCategorySchemeButton);
        toolStrip.addButton(deleteCategorySchemeButton);
        toolStrip.addButton(cancelCategorySchemeValidityButton);

        // Search

        searchSectionStack = new CategorySchemeSearchSectionStack();

        // Categories scheme list

        categorySchemesList = new VersionableResourcePaginatedCheckListGrid(CategorySchemeListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategorySchemes(firstResult, maxResults, searchSectionStack.getCategorySchemeWebCriteria());
            }
        });
        categorySchemesList.getListGrid().setAutoFitMaxRecords(CategorySchemeListPresenter.SCHEME_LIST_MAX_RESULTS);
        categorySchemesList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        categorySchemesList.getListGrid().setDataSource(new CategorySchemeDS());
        categorySchemesList.getListGrid().setUseAllDataSourceFields(false);
        categorySchemesList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categorySchemesList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(categorySchemesList.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showListGridCancelValidityDeleteButton(categorySchemesList.getListGrid().getSelectedRecords());
                } else {
                    hideSelectionDependentButtons();
                }
            }
        });

        categorySchemesList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((CategorySchemeRecord) event.getRecord()).getAttribute(CategorySchemeDS.URN);
                    getUiHandlers().goToCategoryScheme(urn);
                }
            }
        });
        categorySchemesList.getListGrid().setFields(ResourceFieldUtils.getCategorySchemeListGridFields());

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categorySchemeDeleteConfirmationTitle(), getConstants().categorySchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategorySchemes(getUrnsFromSelectedCategorySchemes());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.setHeight100();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(toolStrip);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(categorySchemesList);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategorySchemeListPresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
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
    public void setUiHandlers(CategorySchemeListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        categorySchemesList.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setCategorySchemePaginatedList(GetCategorySchemesResult categorySchemesPaginatedList) {
        setCategorySchemeList(categorySchemesPaginatedList.getCategorySchemeList());
        categorySchemesList.refreshPaginationInfo(categorySchemesPaginatedList.getFirstResultOut(), categorySchemesPaginatedList.getCategorySchemeList().size(),
                categorySchemesPaginatedList.getTotalResults());
        hideSelectionDependentButtons();
    }

    private void setCategorySchemeList(List<CategorySchemeMetamacBasicDto> categorySchemesDtos) {
        CategorySchemeRecord[] records = new CategorySchemeRecord[categorySchemesDtos.size()];
        int index = 0;
        for (CategorySchemeMetamacBasicDto scheme : categorySchemesDtos) {
            records[index++] = org.siemac.metamac.srm.web.category.utils.RecordUtils.getCategorySchemeRecord(scheme);
        }
        categorySchemesList.getListGrid().setData(records);
    }

    @Override
    public void goToCategorySchemeListLastPageAfterCreate() {
        categorySchemesList.goToLastPageAfterCreate();
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    private List<String> getUrnsFromSelectedCategorySchemes() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categorySchemesList.getListGrid().getSelectedRecords()) {
            CategorySchemeRecord schemeRecord = (CategorySchemeRecord) record;
            urns.add(schemeRecord.getUrn());
        }
        return urns;
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            CategorySchemeMetamacBasicDto categorySchemeMetamacDto = ((CategorySchemeRecord) record).getCategorySchemeBasicDto();
            if (!CategoriesClientSecurityUtils.canDeleteCategoryScheme(categorySchemeMetamacDto.getLifeCycle().getProcStatus())) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted) {
            deleteCategorySchemeButton.show();
        } else {
            deleteCategorySchemeButton.hide();
        }
    }

    private void showListGridCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            CategorySchemeMetamacBasicDto categorySchemeMetamacDto = ((CategorySchemeRecord) record).getCategorySchemeBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!CategoriesClientSecurityUtils.canCancelCategorySchemeValidity(categorySchemeMetamacDto)) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled) {
            cancelCategorySchemeValidityButton.show();
        } else {
            cancelCategorySchemeValidityButton.hide();
        }
    }

    private void hideSelectionDependentButtons() {
        deleteCategorySchemeButton.hide();
        cancelCategorySchemeValidityButton.hide();
    }
}
