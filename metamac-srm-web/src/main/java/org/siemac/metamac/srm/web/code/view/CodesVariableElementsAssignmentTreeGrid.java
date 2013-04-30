package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.utils.CodesTreeGridUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.BaseCodesTreeGrid;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesVariableElementsAssignmentTreeGrid extends BaseCodesTreeGrid {

    private CodelistUiHandlers                   uiHandlers;

    private SearchRelatedResourcePaginatedWindow searchVariableElementWindow;

    public CodesVariableElementsAssignmentTreeGrid() {
        super(false, false);

        setShowFilterEditor(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCanSort(false);

        // Add a field with the variable element

        TreeGridField variableElementField = new TreeGridField(CodeDS.VARIABLE_ELEMENT, getConstants().variableElement());
        variableElementField.setCanFilter(false);
        variableElementField.setAlign(Alignment.RIGHT);
        // final String searchIconHTML = Canvas.imgHTML(GlobalResources.RESOURCE.search().getURL(), 16, 16);
        // variableElementField.setCellFormatter(new CellFormatter() {
        //
        // @Override
        // public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
        // StringBuilder builder = new StringBuilder();
        // if (value != null) {
        // builder.append(value.toString()).append(" ");
        // }
        // builder.append(searchIconHTML);
        // return builder.toString();
        // }
        // });

        TreeGridField editField = new TreeGridField(CodeDS.VARIABLE_ELEMENT_EDITION, " "); // Do not show title in this column (an space is needed)
        editField.setCanFilter(false);
        editField.setCanEdit(false);
        editField.setWidth(30);
        editField.setAlign(Alignment.LEFT);
        editField.setType(ListGridFieldType.IMAGE);
        editField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getRecordNum() != 0) { // Skip the root node (codelist node)
                    showSearchVariableElementWindow(event.getRecord());
                }
            }
        });

        ListGridField[] itemFields = getAllFields();

        // Set all fields non editable
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 2];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 2] = variableElementField;
        codeFields[codeFields.length - 1] = editField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult) {
        // Do nothing
    }

    public void setCodesVariableElementsNormalised(CodelistMetamacDto codelistMetamacDto, List<CodeVariableElementNormalisationResult> codeVariableElementNormalisationResults) {
        setCodesAndVariableElements(codelistMetamacDto, codeVariableElementNormalisationResults);
    }

    public void setVariableElementsForManualNormalisation(GetRelatedResourcesResult result) {
        if (searchVariableElementWindow != null) {
            searchVariableElementWindow.setRelatedResources(result.getRelatedResourceDtos());
            searchVariableElementWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getRelatedResourceDtos().size(), result.getTotalResults());
        }
    }

    public Map<Long, Long> getCodesVariableElements() {
        Map<Long, Long> codesVariableElements = new HashMap<Long, Long>();
        TreeNode[] nodes = getTree().getAllNodes();
        for (TreeNode node : nodes) {
            if (!SCHEME_NODE_NAME.equals(node.getName())) {
                Long codeId = node.getAttributeAsLong(CodeDS.ID_DATABASE);
                Long variableElementId = node.getAttributeAsLong(CodeDS.VARIABLE_ELEMENT_ID_DATABASE);
                codesVariableElements.put(codeId, variableElementId);
            }
        }
        return codesVariableElements;
    }

    public void setViewMode() {
        hideField(CodeDS.VARIABLE_ELEMENT_EDITION);
    }

    public void setEditionMode() {
        showField(CodeDS.VARIABLE_ELEMENT_EDITION);
    }

    public CodelistUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private void showSearchVariableElementWindow(final Record record) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        final String codelistUrn = codelistMetamacDto.getUrn();

        searchVariableElementWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableElementSelection(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElementsForManualNormalisation(firstResult, maxResults, searchVariableElementWindow.getRelatedResourceCriteria(), codelistUrn);
            }
        });

        // Load variables (to populate the selection window)
        getUiHandlers().retrieveVariableElementsForManualNormalisation(FIRST_RESULST, MAX_RESULTS, null, codelistUrn);

        searchVariableElementWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchVariableElementWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveVariableElementsForManualNormalisation(firstResult, maxResults, criteria, codelistUrn);
            }
        });
        searchVariableElementWindow.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                RelatedResourceDto selectedVariableElement = searchVariableElementWindow.getSelectedRelatedResource();

                CodesTreeGridUtils.setVariableElementInRecord(record, selectedVariableElement);

                updateData(record);

                markForRedraw();
                searchVariableElementWindow.markForDestroy();
            }
        });
    }
}
