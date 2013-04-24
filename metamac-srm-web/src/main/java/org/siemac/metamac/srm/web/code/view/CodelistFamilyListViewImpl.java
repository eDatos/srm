package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.web.code.model.ds.CodelistFamilyDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistFamilyRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistFamilyListPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistFamilyListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.NewCodelistFamilyWindow;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesResult;
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

public class CodelistFamilyListViewImpl extends ViewWithUiHandlers<CodelistFamilyListUiHandlers> implements CodelistFamilyListPresenter.CodelistFamilyListView {

    private VLayout                  panel;

    private ToolStripButton          newCodelistFamilyButton;
    private ToolStripButton          deleteCodelistFamilyButton;

    private SearchSectionStack       searchSectionStack;

    private PaginatedCheckListGrid   codelistFamilyList;

    private NewCodelistFamilyWindow  newCodelistFamilyWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public CodelistFamilyListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCodelistFamilyButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCodelistFamilyButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newCodelistFamilyWindow = new NewCodelistFamilyWindow(getConstants().codelistFamilyCreate());
                newCodelistFamilyWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistFamilyWindow.validateForm()) {
                            getUiHandlers().createCodelistFamily(newCodelistFamilyWindow.getNewCodelistFamilyDto());
                            newCodelistFamilyWindow.destroy();
                        }
                    }
                });
            }
        });
        newCodelistFamilyButton.setVisibility(CodesClientSecurityUtils.canCreateCodelistFamily() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteCodelistFamilyButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCodelistFamilyButton.setVisibility(Visibility.HIDDEN);
        deleteCodelistFamilyButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newCodelistFamilyButton);
        toolStrip.addButton(deleteCodelistFamilyButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveCodelistFamilies(CodelistFamilyListPresenter.FAMILY_LIST_FIRST_RESULT, CodelistFamilyListPresenter.FAMILY_LIST_MAX_RESULTS,
                        searchSectionStack.getSearchCriteria());
            }
        });

        // Family list

        codelistFamilyList = new PaginatedCheckListGrid(CodelistFamilyListPresenter.FAMILY_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelistFamilies(firstResult, maxResults, searchSectionStack.getSearchCriteria());
            }
        });
        codelistFamilyList.getListGrid().setAutoFitMaxRecords(CodelistFamilyListPresenter.FAMILY_LIST_MAX_RESULTS);
        codelistFamilyList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        codelistFamilyList.getListGrid().setDataSource(new CodelistFamilyDS());
        codelistFamilyList.getListGrid().setUseAllDataSourceFields(false);
        codelistFamilyList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (codelistFamilyList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(codelistFamilyList.getListGrid().getSelectedRecords());
                } else {
                    deleteCodelistFamilyButton.hide();
                }
            }
        });

        codelistFamilyList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = ((CodelistFamilyRecord) event.getRecord()).getAttribute(CodelistFamilyDS.CODE);
                    getUiHandlers().goToCodelistFamily(code);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(CodelistFamilyDS.URN, getConstants().identifiableArtefactUrn());
        codelistFamilyList.getListGrid().setFields(fieldCode, fieldName, urn);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistFamilyDeleteConfirmationTitle(), getConstants().codelistFamilyDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCodelistFamilies(getUrnsFromSelectedFamilies());
                deleteConfirmationWindow.hide();
            }
        });

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(toolStrip);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(codelistFamilyList);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistFamilyListPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setCodelistFamilyPaginatedList(GetCodelistFamiliesResult codelistFamiliesPaginatedList) {
        setCodelistFamilies(codelistFamiliesPaginatedList.getFamilies());
        codelistFamilyList
                .refreshPaginationInfo(codelistFamiliesPaginatedList.getFirstResultOut(), codelistFamiliesPaginatedList.getFamilies().size(), codelistFamiliesPaginatedList.getTotalResults());
    }

    private void setCodelistFamilies(List<CodelistFamilyBasicDto> codelistFamilyDtos) {
        CodelistFamilyRecord[] records = new CodelistFamilyRecord[codelistFamilyDtos.size()];
        int index = 0;
        for (CodelistFamilyBasicDto scheme : codelistFamilyDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodelistFamilyRecord(scheme);
        }
        codelistFamilyList.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    private List<String> getUrnsFromSelectedFamilies() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : codelistFamilyList.getListGrid().getSelectedRecords()) {
            CodelistFamilyRecord familyRecord = (CodelistFamilyRecord) record;
            urns.add(familyRecord.getUrn());
        }
        return urns;
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        if (CodesClientSecurityUtils.canDeleteCodelistFamily()) {
            deleteCodelistFamilyButton.show();
        }
    }
}
