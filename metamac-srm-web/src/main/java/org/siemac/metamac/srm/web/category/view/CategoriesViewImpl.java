package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.presenter.CategoriesPresenter;
import org.siemac.metamac.srm.web.category.utils.RecordUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategoriesUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategorySearchSectionStack;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CategoriesViewImpl extends ViewWithUiHandlers<CategoriesUiHandlers> implements CategoriesPresenter.CategoriesView {

    private VLayout                    panel;

    private CategorySearchSectionStack searchSectionStack;

    private PaginatedListGrid          categoriesListGrid;

    @Inject
    public CategoriesViewImpl() {
        super();

        // Search
        searchSectionStack = new CategorySearchSectionStack();

        // Categories

        categoriesListGrid = new PaginatedListGrid(CategoriesPresenter.ITEM_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategories(firstResult, maxResults, searchSectionStack.getCategoryWebCriteria());
            }
        });
        categoriesListGrid.getListGrid().setAutoFitMaxRecords(CategoriesPresenter.ITEM_LIST_MAX_RESULTS);
        categoriesListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        categoriesListGrid.getListGrid().setDataSource(new CategoryDS());
        categoriesListGrid.getListGrid().setUseAllDataSourceFields(false);

        categoriesListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String categorySchemeUrn = ((CategoryRecord) event.getRecord()).getCategorySchemeUrn();
                String categoryUrn = ((CategoryRecord) event.getRecord()).getUrn();
                getUiHandlers().goToCategory(categorySchemeUrn, categoryUrn);
            }
        });

        ListGridField fieldCode = new ListGridField(CategoryDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(CategoryDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(CategoryDS.URN, getConstants().identifiableArtefactUrn());
        categoriesListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(categoriesListGrid);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(CategoriesUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategoriesPresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
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
    public void setCategories(GetCategoriesResult result) {
        setCategories(result.getCategoryMetamacDtos());
        categoriesListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getCategoryMetamacDtos().size(), result.getTotalResults());
    }

    private void setCategories(List<CategoryMetamacBasicDto> categoryDtos) {
        CategoryRecord[] records = new CategoryRecord[categoryDtos.size()];
        int index = 0;
        for (CategoryMetamacBasicDto scheme : categoryDtos) {
            records[index++] = RecordUtils.getCategoryRecord(scheme);
        }
        categoriesListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
