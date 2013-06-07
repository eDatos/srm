package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourcePaginatedCheckListGrid;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationSchemeListPresenter;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeListUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.NewOrganisationSchemeWindow;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSchemeSearchSectionStack;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
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

public class OrganisationSchemeListViewImpl extends ViewWithUiHandlers<OrganisationSchemeListUiHandlers> implements OrganisationSchemeListPresenter.OrganisationSchemeListView {

    private VLayout                                   panel;

    private ToolStripButton                           newButton;
    private ToolStripButton                           deleteButton;
    private ToolStripButton                           cancelValidityButton;

    private OrganisationSchemeSearchSectionStack      searchSectionStack;

    private VersionableResourcePaginatedCheckListGrid organisationSchemeList;

    private NewOrganisationSchemeWindow               newOrganisationSchemeWindow;
    private DeleteConfirmationWindow                  deleteConfirmationWindow;

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
        newButton.setVisible(OrganisationsClientSecurityUtils.canCreateOrganisationScheme());

        deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisible(false);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisible(false);
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

        searchSectionStack = new OrganisationSchemeSearchSectionStack();

        // Organisations scheme list

        organisationSchemeList = new VersionableResourcePaginatedCheckListGrid(SrmWebConstants.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveOrganisationSchemes(firstResult, maxResults, searchSectionStack.getOrganisationSchemeWebCriteria());
            }
        });
        organisationSchemeList.getListGrid().setAutoFitMaxRecords(SrmWebConstants.SCHEME_LIST_MAX_RESULTS);
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
                    hideSelectionDependentButtons();
                }
            }
        });

        organisationSchemeList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((OrganisationSchemeRecord) event.getRecord()).getAttribute(OrganisationSchemeDS.URN);
                    OrganisationSchemeTypeEnum type = ((OrganisationSchemeRecord) event.getRecord()).getOrganisationSchemeBasicDto().getType();
                    getUiHandlers().goToOrganisationScheme(urn, type);
                }
            }
        });
        organisationSchemeList.getListGrid().setFields(ResourceFieldUtils.getOrganisationSchemeListGridFields());

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationSchemeDeleteConfirmationTitle(), getConstants().organisationSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteOrganisationSchemes(getSelectedOrganisationSchemesUrns());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.setHeight100();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(toolStrip);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(organisationSchemeList);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationSchemeListPresenter.TYPE_SetContextAreaContentOrganisationsToolBar) {
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
    public void setUiHandlers(OrganisationSchemeListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationSchemeList.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public void setOrganisationSchemesPaginatedList(GetOrganisationSchemesResult result) {
        setOrganisationSchemeList(result.getOrganisationSchemeMetamacDtos());
        organisationSchemeList.refreshPaginationInfo(result.getFirstResultOut(), result.getOrganisationSchemeMetamacDtos().size(), result.getTotalResults());
        hideSelectionDependentButtons();
    }

    private void setOrganisationSchemeList(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos) {
        OrganisationSchemeRecord[] records = new OrganisationSchemeRecord[organisationSchemeMetamacDtos.size()];
        for (int i = 0; i < organisationSchemeMetamacDtos.size(); i++) {
            records[i] = org.siemac.metamac.srm.web.organisation.utils.RecordUtils.getOrganisationSchemeRecord(organisationSchemeMetamacDtos.get(i));
        }
        organisationSchemeList.getListGrid().setData(records);
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            OrganisationSchemeMetamacBasicDto organisationSchemeMetamacDto = ((OrganisationSchemeRecord) record).getOrganisationSchemeBasicDto();
            if (!OrganisationsClientSecurityUtils.canDeleteOrganisationScheme(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted) {
            deleteButton.show();
        } else {
            deleteButton.hide();
        }
    }

    private void showListGridCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            OrganisationSchemeMetamacBasicDto organisationSchemeMetamacDto = ((OrganisationSchemeRecord) record).getOrganisationSchemeBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity(organisationSchemeMetamacDto)) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled) {
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

    private void hideSelectionDependentButtons() {
        deleteButton.hide();
        cancelValidityButton.hide();
    }
}
