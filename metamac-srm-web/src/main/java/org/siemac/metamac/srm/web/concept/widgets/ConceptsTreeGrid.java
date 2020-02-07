package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsTreeGridUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ConceptsTreeGrid extends ItemsTreeGrid {

    private NewConceptWindow          newConceptWindow;
    private DeleteConfirmationWindow  deleteConfirmationWindow;

    private MenuItem                  createConceptMenuItem;
    private MenuItem                  deleteConceptMenuItem;

    protected ConceptSchemeMetamacDto conceptSchemeMetamacDto;
    private ItemVisualisationResult   selectedConcept;

    public ConceptsTreeGrid() {
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
                            getBaseConceptUiHandlers().saveConcept(conceptMetamacDto);
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
                getBaseConceptUiHandlers().deleteConcept(selectedConcept);
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

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> concepts) {
        this.conceptSchemeMetamacDto = conceptSchemeMetamacDto;
        super.setItems(conceptSchemeMetamacDto, concepts);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.conceptSchemeMetamacDto = (ConceptSchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
    }

    @Override
    protected void onNodeClick(String nodeName, String urn) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            getBaseConceptUiHandlers().goToConceptScheme(urn);
        } else {
            getBaseConceptUiHandlers().goToConcept(urn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemVisualisationResult concept) {
        selectedConcept = concept;
        createConceptMenuItem.setEnabled(canCreateConcept());
        deleteConceptMenuItem.setEnabled(canDeleteConcept(nodeName));
        showContextMenu();
    }

    private boolean canCreateConcept() {
        return ConceptsClientSecurityUtils.canCreateConcept(conceptSchemeMetamacDto);
    }

    private boolean canDeleteConcept(String nodeName) {
        return !SCHEME_NODE_NAME.equals(nodeName) && ConceptsClientSecurityUtils.canDeleteConcept(conceptSchemeMetamacDto);
    }

    @Override
    protected TreeNode createItemTreeNode(ItemVisualisationResult itemVisualisationResult) {
        return ConceptsTreeGridUtils.createConceptTreeNode(SCHEME_NODE_NAME, (ConceptMetamacVisualisationResult) itemVisualisationResult);
    }

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getConceptDetailViewerFields();
    }

    protected BaseConceptUiHandlers getBaseConceptUiHandlers() {
        return (BaseConceptUiHandlers) uiHandlers;
    }
}
