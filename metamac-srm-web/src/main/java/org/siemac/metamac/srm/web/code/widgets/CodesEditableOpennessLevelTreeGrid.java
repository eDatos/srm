package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class CodesEditableOpennessLevelTreeGrid extends BaseCodesTreeGrid {

    public CodesEditableOpennessLevelTreeGrid() {
        super(false, true);

        setShowFilterEditor(false);
        setAlwaysShowEditors(true);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCanSort(false);
        setCustomIconProperty(CodeDS.OPENNESS_LEVEL_ICON);

        // Add a field with the node openness status

        TreeGridField opennessLevelField = new TreeGridField(CodeDS.OPENNESS_LEVEL, getConstants().codelistOpennessLevel());
        opennessLevelField.setEditorType(new CheckboxItem());
        opennessLevelField.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {

            @Override
            public void onChanged(com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
                System.out.println();
            }
        });
        opennessLevelField.setCanEdit(true);
        opennessLevelField.setCanFilter(false);

        ListGridField[] itemFields = getFields();

        // Set all fields non editable (except the openness level field)
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 1] = opennessLevelField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    public void setItems(ItemSchemeDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOrderVisualisationDto) {
        this.codelistVisualisationDto = codelistOrderVisualisationDto;
        setItems(codelistMetamacDto, codes);

        // Disable item scheme node
        disableItemSchemeNode();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        // Do nothing
    }
}
