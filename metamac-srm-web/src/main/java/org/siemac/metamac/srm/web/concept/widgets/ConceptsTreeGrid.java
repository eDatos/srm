package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ConceptsTreeGrid extends ItemsTreeGrid {

    private NewConceptWindow         newConceptWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private MenuItem                 createConceptMenuItem;
    private MenuItem                 deleteConceptMenuItem;

    private ConceptSchemeMetamacDto  conceptSchemeMetamacDto;
    private ItemDto                  selectedConcept;

    private BaseConceptUiHandlers    uiHandlers;

    public ConceptsTreeGrid() {
        // Remove the filter edition handler and add a new one (to support filter by concept type and SDMX role)
        removeFilterEditionHandler();
        addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

            @Override
            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
                event.cancel();
                TreeNode[] treeNodes = tree.getAllNodes();

                String codeCriteria = event.getCriteria().getAttribute(ItemDS.CODE);
                String nameCriteria = event.getCriteria().getAttribute(ItemDS.NAME);
                String typeCriteria = event.getCriteria().getAttribute(ConceptDS.TYPE);
                String sdmxRoleCriteria = event.getCriteria().getAttribute(ConceptDS.SDMX_RELATED_ARTEFACT);

                if (StringUtils.isBlank(codeCriteria) && StringUtils.isBlank(nameCriteria) && StringUtils.isBlank(typeCriteria) && StringUtils.isBlank(sdmxRoleCriteria)) {
                    setData(tree);
                    return;
                } else {
                    List<TreeNode> matchingNodes = new ArrayList<TreeNode>();
                    for (TreeNode treeNode : treeNodes) {
                        if (!SCHEME_NODE_NAME.equals(treeNode.getName())) {
                            String code = treeNode.getAttributeAsString(ItemDS.CODE);
                            String name = treeNode.getAttributeAsString(ItemDS.NAME);
                            String type = treeNode.getAttributeAsString(ConceptDS.TYPE);
                            String sdmxRole = treeNode.getAttributeAsString(ConceptDS.SDMX_RELATED_ARTEFACT);

                            boolean matches = true;
                            if (codeCriteria != null && !StringUtils.containsIgnoreCase(code, codeCriteria)) {
                                matches = false;
                            }
                            if (nameCriteria != null && !StringUtils.containsIgnoreCase(name, nameCriteria)) {
                                matches = false;
                            }
                            if (typeCriteria != null && !StringUtils.containsIgnoreCase(type, typeCriteria)) {
                                matches = false;
                            }
                            if (sdmxRoleCriteria != null && !StringUtils.containsIgnoreCase(sdmxRole, sdmxRoleCriteria)) {
                                matches = false;
                            }
                            if (matches) {
                                matchingNodes.add(treeNode);
                            }
                        }
                    }
                    Tree resultTree = new Tree();
                    resultTree.setData(matchingNodes.toArray(new TreeNode[0]));
                    setData(resultTree);
                }
            }
        });

        // Add type and sdmxRelatedArtefact fields to ItemsTreeGrid fields

        TreeGridField type = new TreeGridField(ConceptDS.TYPE, getConstants().conceptType());
        TreeGridField sdmxRelatedArtefact = new TreeGridField(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());

        ListGridField[] fields = new ListGridField[getFields().length + 2];
        System.arraycopy(getFields(), 0, fields, 0, getFields().length);
        fields[fields.length - 2] = type;
        fields[fields.length - 1] = sdmxRelatedArtefact;

        setFields(fields);

        // Context menu

        createConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptCreate());
        createConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newConceptWindow = new NewConceptWindow(MetamacSrmWeb.getConstants().conceptCreate(), conceptSchemeMetamacDto.getType());
                newConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptWindow.validateForm()) {
                            ConceptMetamacDto conceptMetamacDto = newConceptWindow.getNewConceptDto();
                            conceptMetamacDto.setItemSchemeVersionUrn(conceptSchemeMetamacDto.getUrn()); // Set concept scheme URN
                            conceptMetamacDto.setItemParentUrn(selectedConcept != null ? selectedConcept.getUrn() : null); // Set concept parent URN
                            ConceptsTreeGrid.this.uiHandlers.saveConcept(conceptMetamacDto);
                            newConceptWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().conceptDeleteTitle(), MetamacSrmWeb.getConstants().conceptDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConceptsTreeGrid.this.uiHandlers.deleteConcept(selectedConcept);
            }
        });
        deleteConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createConceptMenuItem, deleteConceptMenuItem);
    }

    @Override
    public void setItems(ItemSchemeDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.conceptSchemeMetamacDto = (ConceptSchemeMetamacDto) conceptSchemeMetamacDto;
        super.setItems(conceptSchemeMetamacDto, itemHierarchyDtos);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.conceptSchemeMetamacDto = (ConceptSchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    protected TreeNode createItemTreeNode(ItemHierarchyDto itemHierarchyDto, String itemParentUrn) {
        TreeNode node = new TreeNode(itemHierarchyDto.getItem().getId().toString());
        node.setAttribute(ConceptDS.CODE, itemHierarchyDto.getItem().getCode());
        node.setAttribute(ConceptDS.NAME, InternationalStringUtils.getLocalisedString(itemHierarchyDto.getItem().getName()));
        node.setAttribute(ConceptDS.URN, itemHierarchyDto.getItem().getUrn());
        node.setAttribute(ConceptDS.DTO, itemHierarchyDto.getItem());
        node.setAttribute(ConceptDS.ITEM_PARENT_URN, itemParentUrn);
        node.setAttribute(
                ConceptDS.TYPE,
                ((ConceptMetamacDto) itemHierarchyDto.getItem()).getType() != null ? CommonWebUtils.getElementName(((ConceptMetamacDto) itemHierarchyDto.getItem()).getType().getIdentifier(),
                        ((ConceptMetamacDto) itemHierarchyDto.getItem()).getType().getDescription()) : StringUtils.EMPTY);
        node.setAttribute(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(((ConceptMetamacDto) itemHierarchyDto.getItem()).getSdmxRelatedArtefact()));

        // Node children
        TreeNode[] children = new TreeNode[itemHierarchyDto.getChildren().size()];
        for (int i = 0; i < itemHierarchyDto.getChildren().size(); i++) {
            children[i] = createItemTreeNode(itemHierarchyDto.getChildren().get(i), itemHierarchyDto.getItem().getUrn());
        }
        node.setChildren(children);

        return node;
    }

    @Override
    protected void onNodeClick(String nodeName, String conceptUrn) {
        if (!SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToConcept(conceptUrn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemDto concept) {
        selectedConcept = concept;
        createConceptMenuItem.setEnabled(ConceptsClientSecurityUtils.canCreateConcept(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto)));
        deleteConceptMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName)
                && ConceptsClientSecurityUtils.canDeleteConcept(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                        CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto)));
        showContextMenu();
    }
}
