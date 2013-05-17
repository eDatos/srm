package org.siemac.metamac.srm.web.organisation.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsTreeGridUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.BaseOrganisationUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;

public class OrganisationsTreeGrid extends ItemsTreeGrid {

    private NewOrganisationWindow        newOrganisationWindow;
    private DeleteConfirmationWindow     deleteConfirmationWindow;

    private MenuItem                     createOrganisationMenuItem;
    private MenuItem                     deleteOrganisationMenuItem;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private ItemVisualisationResult      selectedOrganisation;

    private BaseOrganisationUiHandlers   uiHandlers;

    public OrganisationsTreeGrid() {

        // Context menu

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
                            OrganisationsTreeGrid.this.uiHandlers.createOrganisation(organisationMetamacDto);
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

        addItemsToContextMenu(createOrganisationMenuItem, deleteOrganisationMenuItem);
    }

    public void setOrganisations(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, List<OrganisationMetamacVisualisationResult> organisations) {
        this.organisationSchemeMetamacDto = organisationSchemeMetamacDto;
        super.setItems(organisationSchemeMetamacDto, organisations);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.organisationSchemeMetamacDto = (OrganisationSchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseOrganisationUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        this.uiHandlers = uiHandlers;
    }

    @Override
    protected void onNodeClick(String nodeName, String urn) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToOrganisationScheme(urn);
        } else {
            uiHandlers.goToOrganisation(urn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemVisualisationResult organisation) {
        selectedOrganisation = organisation;
        createOrganisationMenuItem.setEnabled(OrganisationsClientSecurityUtils.canCreateOrganisation(organisationSchemeMetamacDto));
        deleteOrganisationMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName) && OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeMetamacDto));
        showContextMenu();
    }

    @Override
    protected TreeNode createItemTreeNode(ItemVisualisationResult itemVisualisationResult) {
        return OrganisationsTreeGridUtils.createOrganisationTreeNode(SCHEME_NODE_NAME, (OrganisationMetamacVisualisationResult) itemVisualisationResult);
    }

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getOrganisationDetailViewerFields();
    }
}
