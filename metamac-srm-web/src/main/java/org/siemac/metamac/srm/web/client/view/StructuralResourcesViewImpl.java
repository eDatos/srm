package org.siemac.metamac.srm.web.client.view;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.CategorySchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.ConceptSchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.DsdListGrid;
import org.siemac.metamac.srm.web.client.widgets.OrganisationSchemeListGrid;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class StructuralResourcesViewImpl extends ViewWithUiHandlers<StructuralResourcesUiHandlers> implements StructuralResourcesPresenter.StructuralResourcesView {

    private static final String              CONTEXT_AREA_WIDTH = "*";

    private final DsdListGrid                dsdListGrid;
    private final ConceptSchemeListGrid      conceptSchemeListGrid;
    private final OrganisationSchemeListGrid organisationSchemeListGrid;
    private final CategorySchemeListGrid     categorySchemeListGrid;

    private final SectionStack               lastModifiedArtifactsSectionStack;

    private VLayout                          panel;

    @Inject
    public StructuralResourcesViewImpl(DsdListGrid dsdListGrid, ConceptSchemeListGrid conceptSchemeListGrid, OrganisationSchemeListGrid organisationSchemeListGrid,
            CategorySchemeListGrid categorySchemeListGrid) {
        super();

        this.dsdListGrid = dsdListGrid;
        this.dsdListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                DsdRecord record = (DsdRecord) event.getRecord();
                getUiHandlers().goToDsd(record.getDsd().getUrn());
            }
        });

        this.conceptSchemeListGrid = conceptSchemeListGrid;
        this.conceptSchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                ConceptSchemeRecord record = (ConceptSchemeRecord) event.getRecord();
                getUiHandlers().goToConceptScheme(record.getUrn());

            }
        });

        this.organisationSchemeListGrid = organisationSchemeListGrid;
        this.organisationSchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                OrganisationSchemeRecord record = (OrganisationSchemeRecord) event.getRecord();
                getUiHandlers().goToOrganisationScheme(record.getUrn(), record.getOrganisationSchemeDto().getType());
            }
        });

        this.categorySchemeListGrid = categorySchemeListGrid;
        this.categorySchemeListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                CategorySchemeRecord record = (CategorySchemeRecord) event.getRecord();
                getUiHandlers().goToCategoryScheme(record.getUrn());
            }
        });

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        // Initialize the OperationsList View layout container
        panel.setStyleName("metamac-ContextArea");
        panel.setWidth(CONTEXT_AREA_WIDTH);

        // Section Stack
        lastModifiedArtifactsSectionStack = new SectionStack();
        lastModifiedArtifactsSectionStack.setWidth100();
        lastModifiedArtifactsSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        lastModifiedArtifactsSectionStack.setAnimateSections(true);
        lastModifiedArtifactsSectionStack.setOverflow(Overflow.HIDDEN);

        SectionStackSection lastDsdModifiedSection = new SectionStackSection();
        lastDsdModifiedSection.setTitle(MetamacSrmWeb.getConstants().dsdLastModified());
        lastDsdModifiedSection.setExpanded(false);
        lastDsdModifiedSection.setItems(this.dsdListGrid);

        SectionStackSection lastConceptSchemesModifiedSection = new SectionStackSection();
        lastConceptSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().conceptSchemeLastModified());
        lastConceptSchemesModifiedSection.setExpanded(false);
        lastConceptSchemesModifiedSection.setItems(this.conceptSchemeListGrid);

        SectionStackSection lastOrganisationSchemesModifiedSection = new SectionStackSection();
        lastOrganisationSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().organisationSchemeLastModified());
        lastOrganisationSchemesModifiedSection.setExpanded(false);
        lastOrganisationSchemesModifiedSection.setItems(this.organisationSchemeListGrid);

        SectionStackSection lastClassifModifiedSection = new SectionStackSection();
        lastClassifModifiedSection.setTitle(MetamacSrmWeb.getConstants().classificationLastModified());
        lastClassifModifiedSection.setExpanded(false);
        // lastClassifModifiedSection.setItems();

        SectionStackSection lastCatSchemesModifiedSection = new SectionStackSection();
        lastCatSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().categorySchemeLastModified());
        lastCatSchemesModifiedSection.setExpanded(false);
        lastCatSchemesModifiedSection.setItems(this.categorySchemeListGrid);

        lastModifiedArtifactsSectionStack.setSections(lastDsdModifiedSection, lastConceptSchemesModifiedSection, lastOrganisationSchemesModifiedSection, lastClassifModifiedSection,
                lastCatSchemesModifiedSection);

        // Add the ToolStrip to the Operation View layout container
        panel.addMember(this.lastModifiedArtifactsSectionStack);

    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    /*
     * GWTP will call setInSlot when a child presenter asks to be added under this view
     */
    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == StructuralResourcesPresenter.TYPE_SetContextAreaContentToolBar) {
            if (content != null) {
                Canvas[] canvas = ((ToolStrip) content).getMembers();
                for (int i = 0; i < canvas.length; i++) {
                    if (canvas[i] instanceof ToolStripButton) {
                        ((ToolStripButton) canvas[i]).deselect();
                    }
                }
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void resetView() {
        for (int i = 0; i < lastModifiedArtifactsSectionStack.getSections().length; i++) {
            lastModifiedArtifactsSectionStack.collapseSection(i);
        }
    }

    @Override
    public void setDsdList(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos) {
        dsdListGrid.setDsds(dataStructureDefinitionMetamacDtos);
    }

    @Override
    public void setConceptSchemeList(List<ConceptSchemeMetamacDto> conceptSchemeDtos) {
        conceptSchemeListGrid.setConceptSchemes(conceptSchemeDtos);
    }

    @Override
    public void setOrganisationSchemeList(List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos) {
        organisationSchemeListGrid.setOrganisationSchemes(organisationSchemeMetamacDtos);
    }

    @Override
    public void setCategorySchemesList(List<CategorySchemeMetamacDto> categorySchemeMetamacDtos) {
        categorySchemeListGrid.setCategorySchemes(categorySchemeMetamacDtos);
    }

}
