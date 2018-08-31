package org.siemac.metamac.srm.web.organisation.view;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.NavigablePaginatedListGrid;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.presenter.OrganisationsPresenter;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsRecordUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationsUiHandlers;
import org.siemac.metamac.srm.web.organisation.widgets.OrganisationSearchSectionStack;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsResult;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class OrganisationsViewImpl extends ViewWithUiHandlers<OrganisationsUiHandlers> implements OrganisationsPresenter.OrganisationsView {

    private VLayout                        panel;

    private OrganisationSearchSectionStack searchSectionStack;

    private NavigablePaginatedListGrid     organisationsListGrid;

    @Inject
    public OrganisationsViewImpl() {
        super();

        // Search
        searchSectionStack = new OrganisationSearchSectionStack();

        // Organisations

        organisationsListGrid = new NavigablePaginatedListGrid(SrmWebConstants.ITEM_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveOrganisations(firstResult, maxResults, searchSectionStack.getOrganisationWebCriteria());
            }
        });
        organisationsListGrid.getListGrid().setDataSource(new OrganisationDS());
        organisationsListGrid.getListGrid().setUseAllDataSourceFields(false);

        organisationsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String organisationSchemeUrn = ((OrganisationRecord) event.getRecord()).getItemSchemeUrn();
                String organisationUrn = ((OrganisationRecord) event.getRecord()).getUrn();
                OrganisationTypeEnum organisationTypeEnum = ((OrganisationRecord) event.getRecord()).getOrganisationType();
                getUiHandlers().goToOrganisation(organisationSchemeUrn, organisationUrn, organisationTypeEnum);
            }
        });

        organisationsListGrid.getListGrid().setFields(ResourceFieldUtils.getOrganisationListGridFields());
        organisationsListGrid.setHeight100();

        panel = new VLayout();
        panel.addMember(searchSectionStack);
        panel.addMember(organisationsListGrid);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(OrganisationsUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        organisationsListGrid.setUiHandlers(uiHandlers);
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

    private void setOrganisations(List<OrganisationMetamacBasicDto> organisationDtos) {
        OrganisationRecord[] records = new OrganisationRecord[organisationDtos.size()];
        int index = 0;
        for (OrganisationMetamacBasicDto scheme : organisationDtos) {
            records[index++] = OrganisationsRecordUtils.getOrganisationRecord(scheme);
        }
        organisationsListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
