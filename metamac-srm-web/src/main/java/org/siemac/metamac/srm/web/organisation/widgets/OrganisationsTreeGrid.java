package org.siemac.metamac.srm.web.organisation.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.widgets.ItemsTreeGrid;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.BaseOrganisationUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class OrganisationsTreeGrid extends ItemsTreeGrid {

    private NewOrganisationWindow        newOrganisationWindow;
    private DeleteConfirmationWindow     deleteConfirmationWindow;

    private MenuItem                     createOrganisationMenuItem;
    private MenuItem                     deleteOrganisationMenuItem;

    private OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
    private ItemDto                      selectedOrganisation;

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

    @Override
    public void setItems(ItemSchemeDto organisationSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.organisationSchemeMetamacDto = (OrganisationSchemeMetamacDto) organisationSchemeMetamacDto;
        super.setItems(organisationSchemeMetamacDto, itemHierarchyDtos);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.organisationSchemeMetamacDto = (OrganisationSchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseOrganisationUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    protected void onNodeClick(String nodeName, String organisationUrn) {
        if (!SCHEME_NODE_NAME.equals(nodeName)) {
            uiHandlers.goToOrganisation(organisationUrn);
        }
    }

    @Override
    protected void onNodeContextClick(String nodeName, ItemDto organisation) {
        selectedOrganisation = organisation;
        createOrganisationMenuItem.setEnabled(OrganisationsClientSecurityUtils.canCreateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                organisationSchemeMetamacDto.getType()));
        deleteOrganisationMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(nodeName)
                && OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType()));
        showContextMenu();
    }

}
