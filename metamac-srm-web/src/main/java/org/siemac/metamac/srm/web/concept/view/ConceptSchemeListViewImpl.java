package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptSchemeWindow;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemePaginatedListResult;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeListViewImpl extends ViewImpl implements ConceptSchemeListPresenter.ConceptSchemeListView {

    private ConceptSchemeListUiHandlers uiHandlers;
    private VLayout                     panel;

    private ToolStripButton             newConceptSchemeActor;
    private ToolStripButton             deleteConceptSchemeActor;

    private PaginatedCheckListGrid      conceptSchemesList;

    private DeleteConfirmationWindow    deleteConfirmationWindow;

    @Inject
    public ConceptSchemeListViewImpl() {
        super();

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newConceptSchemeActor = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newConceptSchemeActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final NewConceptSchemeWindow window = new NewConceptSchemeWindow(getConstants().conceptSchemeCreate());
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (window.validateForm()) {
                            uiHandlers.createConceptScheme(window.getNewConceptSchemeDto());
                            window.destroy();
                        }
                    }
                });
            }
        });
        // Security
        newConceptSchemeActor.setVisibility(ClientSecurityUtils.canCreateConceptScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteConceptSchemeActor = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteConceptSchemeActor.setVisibility(Visibility.HIDDEN);
        deleteConceptSchemeActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newConceptSchemeActor);
        toolStrip.addButton(deleteConceptSchemeActor);

        conceptSchemesList = new PaginatedCheckListGrid(ConceptSchemeListPresenter.DEFAULT_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                uiHandlers.retrieveConceptSchemes(firstResult, maxResults);
            }
        });
        conceptSchemesList.setHeight(680);
        conceptSchemesList.getListGrid().setDataSource(new ConceptSchemeDS());
        conceptSchemesList.getListGrid().setUseAllDataSourceFields(false);
        conceptSchemesList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (conceptSchemesList.getListGrid().getSelectedRecords().length > 0) {
                    deleteConceptSchemeActor.show();
                } else {
                    deleteConceptSchemeActor.hide();
                }
            }
        });

        conceptSchemesList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((ConceptSchemeRecord) event.getRecord()).getAttribute(ConceptSchemeDS.URN);
                    uiHandlers.goToConceptScheme(urn);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ListGridField status = new ListGridField(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        conceptSchemesList.getListGrid().setFields(fieldCode, fieldName, status);

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(conceptSchemesList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeDeleteConfirmationTitle(), getConstants().conceptSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteConceptSchemes(getIdsFromSelected());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public void setConceptSchemePaginatedList(GetConceptSchemePaginatedListResult conceptSchemesPaginatedList) {
        setConceptSchemeList(conceptSchemesPaginatedList.getConceptSchemeList());
        conceptSchemesList.refreshPaginationInfo(conceptSchemesPaginatedList.getPageNumber(), conceptSchemesPaginatedList.getConceptSchemeList().size(), conceptSchemesPaginatedList.getTotalResults());
    }

    @Override
    public void goToConceptSchemeListLastPageAfterCreate() {
        conceptSchemesList.goToLastPageAfterCreate();
    }

    private void setConceptSchemeList(List<ConceptSchemeDto> conceptSchemesDtos) {
        ConceptSchemeRecord[] records = new ConceptSchemeRecord[conceptSchemesDtos.size()];
        int index = 0;
        for (ConceptSchemeDto scheme : conceptSchemesDtos) {
            records[index++] = RecordUtils.getConceptSchemeRecord(scheme);
        }
        conceptSchemesList.getListGrid().setData(records);
    }

    public List<Long> getIdsFromSelected() {
        List<Long> codes = new ArrayList<Long>();
        for (ListGridRecord record : conceptSchemesList.getListGrid().getSelectedRecords()) {
            ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
            codes.add(schemeRecord.getId());
        }
        return codes;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemeListPresenter.TYPE_SetContextAreaContentConceptSchemeListToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        if (ToolStripButtonEnum.CONCEPTS.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
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
    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
