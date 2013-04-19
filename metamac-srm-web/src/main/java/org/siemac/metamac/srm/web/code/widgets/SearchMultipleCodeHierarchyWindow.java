package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopyHierarchy;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedItem;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.view.handlers.BaseCodeUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
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
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Window with a {@link CodesSimpleCheckboxTreeGrid} and a {@link SearchRelatedResourcePaginatedItem} as the codelist filter.
 */
public class SearchMultipleCodeHierarchyWindow extends CustomWindow {

    private static final int                     FORM_ITEM_CUSTOM_WIDTH      = 500;

    private static final int                     FIRST_RESULTS               = 0;
    private static final int                     MAX_RESULTS                 = 6;

    private static final String                  TREE_NODE_SELECTED_PROPERTY = "isSelected";

    protected CustomDynamicForm                  filterForm;
    protected CustomDynamicForm                  form;

    protected SearchRelatedResourcePaginatedItem filterListItem;
    protected ViewTextItem                       filterTextItem;

    protected CodesSimpleCheckboxTreeGrid        codesTreeGrid;

    private CodelistMetamacDto                   complexCodelistToAddCodes;                 // Codelist where the selected codes will be inserted
    private TreeNode                             selectedSourceNode;                        // The selected codes will be inserted under this node. The node can a node or the codelist (root node)
    private String                               selectedCodelistUrn;                       // Codelist selected. The codes of this codelist will be inserted in the complex codelist
    private BaseCodeUiHandlers                   uiHandlers;

    private boolean                              cascadeSelection            = true;

