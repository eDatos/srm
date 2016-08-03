package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationSchemeOrganisationsPanel extends VLayout {

    private OrganisationSchemeMetamacDto organisationSchemeDto;

    private OrganisationSchemeUiHandlers uiHandlers;

    private ToolStrip                    toolStrip;
    private ToolStripButton              importOrganisationsButton;
    private ToolStripButton              exportOrganisationsButton;
    private ImportOrganisationsWindow    importOrganisationsWindow;

    // Organisation list
    private ToolStripButton              listGridNewButton;
    private ToolStripButton              listGridDeleteOrganisationButton;
    private NewOrganisationWindow        newOrganisationWindow;
    private DeleteConfirmationWindow     deleteConfirmationWindow;
    private CustomListGrid               organisationListGrid;

    // OrganisationTree
    private OrganisationsTreeGrid        organisationsTreeGrid;

    public OrganisationSchemeOrganisationsPanel() {
        setMargin(15);

        createImportationWindow();
        createDeleteConfirmationWindow();

        createToolStrip();
        createListGrid();
        createTreeGrid();
    }

    public void setUiHandlers(OrganisationSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        organisationsTreeGrid.setUiHandlers(uiHandlers);
        importOrganisationsWindow.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(OrganisationSchemeMetamacDto organisationSchemeDto) {
        this.organisationSchemeDto = organisationSchemeDto;
        // Show/hide organisation list and tree, depending on the organisation scheme type
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeDto.getType())) {
            showOrganisationTree();
        } else {
            showOrganisationList();
            // Security to create organisations
            listGridNewButton.setVisible(OrganisationsClientSecurityUtils.canCreateOrganisation(organisationSchemeDto));
        }
        importOrganisationsButton.setVisible(OrganisationsClientSecurityUtils.canImportOrganisations(organisationSchemeDto));
        exportOrganisationsButton.setVisible(OrganisationsClientSecurityUtils.canExportOrganisations(organisationSchemeDto));
        toolStrip.markForRedraw();

        // Update organisation scheme in tree grid
        organisationsTreeGrid.updateItemScheme(organisationSchemeDto);
    }

    public void setOrganisationList(OrganisationSchemeMetamacDto organisationSchemeDto, List<OrganisationMetamacVisualisationResult> organisations) {
        this.organisationSchemeDto = organisationSchemeDto;
        this.importOrganisationsWindow.setOrganisationScheme(organisationSchemeDto);
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeDto.getType())) {
            // Organisation hierarchy
            organisationsTreeGrid.setOrganisations(organisationSchemeDto, organisations);
            organisationsTreeGrid.setAutoFitMaxRecords(organisations.size() + 1);
        } else {
            // Organisation list
            OrganisationTypeEnum organisationTypeEnum = CommonUtils.getOrganisationTypeEnum(organisationSchemeDto.getType());
            OrganisationRecord[] records = new OrganisationRecord[organisations.size()];
            for (int i = 0; i < organisations.size(); i++) {
                records[i] = org.siemac.metamac.srm.web.organisation.utils.OrganisationsRecordUtils.getOrganisationRecord(organisations.get(i), organisationTypeEnum);
            }
            organisationListGrid.setData(records);
            organisationListGrid.setAutoFitMaxRecords(organisations.size() + 1);
        }
    }

    private void createImportationWindow() {
        importOrganisationsWindow = new ImportOrganisationsWindow();
        importOrganisationsWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });
    }

    private void createDeleteConfirmationWindow() {
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().organisationDeleteConfirmationTitle(), getConstants().organisationDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteOrganisations(getSelectedOrganisationUrns());
                deleteConfirmationWindow.hide();
            }
        });
    }

    private void createToolStrip() {
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        listGridNewButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        listGridNewButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newOrganisationWindow = new NewOrganisationWindow(getConstants().organisationCreate());
                newOrganisationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newOrganisationWindow.validateForm()) {
                            OrganisationMetamacDto organisationToCreate = newOrganisationWindow.getNewOrganisationDto(CommonUtils.getOrganisationTypeEnum(organisationSchemeDto.getType()));
                            organisationToCreate.setItemSchemeVersionUrn(organisationSchemeDto.getUrn());
                            uiHandlers.createOrganisation(organisationToCreate);
                            newOrganisationWindow.destroy();
                        }
                    }
                });
            }
        });
        toolStrip.addButton(listGridNewButton);

        listGridDeleteOrganisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        listGridDeleteOrganisationButton.setVisible(false);
        listGridDeleteOrganisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });
        toolStrip.addButton(listGridDeleteOrganisationButton);

        importOrganisationsButton = new CustomToolStripButton(getConstants().actionImportOrganisationsTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource()
                .getURL());
        importOrganisationsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importOrganisationsWindow.show();
            }
        });
        toolStrip.addButton(importOrganisationsButton);

        exportOrganisationsButton = new CustomToolStripButton(getConstants().actionExportOrganisationsTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource()
                .getURL());
        exportOrganisationsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportOrganisations(organisationSchemeDto.getUrn());
            }
        });
        toolStrip.addButton(exportOrganisationsButton);

        addMember(toolStrip);
    }

    private void createListGrid() {
        organisationListGrid = new CustomListGrid();
        organisationListGrid.setAutoFitData(Autofit.VERTICAL);
        ListGridField codeField = new ListGridField(OrganisationDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField nameField = new ListGridField(OrganisationDS.NAME, getConstants().nameableArtefactName());
        organisationListGrid.setFields(codeField, nameField);
        organisationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (organisationListGrid.getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(organisationListGrid.getSelectedRecords());
                } else {
                    listGridDeleteOrganisationButton.hide();
                }
            }
        });

        organisationListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((OrganisationRecord) event.getRecord()).getAttribute(OrganisationDS.URN);
                    uiHandlers.goToOrganisation(urn);
                }
            }
        });

        addMember(organisationListGrid);
    }

    private void createTreeGrid() {
        organisationsTreeGrid = new OrganisationsTreeGrid();
        addMember(organisationsTreeGrid);
    }

    private void showOrganisationList() {
        organisationListGrid.show();
        organisationsTreeGrid.hide();
    }

    private void showOrganisationTree() {
        listGridNewButton.hide();
        organisationListGrid.hide();
        organisationsTreeGrid.show();
    }

    private List<String> getSelectedOrganisationUrns() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : organisationListGrid.getSelectedRecords()) {
            OrganisationRecord organisationRecord = (OrganisationRecord) record;
            urns.add(organisationRecord.getUrn());
        }
        return urns;
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allOrganisationsCanBeDeleted = true;
        for (ListGridRecord record : records) {
            Boolean hasBeenPublished = ((OrganisationRecord) record).getSpecialOrganisationHasBeenPublished();
            if (!OrganisationsClientSecurityUtils.canDeleteOrganisation(organisationSchemeDto, hasBeenPublished)) {
                allOrganisationsCanBeDeleted = false;
            }
        }
        if (allOrganisationsCanBeDeleted) {
            listGridDeleteOrganisationButton.show();
        } else {
            listGridDeleteOrganisationButton.hide();
        }
    }
}
