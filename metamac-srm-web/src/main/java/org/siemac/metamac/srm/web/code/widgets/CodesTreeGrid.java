package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;

public class CodesTreeGrid extends BaseCodesTreeGrid {

    private NewCodeWindow                     newCodeWindow;
    private SearchMultipleCodeHierarchyWindow searchMultipleCodeHierarchyWindow;
    private DeleteConfirmationWindow          deleteConfirmationWindow;

    private MenuItem                          createCodeMenuItem;
    private MenuItem                          addCodeMenuItem;
    private MenuItem                          deleteCodeMenuItem;

    private CodeMetamacVisualisationResult    selectedCode;

    // Internal representation that helps us to recover opened nodes after reloading tree
    private Map<String, String>               treeOpenStates = new HashMap<String, String>();

    public CodesTreeGrid() {
        super(true, false);

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

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
                            getBaseCodeUiHandlers().saveCode(codeMetamacDto);
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
                if (getSelectedRecord() instanceof TreeNode) {
                    searchMultipleCodeHierarchyWindow = new SearchMultipleCodeHierarchyWindow(codelistMetamacDto, (TreeNode) getSelectedRecord(), getBaseCodeUiHandlers());
                }
            }
        });

        deleteCodeMenuItem = new MenuItem(getConstants().codeDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistDeleteConfirmationTitle(), getConstants().codeDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getBaseCodeUiHandlers().deleteCode(codelistMetamacDto.getUrn(), selectedCode);
            }
        });
        deleteCodeMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createCodeMenuItem, addCodeMenuItem, deleteCodeMenuItem);

        // Tree open state

        addFolderOpenedHandler(new FolderOpenedHandler() {

            @Override
            public void onFolderOpened(FolderOpenedEvent event) {
                saveTreeOpenState();
            }
        });

        addFolderClosedHandler(new FolderClosedHandler() {

            @Override
            public void onFolderClosed(FolderClosedEvent event) {
                saveTreeOpenState();
            }
        });
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        selectedCode = code;
        createCodeMenuItem.setEnabled(canCreateCode());
        addCodeMenuItem.setEnabled(canCreateCode());
        deleteCodeMenuItem.setEnabled(canDeleteCode(nodeName));
        showContextMenu();
    }

    @Override
    public void setItems(ItemSchemeDto itemSchemeDto, List<CodeMetamacVisualisationResult> itemMetamacResults) {
        super.setItems(itemSchemeDto, itemMetamacResults);
        recoverOpenState();
    }

    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        searchMultipleCodeHierarchyWindow.setCodelists(result);
    }

    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        searchMultipleCodeHierarchyWindow.setCodes(codelistMetamacDto, codes);
    }

    private boolean canCreateCode() {
        return CodesClientSecurityUtils.canCreateCode(codelistMetamacDto);
    }

    private boolean canDeleteCode(String nodeName) {
        return !SCHEME_NODE_NAME.equals(nodeName) && CodesClientSecurityUtils.canDeleteCode(codelistMetamacDto);
    }

    private void saveTreeOpenState() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                treeOpenStates.put(itemSchemeDto.getUrn(), getOpenState());
            }
        });
    }

    private void recoverOpenState() {
        if (treeOpenStates.containsKey(itemSchemeDto.getUrn()) && !StringUtils.isBlank(treeOpenStates.get(itemSchemeDto.getUrn()))) {
            setOpenState(treeOpenStates.get(itemSchemeDto.getUrn()));
        }
    }
}
