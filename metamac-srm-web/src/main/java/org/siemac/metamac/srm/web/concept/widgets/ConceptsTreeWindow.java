package org.siemac.metamac.srm.web.concept.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ConceptsTreeWindow extends CustomWindow {

    private static final String FIELD_SAVE = "save-ex";

    private CustomDynamicForm   form;

    private ConceptsTreeGrid    conceptsTreeGrid;

    public ConceptsTreeWindow(String title) {
        super(title);
        setAutoSize(true);

        int width = 650;

        conceptsTreeGrid = new ConceptsTreeGrid();
        conceptsTreeGrid.setWidth(width);
        conceptsTreeGrid.setMargin(5);
        conceptsTreeGrid.setAutoFitMaxRecords(15);
        conceptsTreeGrid.getField(ConceptDS.CODE).setWidth("50%");

        // Do not filter concepts
        conceptsTreeGrid.setShowFilterEditor(false);

        // Change selection type (checkBox)
        conceptsTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
        conceptsTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        // Remove node actions
        conceptsTreeGrid.removeHandlerRegistrations();

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        form = new CustomDynamicForm();
        form.setWidth(width);
        form.setMargin(5);
        form.setFields(saveItem);

        addItem(conceptsTreeGrid);
        addItem(form);

        show();
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> itemVisualisationResults) {
        conceptsTreeGrid.setConcepts(conceptSchemeMetamacDto, itemVisualisationResults);
    }

    public HasClickHandlers getSaveClickHandlers() {
        return form.getItem(FIELD_SAVE);
    }

    public List<ItemVisualisationResult> getSelectedConcepts() {
        ListGridRecord[] records = conceptsTreeGrid.getSelectedRecords();
        if (records != null) {
            List<ItemVisualisationResult> itemVisualisationResults = new ArrayList<ItemVisualisationResult>();
            for (ListGridRecord record : records) {
                TreeNode treeNode = (TreeNode) record;
                if (treeNode.getAttribute(ConceptDS.DTO) != null) {
                    itemVisualisationResults.add((ItemVisualisationResult) treeNode.getAttributeAsObject(ConceptDS.DTO));
                }
            }
            return itemVisualisationResults;
        }
        return null;
    }

    public void selectConcepts(List<String> conceptsUrn) {
        RecordList recordList = conceptsTreeGrid.getDataAsRecordList();
        for (String urn : conceptsUrn) {
            Record record = recordList.find(ConceptDS.URN, urn);
            if (record != null) {
                conceptsTreeGrid.selectRecord(record);
            }
        }
    }

    public ConceptsTreeGrid getConceptsTreeGrid() {
        return conceptsTreeGrid;
    }
}