    /**
     * The selected codes will be inserted under the selectedSourceNode.
     * 
     * @param complexCodelistToAddCodes
     * @param selectedNode
     * @param uiHandlers
     */
    public SearchMultipleCodeHierarchyWindow(CodelistMetamacDto complexCodelistToAddCodes, TreeNode selectedSourceNode, BaseCodeUiHandlers uiHandlers) {
        super(getConstants().codeAdd());
        this.setUiHandlers(uiHandlers);
        this.complexCodelistToAddCodes = complexCodelistToAddCodes;
        this.selectedSourceNode = selectedSourceNode;
        setAutoSize(true);

        // FILTER SECTION

        filterListItem = new SearchRelatedResourcePaginatedItem("filterList", getConstants().codelistSelection(), String.valueOf(FORM_ITEM_CUSTOM_WIDTH), MAX_RESULTS, new PaginatedAction() {

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

        codesTreeGrid = new CodesSimpleCheckboxTreeGrid();
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
                            updateNodeSelectionStatusInCascade(selectedRecord, selectedRecord.getAttributeAsBoolean(TREE_NODE_SELECTED_PROPERTY));
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

        CustomButtonItem continueItem = new CustomButtonItem("save-cod", MetamacWebCommon.getConstants().actionContinue());
        continueItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                ListGridRecord[] allRecords = codesTreeGrid.getRecords();
                ListGridRecord[] selectedRecords = codesTreeGrid.getSelectedRecords();

                if (isAnyValidCodeSelected(selectedRecords)) {
                    List<TreeNode> nodesToAdd = getCodesHierarchy(allRecords, selectedRecords);
                    if (!nodesToAdd.isEmpty()) {

                        final PreviewWindow previewWindow = new PreviewWindow(getConstants().codesHierarchyToAdd());
                        previewWindow.setCodes(nodesToAdd);
                        previewWindow.getSaveClickHandlers().addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(ClickEvent event) {

                                // If the node where the codes will be inserted is a codelist, parent should be null. Otherwise, specify the source code URN.
                                String sourceNodeUrn = SearchMultipleCodeHierarchyWindow.this.selectedSourceNode.getAttribute(ItemDS.URN); // can be a codelist or a code
                                String parentTargetUrn = StringUtils.equals(sourceNodeUrn, SearchMultipleCodeHierarchyWindow.this.complexCodelistToAddCodes.getUrn()) ? null : sourceNodeUrn;

                                // Add the selected nodes in the codelist
                                getUiHandlers().copyCodesInCodelist(SearchMultipleCodeHierarchyWindow.this.complexCodelistToAddCodes.getUrn(), parentTargetUrn, previewWindow.getCodes());

                                previewWindow.markForDestroy();
                                SearchMultipleCodeHierarchyWindow.this.markForDestroy();
                            }
                        });
                    }
                } else {
                    WarningWindow warningWindow = new WarningWindow(getConstants().codesSelection(), getMessages().codeSelectionRequired());
                    warningWindow.show();
                }
            }
        });

        form = new CustomDynamicForm();
        form.setMargin(15);
        form.setWidth(760);
        form.setFields(continueItem);

        addItem(filterForm);
        addItem(title);
        addItem(treeSelectionTypeForm);
        addItem(codesTreeGrid);
        addItem(form);

        // Load codelists
        getUiHandlers().retrieveCodelistsForCreateComplexCodelists(FIRST_RESULTS, MAX_RESULTS, getCodelistWebCriteriaForCreateComplexCodelist(null));

        show();
    }

    private boolean isAnyValidCodeSelected(ListGridRecord[] selectedRecords) {
        if (selectedRecords != null && selectedRecords.length > 0) {
            if (selectedRecords.length == 1 && StringUtils.equals(selectedRecords[0].getAttribute(ItemDS.URN), selectedCodelistUrn)) {
                // If there is only one node selected, and its the root one (the codelist)
                return false;
            }
            return true;
        }
        return false;
    }

    private List<TreeNode> getCodesHierarchy(ListGridRecord[] allRecords, ListGridRecord[] selectedRecords) {

        // These recordLists are built only for search purposes

        RecordList allRecordsAsRecordList = new RecordList(allRecords);
        RecordList selectedRecordsAsRecordList = new RecordList(selectedRecords);
        RecordUtils.removeRecord(selectedRecordsAsRecordList, ItemDS.URN, selectedCodelistUrn); // Remove the codelist node from selectedRecordsAsRecordList if included (NOTE: the codelist node is
                                                                                                // disabled but can be selected)

        List<TreeNode> nodes = new ArrayList<TreeNode>();

        // Add the root node of the tree:
        // - The root node of this tree will be initial node that was selected, where all these nodes will be inserted
        // - Building the tree this way, the user can preview how the final tree looks like
        TreeNode rootNode = new TreeNode();
        rootNode.setID(selectedSourceNode.getAttribute(ItemDS.URN));
        rootNode.setAttribute(ItemDS.CODE, selectedSourceNode.getAttribute(ItemDS.CODE));
        rootNode.setAttribute(ItemDS.NAME, selectedSourceNode.getAttribute(ItemDS.NAME));
        rootNode.setAttribute(ItemDS.URN, selectedSourceNode.getAttribute(ItemDS.URN));
        rootNode.setEnabled(false);
        nodes.add(rootNode);

        // Add the selected nodes (hierarchy should be preserved)
        for (ListGridRecord record : selectedRecords) {
            if (!StringUtils.equals(selectedCodelistUrn, record.getAttribute(ItemDS.URN))) { // The codelist node cannot be included (NOTE: the codelist node is disabled but can be selected)
                TreeNode treeNode = new TreeNode();
                treeNode.setID(record.getAttribute(ItemDS.URN));
                treeNode.setAttribute(ItemDS.CODE, record.getAttribute(ItemDS.CODE));
                treeNode.setAttribute(ItemDS.NAME, record.getAttribute(ItemDS.NAME));
                treeNode.setAttribute(ItemDS.URN, record.getAttribute(ItemDS.URN));
                treeNode.setParentID(getResultingNodeParentID(record, allRecordsAsRecordList, selectedRecordsAsRecordList));
                nodes.add(treeNode);
            }
        }

        return nodes;
    }

    /**
     * Returns the parent of the record preserving the hierarchy of the tree.
     * <p>
     * Given the tree:
     * <p>
     * A<br>
     * |_ B<br>
     * |__|_ C<br>
     * |__|__|_ D<br>
     * <p>
     * If nodes B and D are selected, the resulting parent of node D will be B.
     * 
     * @param record record whose parent this method return
     * @param allRecords
     * @param selectedRecords
     * @return the URN of the parent of the record
     */
    private String getResultingNodeParentID(Record record, RecordList allRecords, RecordList selectedRecords) {
        String parentUrn = record.getAttribute(ItemDS.ITEM_PARENT_URN);

        if (parentUrn != null) {
            Record parentRecord = selectedRecords.find(ItemDS.URN, parentUrn);

            // The parent of the record is in the selected records
            if (parentRecord != null) {
                return parentRecord.getAttribute(ItemDS.URN);
            }

            // The parent of the record is not in the selected records, but maybe the parent of its parent is selected...
            parentRecord = allRecords.find(ItemDS.URN, parentUrn);
            if (parentRecord != null) { // NullPointerException is checked, although this parentRecourd shoul always be != null
                return getResultingNodeParentID(parentRecord, allRecords, selectedRecords);
            }
        }

        // The record has no parent, so it should be linked to the root node
        return selectedSourceNode.getAttribute(ItemDS.URN);
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

    private void updateNodeSelectionStatusInCascade(Record treeNode, boolean selectionStatus) {
        // When setting this property to TRUE/FALSE, the treeNode is selected/unselected WITHOUT firing a new SelectionChanged event
        treeNode.setAttribute(TREE_NODE_SELECTED_PROPERTY, selectionStatus);

        // Select the treeNode children
        Record[] children = codesTreeGrid.getRecordList().findAll(ItemDS.ITEM_PARENT_URN, treeNode.getAttributeAsString(ItemDS.URN));
        if (children != null) {
            for (Record child : children) {
                updateNodeSelectionStatusInCascade(child, selectionStatus);
            }
        }
        codesTreeGrid.markForRedraw();
    }

    /**
     * This window shows a tree with the codes that have been previously selected. The root of the tree is the initial node (can be a codelist or a code) where all the the selected codes will be
     * copied (inserted). The identifier of the codes (except the root node) can be edited.
     */
    private class PreviewWindow extends CustomWindow {

        private static final String         FIELD_SAVE = "save-codes";
        private CodesSimpleEditableTreeGrid codesEditableTreeGrid;

        public PreviewWindow(String title) {
            super(title);
            setAutoSize(true);

            int width = 650;

            InformationLabel informationLabel = new InformationLabel(getMessages().codesInfoChangeIdentifier());
            informationLabel.setWidth(width);
            informationLabel.setMargin(5);

            codesEditableTreeGrid = new CodesSimpleEditableTreeGrid();
            codesEditableTreeGrid.setWidth(width);
            codesEditableTreeGrid.setMargin(5);
            codesEditableTreeGrid.setAutoFitMaxRecords(15);
            codesEditableTreeGrid.getField(CodeDS.CODE).setWidth("50%");

            CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
            form = new CustomDynamicForm();
            form.setWidth(width);
            form.setMargin(5);
            form.setFields(saveItem);

            addItem(informationLabel);
            addItem(codesEditableTreeGrid);
            addItem(form);

            show();
        }

        public void setCodes(List<TreeNode> nodes) {
            codesEditableTreeGrid.setCodes(nodes);
        }

        public List<CodeToCopyHierarchy> getCodes() {
            return codesEditableTreeGrid.getCodes();
        }

        public HasClickHandlers getSaveClickHandlers() {
            return form.getItem(FIELD_SAVE);
        }
    }
}
