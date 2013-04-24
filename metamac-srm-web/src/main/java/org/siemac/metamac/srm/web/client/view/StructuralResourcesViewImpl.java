package org.siemac.metamac.srm.web.client.view;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.presenter.StructuralResourcesPresenter;
import org.siemac.metamac.srm.web.client.view.handlers.StructuralResourcesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.CategorySchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.CodelistListGrid;
import org.siemac.metamac.srm.web.client.widgets.ConceptSchemeListGrid;
import org.siemac.metamac.srm.web.client.widgets.DsdListGrid;
import org.siemac.metamac.srm.web.client.widgets.OrganisationSchemeListGrid;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class StructuralResourcesViewImpl extends ViewWithUiHandlers<StructuralResourcesUiHandlers> implements StructuralResourcesPresenter.StructuralResourcesView {

    private static final String              CONTEXT_AREA_WIDTH = "*";

    private final DsdListGrid                dsdListGrid;
    private final ConceptSchemeListGrid      conceptSchemeListGrid;
    private final OrganisationSchemeListGrid organisationSchemeListGrid;
    private final CategorySchemeListGrid     categorySchemeListGrid;
    private final CodelistListGrid           codelistListGrid;

    private final SectionStack               lastModifiedArtifactsSectionStack;

    private VLayout                          panel;

    @Inject
    public StructuralResourcesViewImpl(DsdListGrid dsdListGrid, ConceptSchemeListGrid conceptSchemeListGrid, OrganisationSchemeListGrid organisationSchemeListGrid,
            CategorySchemeListGrid categorySchemeListGrid, CodelistListGrid codelistListGrid) {
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

        this.codelistListGrid = codelistListGrid;
        this.codelistListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                CodelistRecord record = (CodelistRecord) event.getRecord();
                getUiHandlers().goToCodelist(record.getUrn());
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

        SectionStackSection lastConceptSchemesModifiedSection = new SectionStackSection();
        lastConceptSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().conceptSchemeLastModified());
        lastConceptSchemesModifiedSection.setExpanded(false);
        lastConceptSchemesModifiedSection.setItems(this.conceptSchemeListGrid);

        SectionStackSection lastCodelistModifiedSection = new SectionStackSection();
        lastCodelistModifiedSection.setTitle(MetamacSrmWeb.getConstants().codelistLastModified());
        lastCodelistModifiedSection.setExpanded(false);
        lastCodelistModifiedSection.setItems(this.codelistListGrid);

        SectionStackSection lastDsdModifiedSection = new SectionStackSection();
        lastDsdModifiedSection.setTitle(MetamacSrmWeb.getConstants().dsdLastModified());
        lastDsdModifiedSection.setExpanded(false);
        lastDsdModifiedSection.setItems(this.dsdListGrid);

        SectionStackSection lastOrganisationSchemesModifiedSection = new SectionStackSection();
        lastOrganisationSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().organisationSchemeLastModified());
        lastOrganisationSchemesModifiedSection.setExpanded(false);
        lastOrganisationSchemesModifiedSection.setItems(this.organisationSchemeListGrid);

        SectionStackSection lastCatSchemesModifiedSection = new SectionStackSection();
        lastCatSchemesModifiedSection.setTitle(MetamacSrmWeb.getConstants().categorySchemeLastModified());
        lastCatSchemesModifiedSection.setExpanded(false);
        lastCatSchemesModifiedSection.setItems(this.categorySchemeListGrid);

        lastModifiedArtifactsSectionStack.setSections(lastConceptSchemesModifiedSection, lastCodelistModifiedSection, lastDsdModifiedSection, lastOrganisationSchemesModifiedSection,
                lastCatSchemesModifiedSection);

        // Add the ToolStrip to the Operation View layout container
        panel.addMember(this.lastModifiedArtifactsSectionStack);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void resetView() {
        for (int i = 0; i < lastModifiedArtifactsSectionStack.getSections().length; i++) {
            lastModifiedArtifactsSectionStack.collapseSection(i);
        }
    }

    @Override
    public void setDsdList(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        dsdListGrid.setDsds(dataStructureDefinitionMetamacDtos);
    }

    @Override
    public void setConceptSchemeList(List<ConceptSchemeMetamacBasicDto> conceptSchemeDtos) {
        conceptSchemeListGrid.setConceptSchemes(conceptSchemeDtos);
    }

    @Override
    public void setOrganisationSchemeList(List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos) {
        organisationSchemeListGrid.setOrganisationSchemes(organisationSchemeMetamacDtos);
    }

    @Override
    public void setCategorySchemesList(List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos) {
        categorySchemeListGrid.setCategorySchemes(categorySchemeMetamacDtos);
    }

    @Override
    public void setCodelistList(List<CodelistMetamacBasicDto> codelistMetamacDtos) {
        codelistListGrid.setCodelist(codelistMetamacDtos);
    }
}
