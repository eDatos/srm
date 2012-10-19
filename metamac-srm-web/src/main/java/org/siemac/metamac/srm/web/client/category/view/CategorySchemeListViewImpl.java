package org.siemac.metamac.srm.web.client.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.client.category.presenter.CategorySchemeListPresenter;
import org.siemac.metamac.srm.web.client.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.category.view.handlers.CategorySchemeListUiHandlers;
import org.siemac.metamac.srm.web.client.category.widgets.NewCategorySchemeWindow;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeListResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
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

public class CategorySchemeListViewImpl extends ViewWithUiHandlers<CategorySchemeListUiHandlers> implements CategorySchemeListPresenter.CategorySchemeListView {

    private VLayout                  panel;

    private ToolStripButton          newCategorySchemeButton;
    private ToolStripButton          deleteCategorySchemeButton;
    private ToolStripButton          cancelCategorySchemeValidityButton;

    private SearchSectionStack       searchSectionStack;

    private PaginatedCheckListGrid   categorySchemesList;

    private NewCategorySchemeWindow  newCategorySchemeWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

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
        newCategorySchemeButton.setVisibility(CategoriesClientSecurityUtils.canCreateCategoryScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteCategorySchemeButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCategorySchemeButton.setVisibility(Visibility.HIDDEN);
        deleteCategorySchemeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelCategorySchemeValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelCategorySchemeValidityButton.setVisibility(Visibility.HIDDEN);
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

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveCategorySchemes(CategorySchemeListPresenter.SCHEME_LIST_FIRST_RESULT, CategorySchemeListPresenter.SCHEME_LIST_MAX_RESULTS,
                        searchSectionStack.getSearchCriteria());
            }
        });

        // Categories scheme list

        categorySchemesList = new PaginatedCheckListGrid(CategorySchemeListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategorySchemes(firstResult, maxResults, null);
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
                    deleteCategorySchemeButton.hide();
                    cancelCategorySchemeValidityButton.hide();
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

        ListGridField fieldCode = new ListGridField(CategorySchemeDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(CategorySchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField status = new ListGridField(CategorySchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        categorySchemesList.getListGrid().setFields(fieldCode, fieldName, status);

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(categorySchemesList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categorySchemeDeleteConfirmationTitle(), getConstants().categorySchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategorySchemes(getUrnsFromSelectedCategorySchemes());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategorySchemeListPresenter.TYPE_SetContextAreaContentCategorySchemeListToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CATEGORIES.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
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
    public void setCategorySchemePaginatedList(GetCategorySchemeListResult categorySchemesPaginatedList) {
        setCategorySchemeList(categorySchemesPaginatedList.getCategorySchemeList());
        categorySchemesList.refreshPaginationInfo(categorySchemesPaginatedList.getPageNumber(), categorySchemesPaginatedList.getCategorySchemeList().size(),
                categorySchemesPaginatedList.getTotalResults());
    }

    private void setCategorySchemeList(List<CategorySchemeMetamacDto> categorySchemesDtos) {
        CategorySchemeRecord[] records = new CategorySchemeRecord[categorySchemesDtos.size()];
        int index = 0;
        for (CategorySchemeMetamacDto scheme : categorySchemesDtos) {
            records[index++] = org.siemac.metamac.srm.web.client.category.utils.RecordUtils.getCategorySchemeRecord(scheme);
        }
        categorySchemesList.getListGrid().setData(records);
    }

    @Override
    public void goToCategorySchemeListLastPageAfterCreate() {
        categorySchemesList.goToLastPageAfterCreate();
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
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
            CategorySchemeMetamacDto categorySchemeMetamacDto = ((CategorySchemeRecord) record).getCategorySchemeDto();
            if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(categorySchemeMetamacDto.getLifeCycle().getProcStatus())
                    || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(categorySchemeMetamacDto.getLifeCycle().getProcStatus()) || !CategoriesClientSecurityUtils.canDeleteCategoryScheme()) {
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
            CategorySchemeMetamacDto categorySchemeMetamacDto = ((CategorySchemeRecord) record).getCategorySchemeDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(categorySchemeMetamacDto.getLifeCycle().getProcStatus()) || categorySchemeMetamacDto.getValidTo() != null
                    || !CategoriesClientSecurityUtils.canCancelCategorySchemeValidity()) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled) {
            cancelCategorySchemeValidityButton.show();
        } else {
            cancelCategorySchemeValidityButton.hide();
        }
    }

}
