package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;

public class CodesOrderTreeGrid extends BaseCodesTreeGrid {

    public CodesOrderTreeGrid() {
        super(false, true);
        setShowFilterEditor(false);

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Disable the option to order by CODE and NAME fields (do not enable the button to order by other fields than order field)

        ListGridField[] itemFields = getAllFields();
        for (ListGridField itemField : itemFields) {
            if (!CodeDS.ORDER.equals(itemField.getName())) {
                itemField.setCanSort(false);
            }
        }

        // Do not let to order in descending direction!!
        addSortChangedHandler(new SortChangedHandler() {

            @Override
            public void onSortChanged(SortEvent event) {
                SortSpecifier[] sortSpecifiers = event.getSortSpecifiers();
                if (sortSpecifiers != null) {
                    for (SortSpecifier sortSpecifier : sortSpecifiers) {
                        if (SortDirection.DESCENDING.equals(sortSpecifier.getSortDirection())) {
                            // Instead of cancel the event (do not know if it is possible), the column is ordered again in ascending order
                            sort(CodeDS.ORDER, SortDirection.ASCENDING);
                        }
                    }
                }
            }
        });

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
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        // Do nothing
    }
}
