package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedItem;
import org.siemac.metamac.srm.web.code.view.handlers.BaseCodeUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Window with a {@link CodesCheckboxTreeGrid} and a {@link SearchRelatedResourcePaginatedItem} as the codelist filter.
 */
public class SearchMultipleCodeHierarchyWindow extends CustomWindow {

    private static final String                  FORM_ITEM_CUSTOM_WIDTH      = FormItemUtils.FORM_ITEM_WIDTH;

    private static final int                     FIRST_RESULTS               = 0;
    private static final int                     MAX_RESULTS                 = 6;

    private static final String                  TREE_NODE_SELECTED_PROPERTY = "isSelected";

    protected CustomDynamicForm                  filterForm;
    protected CustomDynamicForm                  form;

    protected SearchRelatedResourcePaginatedItem filterListItem;
    protected ViewTextItem                       filterTextItem;

    protected CodesCheckboxTreeGrid              codesTreeGrid;

    private CodelistMetamacDto                   complexCodelistToAddCodes;                                  // Codelist where the selected codes will be inserted
    private String                               selectedCodelistUrn;                                        // Codelist selected. The codes of this codelist will be inserted in the complex codelist
    private BaseCodeUiHandlers                   uiHandlers;

    private boolean                              cascadeSelection            = true;

    public SearchMultipleCodeHierarchyWindow(CodelistMetamacDto complexCodelistToAddCodes, BaseCodeUiHandlers uiHandlers) {
        super(getConstants().codeAdd());
        this.setUiHandlers(uiHandlers);
        this.complexCodelistToAddCodes = complexCodelistToAddCodes;
        setAutoSize(true);

        // FILTER SECTION

        filterListItem = new SearchRelatedResourcePaginatedItem("filterList", getConstants().codelistSelection(), FORM_ITEM_CUSTOM_WIDTH, MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelistsForCreateComplexCodelists(firstResult, maxResults, getCodelistWebCriteriaForCreateComplexCodelist(filterListItem.getSearchCriteria()));
            }
        });
        filterListItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCodelistsForCreateComplexCodelists(firstResult, maxResults, getCodelistWebCriteriaForCreateComplexCodelist(criteria));
            }
        });
        filterListItem.setTitleStyle("staticFormItemTitle");
        filterListItem.getListGrid().setSelectionType(SelectionStyle.SINGLE);
        filterListItem.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {

                RelatedResourceDto selectedRelatedResourceDto = filterListItem.getSelectedRelatedResource();
                if (selectedRelatedResourceDto != null) {
                    // Load codes of the codelist
                    getUiHandlers().retrieveCodesForCreateComplexCodelists(selectedRelatedResourceDto.getUrn());
                    filterTextItem.setValue(RelatedResourceUtils.getRelatedResourceName(selectedRelatedResourceDto));
                } else {
                    filterTextItem.clearValue();
                }
                filterForm.markForRedraw();
            }
        });

        filterTextItem = new ViewTextItem("filterText", getConstants().codelistSelected());
        filterTextItem.setWidth(200);
        filterTextItem.setTitleColSpan(2);
        filterTextItem.setShowIfCondition(getFilterTextFormItemIfFunction());

        filterForm = new CustomDynamicForm();
        filterForm.setTitleOrientation(TitleOrientation.TOP);
        filterForm.setMargin(15);
        filterForm.setNumCols(3);
        filterForm.setFields(filterListItem, filterTextItem);

        // TREEGRID TITLE

        Label title = new Label(getConstants().codesSelectionToAdd() + ":");
        title.setMargin(15);
        title.setHeight(20);
        title.setWidth100();
        title.setStyleName("staticFormItemTitle");

        // TREE SELECTION TYPE

        final CheckboxItem partialSelectionItem = new CheckboxItem("cascadeSelection", getConstants().actionTreeCascadeSelection());
        partialSelectionItem.setDefaultValue(true);
        partialSelectionItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                cascadeSelection = partialSelectionItem.getValueAsBoolean();
            }
        });

        DynamicForm treeSelectionTypeForm = new DynamicForm();
        treeSelectionTypeForm.setFields(partialSelectionItem);

        // CODES TREEGRID

        codesTreeGrid = new CodesCheckboxTreeGrid();
        codesTreeGrid.setMargin(15);
        codesTreeGrid.setWidth(760);
        codesTreeGrid.setSelectionProperty(TREE_NODE_SELECTED_PROPERTY);
        codesTreeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                Record selectedRecord = event.getRecord();
                if (selectedRecord != null) {
                    if (cascadeSelection) {
                        if (selectedRecord.getAttributeAsBoolean(TREE_NODE_SELECTED_PROPERTY) != null) {
                            updateNodeSelectionStatusInCascade((TreeNode) selectedRecord, selectedRecord.getAttributeAsBoolean(TREE_NODE_SELECTED_PROPERTY));
                        }
                    } else {
                        // If the cascade selection is not enabled, and the selected node is the codelist node, DO NOT SELECT the node
                        if (StringUtils.equals(selectedCodelistUrn, selectedRecord.getAttributeAsString(ItemDS.URN))) {
                            ((TreeNode) selectedRecord).setAttribute(TREE_NODE_SELECTED_PROPERTY, false);
                        }
                    }
                }
            }
        });

        CustomButtonItem saveItem = new CustomButtonItem("save-cod", MetamacWebCommon.getConstants().accept());
        saveItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                ListGridRecord[] selectedRecords = codesTreeGrid.getSelectedRecords();
                for (ListGridRecord record : selectedRecords) {
                    if (!StringUtils.equals(selectedCodelistUrn, record.getAttribute(ItemDS.URN))) { // Do not add the codelist node!
                        String recordParent = record.getAttribute(ItemDS.ITEM_PARENT_URN);
                        if (recordParent == null) {
                            // TODO ¿Necesario conservar jerarquía?
                        }
                    }
                }
            }
        });

        form = new CustomDynamicForm();
        form.setMargin(15);
        form.setWidth(760);
        form.setFields(saveItem);

        addItem(filterForm);
        addItem(title);
        addItem(treeSelectionTypeForm);
        addItem(codesTreeGrid);
        addItem(form);

        // Load codelists
        getUiHandlers().retrieveCodelistsForCreateComplexCodelists(FIRST_RESULTS, MAX_RESULTS, getCodelistWebCriteriaForCreateComplexCodelist(null));

        show();
    }

    public void setCodelists(GetCodelistsResult result) {
        List<RelatedResourceDto> codelists = RelatedResourceUtils.getCodelistDtosAsRelatedResourceDtos(result.getCodelists());
        filterListItem.setRelatedResources(codelists);
        filterListItem.refreshPaginationInfo(result.getFirstResultOut(), codelists.size(), result.getTotalResults());
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        this.selectedCodelistUrn = codelistMetamacDto.getUrn();
        codesTreeGrid.setItems(codelistMetamacDto, codes);
    }

    public void setUiHandlers(BaseCodeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public BaseCodeUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private FormItemIfFunction getFilterTextFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return filterTextItem.getValue() != null && !StringUtils.isBlank(filterTextItem.getValue().toString());
            }
        };
    }

    private CodelistWebCriteria getCodelistWebCriteriaForCreateComplexCodelist(String criteria) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(criteria);
        // Do not include the codes of the own codelist
        codelistWebCriteria.setIsNotCodelistUrn(complexCodelistToAddCodes.getUrn());
        return codelistWebCriteria;
    }

    private void updateNodeSelectionStatusInCascade(TreeNode treeNode, boolean selectionStatus) {
        // When setting this property to TRUE/FALSE, the treeNode is selected/unselected WITHOUT firing a new SelectionChanged event
        treeNode.setAttribute(TREE_NODE_SELECTED_PROPERTY, selectionStatus);

        // Select the treeNode children
        Record[] children = codesTreeGrid.getRecordList().findAll(ItemDS.ITEM_PARENT_URN, treeNode.getAttributeAsString(ItemDS.URN));
        if (children != null) {
            for (Record child : children) {
                updateNodeSelectionStatusInCascade((TreeNode) child, selectionStatus);
            }
        }
        codesTreeGrid.markForRedraw();
    }
}
