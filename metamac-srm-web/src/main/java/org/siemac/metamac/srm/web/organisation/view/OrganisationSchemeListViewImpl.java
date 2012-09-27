package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemeListPresenter;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.NewOrganisationSchemeWindow;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
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

public class OrganisationSchemeListViewImpl extends ViewWithUiHandlers<OrganisationSchemeListUiHandlers> implements OrganisationSchemeListPresenter.OrganisationSchemeListView {

    private VLayout                     panel;

    private ToolStripButton             newButton;
    private ToolStripButton             deleteButton;
    private ToolStripButton             cancelValidityButton;

    private SearchSectionStack          searchSectionStack;

    private PaginatedCheckListGrid      organisationSchemeList;

    private NewOrganisationSchemeWindow newOrganisationSchemeWindow;
    private DeleteConfirmationWindow    deleteConfirmationWindow;

    @Inject
    public OrganisationSchemeListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newOrganisationSchemeWindow = new NewOrganisationSchemeWindow(getConstants().organisationSchemeCreate());
                newOrganisationSchemeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newOrganisationSchemeWindow.validateForm()) {
                            getUiHandlers().createOrganisationScheme(newOrganisationSchemeWindow.getNewOrganisationSchemeDto());
                            newOrganisationSchemeWindow.destroy();
                        }
                    }
                });
            }
        });
        newButton.setVisibility(OrganisationsClientSecurityUtils.canCreateOrganisationScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisibility(Visibility.HIDDEN);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisibility(Visibility.HIDDEN);
        cancelValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(getSelectedOrganisationSchemesUrns());
            }
        });

        toolStrip.addButton(newButton);
        toolStrip.addButton(deleteButton);
        toolStrip.addButton(cancelValidityButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveOrganisationSchemes(OrganisationSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, OrganisationSchemeListPresenter.SCHEME_LIST_MAX_RESULTS,
                        searchSectionStack.getSearchCriteria());
            }
        });

        // Organisations scheme list

        organisationSchemeList = new PaginatedCheckListGrid(OrganisationSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveOrganisationSchemes(firstResult, maxResults);
            }
        });
        organisationSchemeList.getListGrid().setAutoFitMaxRecords(OrganisationSchemeListPresenter.SCHEME_LIST_MAX_RESULTS);
        organisationSchemeList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        organisationSchemeList.getListGrid().setDataSource(new OrganisationSchemeDS());
        organisationSchemeList.getListGrid().setUseAllDataSourceFields(false);
        organisationSchemeList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (organisationSchemeList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(organisationSchemeList.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showListGridCancelValidityDeleteButton(organisationSchemeList.getListGrid().getSelectedRecords());
                } else {
                    deleteButton.hide();
                    cancelValidityButton.hide();
                }
            }
        });

        organisationSchemeList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((OrganisationSchemeRecord) event.getRecord()).getAttribute(OrganisationSchemeDS.URN);
                    OrganisationSchemeTypeEnum type = ((OrganisationSchemeRecord) event.getRecord()).getOrganisationSchemeDto().getType();
                    getUiHandlers().goToOrganisationScheme(urn, type);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField status = new ListGridField(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ListGridField type = new ListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        organisationSchemeList.getListGrid().setFields(fieldCode, fieldName, status, type);

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(organisationSchemeList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationSchemeDeleteConfirmationTitle(), getConstants().organisationSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteOrganisationSchemes(getSelectedOrganisationSchemesUrns());
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
        if (slot == OrganisationSchemeListPresenter.TYPE_SetContextAreaContentOrganisationSchemeListToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.ORGANISATIONS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
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
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    @Override
    public void setOrganisationSchemesPaginatedList(GetOrganisationSchemeListResult result) {
        setOrganisationSchemeList(result.getOrganisationSchemeMetamacDtos());
        organisationSchemeList.refreshPaginationInfo(result.getPageNumber(), result.getOrganisationSchemeMetamacDtos().size(), result.getTotalResults());
    }

    private void setOrganisationSchemeList(List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos) {
        OrganisationSchemeRecord[] records = new OrganisationSchemeRecord[organisationSchemeMetamacDtos.size()];
        for (int i = 0; i < organisationSchemeMetamacDtos.size(); i++) {
            records[i] = org.siemac.metamac.srm.web.organisation.utils.RecordUtils.getOrganisationSchemeRecord(organisationSchemeMetamacDtos.get(i));
        }
        organisationSchemeList.getListGrid().setData(records);
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = ((OrganisationSchemeRecord) record).getOrganisationSchemeDto();
            if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())
                    || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted && OrganisationsClientSecurityUtils.canDeleteOrganisationScheme()) {
            deleteButton.show();
        } else {
            deleteButton.hide();
        }
    }

    private void showListGridCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = ((OrganisationSchemeRecord) record).getOrganisationSchemeDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(organisationSchemeMetamacDto.getLifeCycle().getProcStatus()) || organisationSchemeMetamacDto.getValidTo() != null) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled && OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity()) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }

    private List<String> getSelectedOrganisationSchemesUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : organisationSchemeList.getListGrid().getSelectedRecords()) {
            OrganisationSchemeRecord schemeRecord = (OrganisationSchemeRecord) record;
            urns.add(schemeRecord.getUrn());
        }
        return urns;
    }

}
