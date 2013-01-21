package org.siemac.metamac.srm.web.client.code.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.client.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.BaseCodeUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

public class CodesTreeGrid extends ItemsTreeGrid {

    private NewCodeWindow                 newCodeWindow;
    private DeleteConfirmationWindow      deleteConfirmationWindow;

    private MenuItem                      createCodeMenuItem;
    private MenuItem                      deleteCodeMenuItem;

    private CodelistMetamacDto            codelistMetamacDto;
    private ItemDto                       selectedCode;

    private CodelistOrderVisualisationDto codelistOrderVisualisationDto;

    private BaseCodeUiHandlers            uiHandlers;

    public CodesTreeGrid() {
        super();

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex(); // Absolute position
                if (isDroppable(dropFolder)) {
                    TreeNode[] siblings = getData().getChildren(dropFolder);

                    // We find out the position of the node under the dropFolder
                    int relPosition = position; // Use to update position
                    int pos = -1;
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == droppedNode) {
                            pos = i;
                        }
                    }
                    if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                        relPosition--;
                    }

                    Long order = Long.valueOf(relPosition);

                    String oldItemParent = droppedNode.getAttribute(CodeDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? null : dropFolder.getAttribute(CodeDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {
                        uiHandlers.updateCodeParent(droppedNode.getAttribute(CodeDS.URN), dropFolder.getAttribute(CodeDS.URN),
                                codelistOrderVisualisationDto != null ? codelistOrderVisualisationDto.getUrn() : null);
                    } else {
                        // Only update order if there is order selected
                        if (codelistOrderVisualisationDto != null) {
                            uiHandlers.updateCodeInOrder(droppedNode.getAttribute(CodeDS.URN), codelistOrderVisualisationDto.getUrn(), order);
                        }
                    }
                }
                event.cancel();
            }
        });

        // Context menu

        createCodeMenuItem = new MenuItem(MetamacSrmWeb.getConstants().codeCreate());
        createCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newCodeWindow = new NewCodeWindow(MetamacSrmWeb.getConstants().codeCreate());
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

        deleteCodeMenuItem = new MenuItem(MetamacSrmWeb.getConstants().codeDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().codelistDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().codeDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodesTreeGrid.this.uiHandlers.deleteCode(selectedCode);
            }
        });
        deleteCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createCodeMenuItem, deleteCodeMenuItem);
    }

    public void setItems(ItemSchemeDto codelistMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos, CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        this.codelistOrderVisualisationDto = codelistOrderVisualisationDto;
        setItems(codelistMetamacDto, itemHierarchyDtos);
    }

    @Override
    public void setItems(ItemSchemeDto codelistMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.codelistMetamacDto = (CodelistMetamacDto) codelistMetamacDto;
        super.setItems(codelistMetamacDto, itemHierarchyDtos);
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
    protected void onNodeContextClick(String nodeName, ItemDto code) {
        selectedCode = code;
        createCodeMenuItem.setEnabled(CodesClientSecurityUtils.canCreateCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        deleteCodeMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName) && CodesClientSecurityUtils.canDeleteCode(codelistMetamacDto.getLifeCycle().getProcStatus()));
        showContextMenu();
    }

    private boolean isDroppable(TreeNode dropFolder) {
        // TODO Security
        // Replacing false root is not allowed
        return !("/".equals(getDropFolder().getName()));
    }
}
