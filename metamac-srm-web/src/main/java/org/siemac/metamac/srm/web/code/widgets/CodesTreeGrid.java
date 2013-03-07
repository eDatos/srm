package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.BaseCodeUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

public class CodesTreeGrid extends BaseCodesTreeGrid {

    private NewCodeWindow                     newCodeWindow;
    private SearchMultipleCodeHierarchyWindow searchMultipleCodeHierarchyWindow;
    private DeleteConfirmationWindow          deleteConfirmationWindow;

    private HandlerRegistration               folderDropHandlerRegistration;

    private MenuItem                          createCodeMenuItem;
    private MenuItem                          addCodeMenuItem;
    private MenuItem                          deleteCodeMenuItem;

    private CodelistMetamacDto                codelistMetamacDto;
    private CodeMetamacVisualisationResult    selectedCode;

    private CodelistOrderVisualisationDto     codelistOrderVisualisationDto;

    private BaseCodeUiHandlers                uiHandlers;

    public CodesTreeGrid() {
        super();

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Order by ORDER field
        setCanSort(true);
        setSortField(CodeDS.ORDER);

        folderDropHandlerRegistration = addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex(); // Absolute position
                if (isDroppable(dropFolder)) {
                    TreeNode[] siblings = getData().getChildren(dropFolder);

                    // We find out the position of the node under the dropFolder
                    int relativePosition = position; // Used to update position
                    int pos = -1;
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == droppedNode) {
                            pos = i;
                        }
                    }
                    if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                        relativePosition--;
                    }

                    String oldItemParent = droppedNode.getAttribute(CodeDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(CodeDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {
                        // Update code parent
                        if (CodesClientSecurityUtils.canUpdateCode(codelistMetamacDto.getLifeCycle().getProcStatus())) {
                            uiHandlers.updateCodeParent(droppedNode.getAttribute(CodeDS.URN), newItemParent, codelistOrderVisualisationDto != null ? codelistOrderVisualisationDto.getUrn() : null);
                        }
                    } else {
                        // Update order
                        if (CodesClientSecurityUtils.canUpdateCodelistOrderVisualisation(codelistMetamacDto.getLifeCycle().getProcStatus())) {
                            // Only update order if there is an order selected and it is not the alphabetical one
                            if (codelistOrderVisualisationDto != null && !SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(codelistOrderVisualisationDto.getCode())) {
                                uiHandlers.updateCodeInOrder(droppedNode.getAttribute(CodeDS.URN), codelistOrderVisualisationDto.getUrn(), relativePosition);
                            }
                        }
                    }
                }
                event.cancel();
            }
        });

        // Context menu

        createCodeMenuItem = new MenuItem(getConstants().codeCreate());
        createCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newCodeWindow = new NewCodeWindow(getConstants().codeCreate());
                newCodeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodeWindow.validateForm()) {
                            CodeMetamacDto codeMetamacDto = newCodeWindow.getNewCodeDto();
                            codeMetamacDto.setItemSchemeVersionUrn(codelistMetamacDto.getUrn()); // Set codelist URN
                            codeMetamacDto.setItemParentUrn(selectedCode != null ? selectedCode.getUrn() : null); // Set code parent URN
                            CodesTreeGrid.this.uiHandlers.saveCode(codeMetamacDto);
                            newCodeWindow.destroy();
                        }
                    }
                });
            }
        });

        addCodeMenuItem = new MenuItem(getConstants().codeAddFromCodelist());
        addCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                searchMultipleCodeHierarchyWindow = new SearchMultipleCodeHierarchyWindow(codelistMetamacDto, uiHandlers);
            }
        });

        deleteCodeMenuItem = new MenuItem(getConstants().codeDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistDeleteConfirmationTitle(), getConstants().codeDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodesTreeGrid.this.uiHandlers.deleteCode(codelistMetamacDto.getUrn(), selectedCode);
            }
        });
        deleteCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createCodeMenuItem, addCodeMenuItem, deleteCodeMenuItem);
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    public void setItems(ItemSchemeDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        this.codelistOrderVisualisationDto = codelistOrderVisualisationDto;
        setItems(codelistMetamacDto, codes);
    }

    @Override
    public void setItems(ItemSchemeDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        this.codelistMetamacDto = (CodelistMetamacDto) codelistMetamacDto;
        super.setItems(codelistMetamacDto, codes);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.codelistMetamacDto = (CodelistMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseCodeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    protected void onNodeClick(String nodeName, String codeUrn) {
        if (!SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToCode(codeUrn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        selectedCode = code;
        createCodeMenuItem.setEnabled(CodesClientSecurityUtils.canCreateCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        addCodeMenuItem.setEnabled(CodesClientSecurityUtils.canCreateCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        deleteCodeMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName) && CodesClientSecurityUtils.canDeleteCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        showContextMenu();
    }

    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        searchMultipleCodeHierarchyWindow.setCodelists(result);
    }

    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        searchMultipleCodeHierarchyWindow.setCodes(codelistMetamacDto, codes);
    }

    private boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }
}
