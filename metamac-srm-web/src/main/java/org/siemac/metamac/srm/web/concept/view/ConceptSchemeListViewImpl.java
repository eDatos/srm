package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.srm.web.client.view.PaginationViewImpl;
import org.siemac.metamac.srm.web.client.widgets.StatusBar;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.RecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptSchemeWindow;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
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

public class ConceptSchemeListViewImpl extends PaginationViewImpl<ConceptSchemeListUiHandlers> implements ConceptSchemeListPresenter.ConceptSchemeListView {

    private VLayout                  panel;

    private ToolStripButton          newConceptSchemeActor;
    private ToolStripButton          deleteConceptSchemeActor;

    private BaseCustomListGrid       conceptSchemesList;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    private NewConceptSchemeWindow   window;

    @Inject
    public ConceptSchemeListViewImpl(StatusBar statusBar) {
        super(statusBar);

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newConceptSchemeActor = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newConceptSchemeActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                window = new NewConceptSchemeWindow(getConstants().conceptSchemeCreate());
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (window.validateForm()) {
                            getUiHandlers().createConceptScheme(window.getNewConceptSchemeDto());
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

        conceptSchemesList = new BaseCustomListGrid();
        conceptSchemesList.setHeight(680);
        conceptSchemesList.setDataSource(new ConceptSchemeDS());
        conceptSchemesList.setUseAllDataSourceFields(false);
        conceptSchemesList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        conceptSchemesList.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (conceptSchemesList.getSelectedRecords().length > 0) {
                    deleteConceptSchemeActor.show();
                } else {
                    deleteConceptSchemeActor.hide();
                }

                ListGridRecord[] records = event.getSelection();

                setNumberSelected(records.length);

                String selectedLabel = getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
                ConceptSchemeListViewImpl.this.statusBar.getSelectedLabel().setContents(selectedLabel);

            }
        });

        conceptSchemesList.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String idLogic = event.getRecord().getAttribute(ConceptSchemeDS.ID_LOGIC);
                    getUiHandlers().goToConceptScheme(idLogic);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeIdLogic());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ListGridField status = new ListGridField(ConceptSchemeDS.PROC_STATUS, getConstants().conceptSchemeProcStatus());
        conceptSchemesList.setFields(fieldCode, fieldName, status);

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(conceptSchemesList);
        panel.addMember(statusBar);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeDeleteConfirmationTitle(), getConstants().conceptSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteConceptSchemes(getUuidsFromSelected());
                deleteConfirmationWindow.hide();
            }
        });

        initStatusBar();
    }

    @Override
    public void setConceptSchemeList(List<ConceptSchemeDto> conceptSchemesDtos) {
        ConceptSchemeRecord[] records = new ConceptSchemeRecord[conceptSchemesDtos.size()];
        int index = 0;
        for (ConceptSchemeDto scheme : conceptSchemesDtos) {
            records[index++] = RecordUtils.getConceptSchemeRecord(scheme);
        }
        conceptSchemesList.setData(records);
    }

    public List<String> getUuidsFromSelected() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : conceptSchemesList.getSelectedRecords()) {
            codes.add(record.getAttribute(ConceptSchemeDS.UUID));
        }
        return codes;
    }

    public void refreshStatusBar() {
        // update Selected label e.g "0 of 50 selected"
        String selectedLabel = getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
        getStatusBar().getSelectedLabel().setContents(selectedLabel);

        // update Page number label e.g "Page 1"
        String pageNumberLabel = getMessages().page(String.valueOf(getPageNumber()));
        getStatusBar().getPageNumberLabel().setContents(pageNumberLabel);
        getStatusBar().getPageNumberLabel().markForRedraw();
    }

    protected void initStatusBar() {

        // "0 of 50 selected"

        getStatusBar().getResultSetFirstButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (getUiHandlers() != null) {
                    getUiHandlers().onResultSetFirstButtonClicked();
                }
            }
        });

        getStatusBar().getResultSetPreviousButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (getUiHandlers() != null) {
                    getUiHandlers().onResultSetPreviousButtonClicked();
                }
            }
        });

        // "Page 1"

        getStatusBar().getResultSetNextButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (getUiHandlers() != null) {
                    getUiHandlers().onResultSetNextButtonClicked();
                }
            }
        });

        getStatusBar().getResultSetLastButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getUiHandlers() != null) {
                    getUiHandlers().onResultSetLastButtonClicked();
                }
            }
        });
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

}
