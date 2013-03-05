package org.siemac.metamac.srm.web.organisation.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationsPresenter;
import org.siemac.metamac.srm.web.organisation.utils.RecordUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationsUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSearchSectionStack;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsResult;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
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

public class OrganisationsViewImpl extends ViewWithUiHandlers<OrganisationsUiHandlers> implements OrganisationsPresenter.OrganisationsView {

    private VLayout                        panel;

    private OrganisationSearchSectionStack searchSectionStack;

    private PaginatedListGrid              organisationsListGrid;

    @Inject
    public OrganisationsViewImpl() {
        super();

        // Search
        searchSectionStack = new OrganisationSearchSectionStack();

        // Organisations

        organisationsListGrid = new PaginatedListGrid(OrganisationsPresenter.ITEM_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveOrganisations(firstResult, maxResults, searchSectionStack.getOrganisationWebCriteria());
            }
        });
        organisationsListGrid.getListGrid().setAutoFitMaxRecords(OrganisationsPresenter.ITEM_LIST_MAX_RESULTS);
        organisationsListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        organisationsListGrid.getListGrid().setDataSource(new OrganisationDS());
        organisationsListGrid.getListGrid().setUseAllDataSourceFields(false);

        organisationsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String organisationSchemeUrn = ((OrganisationRecord) event.getRecord()).getOrganisationSchemeUrn();
                String organisationUrn = ((OrganisationRecord) event.getRecord()).getUrn();
                OrganisationTypeEnum organisationTypeEnum = ((OrganisationRecord) event.getRecord()).getOrganisationType();
                getUiHandlers().goToOrganisation(organisationSchemeUrn, organisationUrn, organisationTypeEnum);
            }
        });

        ListGridField fieldCode = new ListGridField(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(OrganisationDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(OrganisationDS.URN, getConstants().identifiableArtefactUrn());
        organisationsListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(organisationsListGrid);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(OrganisationsUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == OrganisationsPresenter.TYPE_SetContextAreaContentOrganisationsToolBar) {
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
    public void setOrganisations(GetOrganisationsResult result) {
        setOrganisations(result.getOrganisations());
        organisationsListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getOrganisations().size(), result.getTotalResults());
    }

    private void setOrganisations(List<OrganisationMetamacDto> organisationDtos) {
        OrganisationRecord[] records = new OrganisationRecord[organisationDtos.size()];
        int index = 0;
        for (OrganisationMetamacDto scheme : organisationDtos) {
            records[index++] = RecordUtils.getOrganisationRecord(scheme);
        }
        organisationsListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
