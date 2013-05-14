package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class CodesTreeGrid extends BaseCodesTreeGrid {

    private NewCodeWindow                     newCodeWindow;
    private SearchMultipleCodeHierarchyWindow searchMultipleCodeHierarchyWindow;
    private DeleteConfirmationWindow          deleteConfirmationWindow;

    private MenuItem                          createCodeMenuItem;
    private MenuItem                          addCodeMenuItem;
    private MenuItem                          deleteCodeMenuItem;

    private CodeMetamacVisualisationResult    selectedCode;

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
                if (getSelectedRecord() instanceof TreeNode) {
                    searchMultipleCodeHierarchyWindow = new SearchMultipleCodeHierarchyWindow(codelistMetamacDto, (TreeNode) getSelectedRecord(), uiHandlers);
                }
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

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        selectedCode = code;
        createCodeMenuItem.setEnabled(canCreateCode());
        addCodeMenuItem.setEnabled(canCreateCode());
        deleteCodeMenuItem.setEnabled(canDeleteCode(nodeName));
        showContextMenu();
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

    @Override
    protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
        DetailViewer detailViewer = new DetailViewer();
        detailViewer.setFields(ResourceFieldUtils.getCodeDetailViewerFields());
        detailViewer.setData(new Record[]{record});
        return detailViewer;
    }
}
