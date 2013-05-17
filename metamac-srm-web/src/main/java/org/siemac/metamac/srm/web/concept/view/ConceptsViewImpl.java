package org.siemac.metamac.srm.web.concept.view;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.NavigablePaginatedListGrid;
import org.siemac.metamac.srm.web.client.widgets.NavigableListGrid;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptsPresenter;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptsUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSearchSectionStack;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ConceptsViewImpl extends ViewWithUiHandlers<ConceptsUiHandlers> implements ConceptsPresenter.ConceptsView {

    private VLayout                   panel;

    private ConceptSearchSectionStack searchSectionStack;

    private NavigablePaginatedListGrid     conceptsListGrid;

    @Inject
    public ConceptsViewImpl() {
        super();

        // Search
        searchSectionStack = new ConceptSearchSectionStack();

        // Concepts

        conceptsListGrid = new NavigablePaginatedListGrid(ConceptsPresenter.ITEM_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveConcepts(firstResult, maxResults, searchSectionStack.getConceptWebCriteria());
            }
        });
        conceptsListGrid.getListGrid().setAutoFitMaxRecords(ConceptsPresenter.ITEM_LIST_MAX_RESULTS);
        conceptsListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        conceptsListGrid.getListGrid().setDataSource(new ConceptDS());
        conceptsListGrid.getListGrid().setUseAllDataSourceFields(false);

        conceptsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String conceptSchemeUrn = ((ConceptRecord) event.getRecord()).getItemSchemeUrn();
                String conceptUrn = ((ConceptRecord) event.getRecord()).getUrn();
                getUiHandlers().goToConcept(conceptSchemeUrn, conceptUrn);
            }
        });

        conceptsListGrid.getListGrid().setFields(ResourceFieldUtils.getConceptListGridFields());

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(conceptsListGrid);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(ConceptsUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
        ((NavigableListGrid) conceptsListGrid.getListGrid()).setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptsPresenter.TYPE_SetContextAreaContentConceptsToolBar) {
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
    public void setConcepts(GetConceptsResult result) {
        setConcepts(result.getConcepts());
        conceptsListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getConcepts().size(), result.getTotalResults());
    }

    private void setConcepts(List<ConceptMetamacBasicDto> conceptDtos) {
        ConceptRecord[] records = new ConceptRecord[conceptDtos.size()];
        int index = 0;
        for (ConceptMetamacBasicDto concept : conceptDtos) {
            records[index++] = RecordUtils.getConceptRecord(concept);
        }
        conceptsListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
