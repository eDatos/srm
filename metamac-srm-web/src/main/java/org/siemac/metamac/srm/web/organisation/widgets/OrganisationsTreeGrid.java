package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.BaseOrganisationUiHandlers;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public class OrganisationsTreeGrid extends TreeGrid {

    private static final String          SCHEME_NODE_NAME = "scheme-node";

    private NewOrganisationWindow        newOrganisationWindow;
    private DeleteConfirmationWindow     deleteConfirmationWindow;

    private Menu                         contextMenu;

    private MenuItem                     createOrganisationMenuItem;
    private MenuItem                     deleteOrganisationMenuItem;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private ItemDto                      selectedOrganisation;

    private BaseOrganisationUiHandlers   uiHandlers;

    private HandlerRegistration          folderContextHandlerRegistration;
    private HandlerRegistration          leafContextHandlerRegistration;
    private HandlerRegistration          folderClickHandlerRegistration;
    private HandlerRegistration          leafClickHandlerRegistration;

    public OrganisationsTreeGrid() {

        setAutoFitMaxRecords(10);
        setAutoFitData(Autofit.VERTICAL);

        setShowOpenIcons(false);
        setShowDropIcons(false);
        setShowSelectedStyle(true);
        setShowPartialSelection(true);
        setCascadeSelection(false);
        setCanSort(false);
        setShowConnectors(true);
        setShowHeader(true);
        setLoadDataOnDemand(false);
        setSelectionType(SelectionStyle.SINGLE);
        setShowCellContextMenus(true);
        setLeaveScrollbarGap(Boolean.FALSE);

        TreeGridField codeField = new TreeGridField(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        TreeGridField nameField = new TreeGridField(OrganisationDS.NAME, getConstants().nameableArtefactName());

        setFields(codeField, nameField);

        // Menu

        createOrganisationMenuItem = new MenuItem(MetamacSrmWeb.getConstants().organisationCreate());
        createOrganisationMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newOrganisationWindow = new NewOrganisationWindow(MetamacSrmWeb.getConstants().organisationCreate());
                newOrganisationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newOrganisationWindow.validateForm()) {
                            OrganisationMetamacDto organisationMetamacDto = newOrganisationWindow.getNewOrganisationDto(CommonUtils.getOrganisationTypeEnum(organisationSchemeMetamacDto.getType()));
                            organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn()); // Set organisation scheme URN
                            organisationMetamacDto.setItemParentUrn(selectedOrganisation != null ? selectedOrganisation.getUrn() : null); // Set organisation parent URN
                            OrganisationsTreeGrid.this.uiHandlers.saveOrganisation(organisationMetamacDto);
                            newOrganisationWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteOrganisationMenuItem = new MenuItem(MetamacSrmWeb.getConstants().organisationDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().organisationDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().organisationDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                OrganisationsTreeGrid.this.uiHandlers.deleteOrganisation(selectedOrganisation);
            }
        });
        deleteOrganisationMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        contextMenu = new Menu();
        contextMenu.addItem(createOrganisationMenuItem);
        contextMenu.addItem(deleteOrganisationMenuItem);

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (ItemDto) event.getFolder().getAttributeAsObject(OrganisationDS.DTO));
            }
        });

        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (ItemDto) event.getLeaf().getAttributeAsObject(OrganisationDS.DTO));
            }
        });

        folderClickHandlerRegistration = addFolderClickHandler(new FolderClickHandler() {

            @Override
            public void onFolderClick(FolderClickEvent event) {
                onNodeClick(event.getFolder().getName(), event.getFolder().getAttribute(OrganisationDS.URN));
            }
        });

        leafClickHandlerRegistration = addLeafClickHandler(new LeafClickHandler() {

            @Override
            public void onLeafClick(LeafClickEvent event) {
                onNodeClick(event.getLeaf().getName(), event.getLeaf().getAttribute(OrganisationDS.URN));
            }
        });
    }

    public void setOrganisations(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.organisationSchemeMetamacDto = organisationSchemeMetamacDto;

        TreeNode[] treeNodes = new TreeNode[itemHierarchyDtos.size()];
        for (int i = 0; i < itemHierarchyDtos.size(); i++) {
            treeNodes[i] = createOrganisationTreeNode(itemHierarchyDtos.get(i));
        }

        TreeNode organisationSchemeTreeNode = createOrganisationSchemeTreeNode(organisationSchemeMetamacDto);
        organisationSchemeTreeNode.setChildren(treeNodes);

        Tree tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setData(new TreeNode[]{organisationSchemeTreeNode});
        setData(tree);
        getData().openAll();
    }

    public void setUiHandlers(BaseOrganisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void selectOrganisation(OrganisationMetamacDto organisationMetamacDto) {
        RecordList nodes = getDataAsRecordList();
        Record record = nodes.find(OrganisationDS.URN, organisationMetamacDto.getUrn());
        selectRecord(record);
    }

    public void removeHandlerRegistrations() {
        folderContextHandlerRegistration.removeHandler();
        leafContextHandlerRegistration.removeHandler();
        folderClickHandlerRegistration.removeHandler();
        leafClickHandlerRegistration.removeHandler();
    }

    private TreeNode createOrganisationSchemeTreeNode(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        TreeNode node = new TreeNode(SCHEME_NODE_NAME);
        node.setAttribute(OrganisationDS.CODE, organisationSchemeMetamacDto.getCode());
        node.setAttribute(OrganisationDS.NAME, InternationalStringUtils.getLocalisedString(organisationSchemeMetamacDto.getName()));
        return node;
    }

    private TreeNode createOrganisationTreeNode(ItemHierarchyDto itemHierarchyDto) {
        TreeNode node = new TreeNode(itemHierarchyDto.getItem().getId().toString());
        node.setAttribute(OrganisationDS.CODE, itemHierarchyDto.getItem().getCode());
        node.setAttribute(OrganisationDS.NAME, InternationalStringUtils.getLocalisedString(itemHierarchyDto.getItem().getName()));
        node.setAttribute(OrganisationDS.URN, itemHierarchyDto.getItem().getUrn());
        node.setAttribute(OrganisationDS.DTO, itemHierarchyDto.getItem());

        // Node children
        TreeNode[] children = new TreeNode[itemHierarchyDto.getChildren().size()];
        for (int i = 0; i < itemHierarchyDto.getChildren().size(); i++) {
            children[i] = createOrganisationTreeNode(itemHierarchyDto.getChildren().get(i));
        }
        node.setChildren(children);

        return node;
    }

    private void showContextMenu() {
        contextMenu.markForRedraw();
        contextMenu.showContextMenu();
    }

    private void onNodeClick(String nodeName, String organisationUrn) {
        if (!SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToOrganisation(organisationUrn);
        }
    }

    private void onNodeContextClick(String nodeName, ItemDto organisation) {
        selectedOrganisation = organisation;
        createOrganisationMenuItem.setEnabled(OrganisationsClientSecurityUtils.canCreateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus()));
        deleteOrganisationMenuItem
                .setEnabled(!SCHEME_NODE_NAME.equals(nodeName) && OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus()));
        showContextMenu();
    }

}
