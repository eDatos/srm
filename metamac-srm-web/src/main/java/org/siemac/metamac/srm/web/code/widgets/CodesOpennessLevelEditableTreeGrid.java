package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * This tree is used to configure the openness levels of the codes of a codelist. All the nodes are shown opened. An editable column lets the user to change the state of a node (opened or closed).
 */
public class CodesOpennessLevelEditableTreeGrid extends BaseCodesTreeGrid {

    public CodesOpennessLevelEditableTreeGrid() {
        super(false, true);

        setShowFilterEditor(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCanSort(false);
        setCustomIconProperty(CodeDS.OPENNESS_LEVEL_ICON);
        setAutoFitMaxRecords(10);

        // Do not show info field
        infoField.setHidden(true);

        // Add a field with the node openness state

        TreeGridField opennessLevelField = new TreeGridField(CodeDS.OPENNESS_LEVEL, getConstants().codelistOpennessLevelOpened());
        opennessLevelField.setType(ListGridFieldType.BOOLEAN);
        opennessLevelField.setCanEdit(true);
        opennessLevelField.setCanFilter(false);
        opennessLevelField.setShowHover(false);

        ListGridField[] itemFields = getAllFields();

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
